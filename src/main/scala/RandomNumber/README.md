# MRG8 hardware modules

`mrg8.scala` contains the building blocks and one matrix-vector core.
`mrg8_top.scala` adds a separate FSM-controlled top with one shared seed generator,
one shared jump-matrix store, and a configurable number of cores.

## Arithmetic and state ordering

The default recurrence uses 31-bit state modulo `2^31 - 1`. The software's residue
representation is preserved: a positive multiple of the modulus reduces to the
modulus, while an all-zero sum remains zero. The seed port defaults to 32 bits;
the seed expander always maintains the software's 64-bit running MCG state.

`yvec` is ordered newest first, like `original/mrg8.cpp`: applying `A^8` yields
the next state vector. To consume the eight newly generated integers in
chronological order, read the output vector from index 7 down to index 0.
`A8` must be passed as eight rows, each containing eight nonnegative values.
The core derives multiplier and adder widths from `matrix_bw`; changing that
width does not automatically supply coefficients for another generator.

## Memory

`mem` and `banked_mem` return `rvalid` one cycle after an enabled read. Their data
is zero when invalid. A read response remains visible when the current read
enable goes low. `banked_mem.waddr` is a vector of independent bank addresses.

`jump_matrix` accepts consecutive words in the column-major order of the
software's `jump_matrix.txt`, starting at matrix zero. `p_load` can be 1, 2, 4,
or 8. Word `matrix * 64 + column * 8 + row` is stored at address
`matrix * 8 + row`, bank `(row + column) % 8`. Reading a row reverses that bank
rotation; no transpose buffer or extra write cycles are needed.

- Hold `load` high and present each beat with `wmatrix.valid`. Valid-low cycles
  are bubbles. `load_done` asserts only after the final beat is written and
  remains asserted until the next load pass. Writes after completion are ignored.
- Hold `read` high to issue one row per cycle. The first request captures
  `matrix_ofst` and `coreid_bits`. It reads `coreid_bits` consecutive matrices
  beginning at `matrix_ofst`, eight rows per matrix. `rmatrix.valid` marks each
  response; `read_done` asserts with the final response and stays asserted.
  A zero matrix count completes without producing a response.
- Lowering an enable before completion pauses that pass. After completion,
  deassert and reassert the enable to restart at the beginning. Loading a new
  table cancels the read pass. Do not consume read data while loading.
- Reads must stay within the loaded table. Hardware assertions check the matrix
  count and offset at the start of a read pass. There is no response backpressure;
  the consumer must reserve room for each issued read's one-cycle-later response.

## Core request protocol

`opcode` selects requests: 0 loads a state vector, 1 submits a jump matrix row,
2 submits an `A8` row, and 3 pauses submission.

- State loads are accepted on `input.valid && input.ready` with opcode 0.
  A load restores the original `core_idx` jump-bit state and row counters.
- Jump rows are accepted on `input.valid && jump.ready`. Submit all eight rows
  of each successive jump matrix. `jump.done` pulses after the eighth result
  has returned, even if the current jump-index bit is zero and the state is
  unchanged. The jump index then shifts right by one bit.
- With opcode 2, each `mrg8.proceed && mrg8.ready` submits **one row** from the
  stored `A8` matrix. Eight accepted row requests produce one output vector.
- `busy` remains high from the first accepted row until the eighth result is
  committed. The operation type is captured on the first row. Requests for
  another operation are not accepted while busy. Changing the opcode pauses
  further rows of the current operation; already accepted rows continue through
  the pipeline and their results are collected regardless of opcode. Resume the
  original opcode to submit any remaining rows.
- `output.valid` is a one-cycle pulse when an `A8` operation commits. There is no
  output ready input. Output data holds the current state until a later commit
  or accepted seed load.
- Reset aborts the operation and clears the valid pipelines. Seed the core before
  generation; reset initializes its state vector to zero.

For the default widths, the arithmetic pipeline has one multiplier stage, three
adder stages, and three modulo stages, in addition to the core's row staging and
state commit. `yvec` stays constant throughout a matrix operation.

## Multicore top

