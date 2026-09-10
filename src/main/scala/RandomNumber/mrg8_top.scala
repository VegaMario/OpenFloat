package RandomNumber

import chisel3._
import chisel3.util.{Decoupled, log2Ceil, switch, is}

object Mrg8Phase extends ChiselEnum {
  val Idle, Seed, BroadcastSeed, LoadMatrices, ReadJump, WaitJump,
      Ready = Value
}

object Mrg8CorePhase extends ChiselEnum {
  val Idle, Generate, WaitGenerate, Output = Value
}

/** Shared initialization and matrix broadcasts, with independent generation.
  *
  * start/start_ready initializes a session; wmatrix loads the full column-major
  * table. generate(i)/generate_ready(i) requests eight integers from core i.
  * Each output(i) has its own ready/valid handshake and holding register.
  * A8 is supplied in row-major, newest-first software state order.
  */
class mrg8_top(
    num_cores: Int = 1,
    A8: Seq[Seq[Long]],
    total_matrices: Int = 247,
    p_load: Int = 1,
    seed_bw: Int = 32,
    matrix_bw: Int = 31
) extends Module {
  require(num_cores > 0 && total_matrices > 0)
  private val jump_bits = log2Ceil(num_cores)
  val idx_bits: Int = math.max(1, jump_bits)
  require(jump_bits <= total_matrices)

  val io = IO(new Bundle {
    val start = Input(Bool())
    val start_ready = Output(Bool())
    val seed = Input(UInt(seed_bw.W))
    val matrix_ofst = Input(UInt(math.max(1, log2Ceil(total_matrices + 1)).W))
    val wmatrix = Flipped(Decoupled(Vec(p_load, UInt(matrix_bw.W))))
    val initialized = Output(Bool())
    val init_done = Output(Bool())
    val busy = Output(Bool())
    val phase = Output(Mrg8Phase())
    val generate = Input(Vec(num_cores, Bool()))
    val generate_ready = Output(Vec(num_cores, Bool()))
    val core_busy = Output(Vec(num_cores, Bool()))
    val output = Vec(num_cores, Decoupled(Vec(8, UInt(matrix_bw.W))))
  })

  val seed_generator = Module(new mrg8.yzero_gen(seed_bw, matrix_bw))
  val matrices = Module(new mrg8.jump_matrix(total_matrices, num_cores, matrix_bw, p_load))
  val cores = Seq.tabulate(num_cores)(id =>
    Module(new mrg8.mrg8_core(matrix_bw, A8, id, idx_bits)))

  val phase = RegInit(Mrg8Phase.Idle)
  val initialized = RegInit(false.B)
  val init_done = RegInit(false.B)
  val matrix_offset = RegInit(0.U(io.matrix_ofst.getWidth.W))
  val jump_index = RegInit(0.U(math.max(1, log2Ceil(jump_bits + 1)).W))
  val row_index = RegInit(0.U(3.W))
  val output_data = RegInit(VecInit(Seq.fill(num_cores)(VecInit(Seq.fill(8)(0.U(matrix_bw.W))))))
  val core_phase = RegInit(VecInit(Seq.fill(num_cores)(Mrg8CorePhase.Idle)))
  val core_row = RegInit(VecInit(Seq.fill(num_cores)(0.U(3.W))))
  val all_idle = core_phase.map(_ === Mrg8CorePhase.Idle).reduce(_ && _)
  val enabled = !reset.asBool

  io.phase := phase
  io.initialized := initialized
  io.init_done := init_done
  io.busy := (phase =/= Mrg8Phase.Idle && phase =/= Mrg8Phase.Ready) || !all_idle
  // Reinitialization cannot discard an in-flight result or an unconsumed output.
  io.start_ready := enabled && all_idle && (phase === Mrg8Phase.Idle || phase === Mrg8Phase.Ready)
  init_done := false.B

  val start_fire = io.start && io.start_ready
  seed_generator.io.start := start_fire
  seed_generator.io.seed := io.seed // sampled by the seed generator on start_fire
  matrices.io.matrix_ofst := matrix_offset
  matrices.io.coreid_bits := jump_bits.U
  matrices.io.load := enabled && phase === Mrg8Phase.LoadMatrices
  io.wmatrix.ready := matrices.io.load && !matrices.io.load_done
  matrices.io.wmatrix.valid := io.wmatrix.fire
  matrices.io.wmatrix.data := io.wmatrix.bits

  val all_seed_ready = cores.map(_.io.input.ready).reduce(_ && _)
  val all_jump_ready = cores.map(_.io.jump.ready).reduce(_ && _)
  val all_jump_done = cores.map(_.io.jump.done).reduce(_ && _)
  val jumping = phase === Mrg8Phase.ReadJump || phase === Mrg8Phase.WaitJump
  val broadcast_seed = enabled && phase === Mrg8Phase.BroadcastSeed && all_seed_ready
  val broadcast_row = enabled && jumping && matrices.io.rmatrix.valid

  // Eight read requests, then a pause until all cores commit this matrix.
  // WaitJump still broadcasts the final synchronous response.
  matrices.io.read := enabled && phase === Mrg8Phase.ReadJump && all_jump_ready
  when(broadcast_row) { assert(all_jump_ready, "jump row response must be accepted by every core") }

  for ((core, id) <- cores.zipWithIndex) {
    val available = enabled && phase === Mrg8Phase.Ready
    val generating = available && (core_phase(id) === Mrg8CorePhase.Generate ||
      core_phase(id) === Mrg8CorePhase.WaitGenerate)
    val generate_row = available && core_phase(id) === Mrg8CorePhase.Generate && core.io.mrg8.ready
    io.generate_ready(id) := available && core_phase(id) === Mrg8CorePhase.Idle && !start_fire
    io.core_busy(id) := core_phase(id) =/= Mrg8CorePhase.Idle
    io.output(id).valid := available && core_phase(id) === Mrg8CorePhase.Output
    io.output(id).bits := output_data(id)

    core.io.opcode := Mux(phase === Mrg8Phase.BroadcastSeed, 0.U,
      Mux(jumping, 1.U, Mux(generating, 2.U, 3.U)))
    core.io.input.valid := broadcast_seed || broadcast_row
    core.io.input.data := Mux(phase === Mrg8Phase.BroadcastSeed,
      seed_generator.io.yzero, matrices.io.rmatrix.data)
    core.io.mrg8.proceed := generate_row
    when(jumping) { assert(core.io.jump.done === cores.head.io.jump.done) }

    when(io.generate(id) && io.generate_ready(id)) {
      core_row(id) := 0.U
      core_phase(id) := Mrg8CorePhase.Generate
    }
    when(generate_row) {
      core_row(id) := core_row(id) + 1.U
      when(core_row(id) === 7.U) { core_phase(id) := Mrg8CorePhase.WaitGenerate }
    }
    when(available && core_phase(id) === Mrg8CorePhase.WaitGenerate && core.io.output.valid) {
      output_data(id) := core.io.output.data
      core_phase(id) := Mrg8CorePhase.Output
    }
    when(io.output(id).fire) { core_phase(id) := Mrg8CorePhase.Idle }
  }

  when(start_fire) {
    // Offset is a jump-table index: core i starts i * 2^offset steps ahead.
    assert((io.matrix_ofst +& jump_bits.U) <= total_matrices.U)
    matrix_offset := io.matrix_ofst
    initialized := false.B
    row_index := 0.U
    jump_index := 0.U
    phase := Mrg8Phase.Seed
  }

  switch(phase) {
    is(Mrg8Phase.Seed) {
      when(seed_generator.io.done) { phase := Mrg8Phase.BroadcastSeed }
    }
    is(Mrg8Phase.BroadcastSeed) {
      when(broadcast_seed) { phase := Mrg8Phase.LoadMatrices }
    }
    is(Mrg8Phase.LoadMatrices) {
      when(matrices.io.load_done) {
        if (jump_bits == 0) {
          // Core zero needs no jump, but still follows seed and table loading.
          initialized := true.B
          init_done := true.B
          phase := Mrg8Phase.Ready
        } else {
          row_index := 0.U
          phase := Mrg8Phase.ReadJump
        }
      }
    }
    is(Mrg8Phase.ReadJump) {
      when(matrices.io.read) {
        row_index := row_index + 1.U
        when(row_index === 7.U) { phase := Mrg8Phase.WaitJump }
      }
    }
    is(Mrg8Phase.WaitJump) {
      when(all_jump_done) {
        when(jump_index === math.max(0, jump_bits - 1).U) {
          assert(matrices.io.read_done, "all requested jump rows must have returned")
          initialized := true.B
          init_done := true.B
          phase := Mrg8Phase.Ready
        }.otherwise {
          jump_index := jump_index + 1.U
          row_index := 0.U
          phase := Mrg8Phase.ReadJump
        }
      }
    }
  }
}