Instantiate `new mrg8_top(num_cores = N, A8 = rows, ...)`. `A8` is the same
row-major matrix used by each core. Other parameters are `total_matrices` (247),
`p_load` (1), `seed_bw` (32), and `matrix_bw` (31). Core IDs run from zero to
`N - 1`; `idx_bits = max(1, ceil(log2(N)))`. A single core skips jump-ahead
because its ID is zero. Non-power-of-two core counts are supported.

The shared initialization FSM follows these phases:

1. `Idle`: accepts `start && start_ready`, sampling `seed` and `matrix_ofst`.
2. `Seed`: the one shared generator expands the seed.
3. `BroadcastSeed`: all cores accept the same eight-value seed vector on the
   same clock edge.
4. `LoadMatrices`: accept the complete column-major table through
   `wmatrix.valid/ready/bits`. There are `total_matrices * 64 / p_load` beats.
   Only valid/ready transfers count; the source may insert bubbles.
5. `ReadJump` / `WaitJump`: read eight rows and broadcast each response to every
   core together. Pause SRAM requests after the eighth request, pass through its
   delayed response, and wait for every core's `jump.done` before the next
   adjacent matrix. Read `ceil(log2(N))` matrices starting at `matrix_ofst`.
6. `Ready`: shared initialization is complete and the FSM stays here while
   cores generate independently. `init_done` pulses on entry after initialization,
   while `initialized` stays high until reset or the next accepted start.

Each core has a separate `Idle -> Generate -> WaitGenerate -> Output -> Idle`
controller and an independent output holding register:

- `generate(i) && generate_ready(i)` requests exactly eight integers from core
  `i`. The controller submits its eight A8 rows without further external pulses.
  Deassert `generate(i)` after its handshake for a single request. Holding it
  high requests another vector whenever that core is ready again.
- `output(i).bits(element)` presents that core's newest-first vector.
  `output(i).valid` and the entire vector stay stable until `output(i).ready`
  accepts it. Only that core waits for its pending output; other cores can
  receive requests, compute, and transfer results independently.
- `core_busy(i)` reports generation or an unconsumed output for that core.
  During shared initialization, use the global `busy`/`phase` signals; no
  generation requests are accepted until initialization is complete.
- Each core accepts at most one outstanding generation request. Requests can
  be issued simultaneously to any subset of cores or staggered arbitrarily.
  Cores retain their own state and advance only when requested.

A new `start` is accepted only when all cores are idle and no outputs are pending.
An accepted start takes priority over simultaneous generation requests. An
unaccepted start does not block otherwise-ready cores. To reinitialize, finish
outstanding requests, consume their outputs, and handshake `start/start_ready`.
Every accepted start repeats seed creation, table loading, and synchronized
jump-ahead; it does not reuse a previous session's table implicitly.

`matrix_ofst` is a **table index**, not a direct step count. With the software's
power table, core `i` starts at `A^(i * 2^matrix_ofst) y0`. The selected matrix
range must fit in the table; the top asserts this on an accepted start. Each
output vector keeps the core's newest-first ordering. Global `busy` is high
during initialization or whenever any core is generating or holding an output.
`phase` exposes the shared initialization phase and stays `Ready` during generation.
Reset aborts initialization, all active cores, and any pending outputs.

## Verification

Run `sbt 'testOnly RandomNumber.Mrg8TopSpec RandomNumber.Mrg8Spec'`. Tests cover software-compatible
seeding, synchronous memory responses, modular boundaries and valid bubbles,
column-major loading for every supported lane count, pauses/reloads/repeated
reads, jump-bit selection, scalar-sequence equivalence, reseeding, opcode changes,
reset during an operation, and a non-default arithmetic width. The suite uses
the project's shared Verilator configuration.

Top-level tests cover 1, 2, 3, and 8 cores, the complete 247-matrix table, the
highest jump offset, shared initialization, non-power-of-two configurations,
reinitialization, independent and simultaneous requests, per-core output
backpressure, randomized request/response timing, and reset during active work. Results are compared with independent modular matrix
arithmetic and the scalar MRG8 recurrence.
