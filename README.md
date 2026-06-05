# OpenFloat

**Code Generators for Floating-Point Unit Design in Integrated Circuits**

OpenFloat is a parameterized floating-point unit (FPU) design generator developed using [Chisel](https://www.chisel-lang.org/), a hardware construction language embedded in Scala. It generates synthesizable RTL for floating-point arithmetic operations targeting FPGA and ASIC implementations.

## Overview

OpenFloat provides a library of highly configurable floating-point arithmetic modules that support multiple IEEE 754 precision formats. All modules are parameterized by a `FloatingPointFormat` trait, which defines the exponent and mantissa widths. This allows the same module to be instantiated for any standard format (FP16, BF16, FP32, FP64, FP128) or a custom format.

## Project Structure

```
OpenFloat/
├── src/main/scala/
│   ├── FloatingPoint/          # Core FPU modules
│   │   ├── FloatingPointFormat.scala # Format definitions and FPModule base class
│   │   └── fpu.scala           # All floating-point operation implementations
│   ├── Primitives/             # Low-level building blocks
│   │   ├── primitives.scala    # Arithmetic primitives (adders, dividers, CORDIC, hpmath, etc.)
│   │   └── convert.scala       # IEEE 754 format conversion utilities
│   ├── Generate/               # RTL generation utilities
│   │   └── generate.scala      # SystemVerilog output generator
│   └── TB/                     # Standalone test benches (run via `runMain`)
│       └── testbench.scala     # Example/legacy verification harnesses
├── src/test/scala/             # ScalaTest accuracy regression suites (run via `sbt test`)
│   ├── RoundingSpec.scala      # FP_add / FP_mult vs correctly-rounded FP64
│   ├── DivSqrtSpec.scala       # FP_div / FP_sqrt vs correctly-rounded FP64
│   ├── TranscendentalSpec.scala# FP_cos / FP_atan / FP_exp accuracy (avg & max error)
│   ├── CordicCoreSpec.scala    # cordic / ucordic core cos/sin accuracy
│   └── FP_accSpec.scala        # FP_acc exactness vs BigDecimal sum + control tests
├── build.sbt                   # SBT build configuration
├── LICENSE                     # BSD-style license
└── README.md                   # This file
```

## Supported Floating-Point Formats

OpenFloat supports standard IEEE 754 formats, BFloat16, and custom formats via the `FloatingPointFormat` trait.

| Format | Object | Total Bits | Sign | Exponent | Mantissa | Bias |
|--------|--------|------------|------|----------|----------|------|
| Half | `FP16` | 16 | 1 | 5 | 10 | 15 |
| BFloat16 | `BF16` | 16 | 1 | 8 | 7 | 127 |
| Single | `FP32` | 32 | 1 | 8 | 23 | 127 |
| Double | `FP64` | 64 | 1 | 11 | 52 | 1023 |
| Quad | `FP128` | 128 | 1 | 15 | 112 | 16383|

You can also define arbitrary custom formats using `CustomFormat(exponent, mantissa)`.

## Floating-Point Modules

All arithmetic and transcendental modules are **IEEE round-to-nearest-even (RNE)**
internally. The basic operations (`FP_add`, `FP_mult`, `FP_div`, `FP_sqrt`) are
bit-exact correctly-rounded against a software FP64 reference, and the transcendental
units (`FP_cos`, `FP_atan`, `FP_exp`) achieve near-correctly-rounded accuracy (low
single-digit ULP) by carrying guard bits and rounding at every datapath boundary.
See [Precision and Rounding](#precision-and-rounding) for details.

### Basic Arithmetic Operations

| Module | Description | Parameters |
|--------|-------------|------------|
| `FP_add` | Floating-point addition (RNE) | `FORMAT`: FloatingPointFormat, `latency`: pipeline depth (any value >= 1) |
| `FP_mult` | Floating-point multiplication (RNE) | `FORMAT`: FloatingPointFormat, `latency`: pipeline depth (any value >= 1) |
| `FP_div` | Digit-recurrence division (RNE) | `FORMAT`: FloatingPointFormat, `L`: iterations, `latency`: pipeline stages |
| `FP_sqrt` | Digit-recurrence square root (RNE) | `FORMAT`: FloatingPointFormat, `L`: iterations, `latency`: pipeline stages |

### Transcendental Functions

| Module | Description | Parameters |
|--------|-------------|------------|
| `FP_cos` | Cosine and Sine (CORDIC-based) | `FORMAT`: FloatingPointFormat, `iters`: CORDIC iterations |
| `FP_atan` | Arctangent (CORDIC-based) | `FORMAT`: FloatingPointFormat, `iters`: CORDIC iterations |
| `FP_exp` | Exponential function (e^x) | `FORMAT`: FloatingPointFormat |

> **Accuracy note:** the transcendental units feed their inputs through a
> float-to-fixed converter, a CORDIC engine, and a fixed-to-float converter. All three
> stages now round-to-nearest and carry internal guard bits, so results are accurate to
> a few ULP across the full input range (see [Precision and Rounding](#precision-and-rounding)).

### Utility Modules

| Module | Description | Parameters |
|--------|-------------|------------|
| `FP_acc` | Fixed-point (Kulisch-style) streaming accumulator. Sums terms **exactly** in a wide signed two's-complement register and rounds-to-nearest-even only once, at the final normalization. Supports `in_last`/`clear` streaming control and reports `out_inexact`/`out_overflow`. | `FORMAT`: FloatingPointFormat, `maxExp`/`minExp`: supported unbiased exponent range, `iters`: default batch length |
| `FP_floor` | Floor function with dual outputs: integer part (`out_whole`) and fractional part (`out_frac`). Useful as a range reduction building block (e.g., used by `FP_cos`). Correctly handles negative exact integers (where `floor(x) == x`). | `FORMAT`: FloatingPointFormat |
| `FloatTOFixed` | Float to fixed-point conversion. Carries guard bits through the alignment shift and rounds-to-nearest-even (with a sticky bit), instead of truncating. | `FORMAT`: FloatingPointFormat, `ibits`: integer bits, `fbits`: fractional bits |
| `FixedTOFloat` | Fixed-point to float conversion. Rounds-to-nearest-even when extracting the mantissa (with carry-out exponent bump), instead of truncating. | `FORMAT`: FloatingPointFormat, `ibits`: integer bits, `fbits`: fractional bits |

### Pipeline Depth

`FP_add` and `FP_mult` accept any `latency >= 1`. Pipeline registers are automatically distributed across 10 internal stage boundaries using the same `pipe_skip`/`pipe_map` algorithm used by the digit-recurrence primitives (`divider`, `frac_sqrt`, `cordic`). Higher latency values improve timing at the cost of throughput latency; values above 10 stack additional registers at evenly-spaced boundaries.

## Primitive Modules

The `Primitives` package provides low-level building blocks:

| Module | Description |
|--------|-------------|
| `LZC` | Leading Zero Counter with tree-based reduction |
| `full_adder` | Parameterized width adder with carry |
| `full_subtractor` | Parameterized width subtractor with borrow |
| `multiplier` | Basic integer multiplication |
| `divider` | Digit-recurrence integer divider (pipelined) |
| `frac_sqrt` | Fractional square root for normalized numbers |
| `cordic` | Fixed-point CORDIC processor (rotation/vectoring modes). Carries internal fractional guard bits during the iterations and rounds-to-nearest at the output. |
| `ucordic` | Universal CORDIC with runtime mode selection (`ctrl_mode`, `ctrl_vectoring`). A single instance can compute cos, sin, atan, multiply, divide, sinh, cosh, atanh, exp, and ln. Supports **circular** (mu=1), **linear** (mu=0), and **hyperbolic** (mu=-1) modes in both rotation and vectoring. Correctly implements hyperbolic iteration index repetition (indices 4, 13, 40, 121) for convergence. Carries internal fractional guard bits and rounds-to-nearest at the output. |
| `cos`, `atan`, `exp` | Fixed-point trigonometric/exponential wrappers |
| `hpmath` | High-precision (BigDecimal) generators for the CORDIC constant tables (`atan`, `atanh`, `sqrt`, `PI`) and gain inverses. Computed at ~120 significant digits and rounded-to-nearest so the constants are accurate for every format through FP128. |

## Software Conversion Utilities

The `convert` object provides Scala-side IEEE 754 conversion functions for testbench use:

| Function | Description |
|----------|-------------|
| `convert_string_to_IEEE_754(str, fmt)` | Converts a decimal string to an IEEE 754 bit pattern for any `FloatingPointFormat` |
| `convert_IEEE754_to_Decimal(num, fmt)` | Converts an IEEE 754 bit pattern back to a `BigDecimal` value |

These accept any `FloatingPointFormat` (including `BF16`, `CustomFormat`, etc.), making it easy to generate test vectors and examine outputs for any format.

## Interface Specification

All modules implement a standard ready-valid handshaking protocol for flow control:

### Input Interface
```scala
val in_ready = Output(Bool())  // Module can accept new input
val in_valid = Input(Bool())   // Input data is valid
val in_a     = Input(UInt())   // Input operand A
val in_b     = Input(UInt())   // Input operand B (for binary ops)
```

### Output Interface
```scala
val out_ready = Input(Bool())  // Consumer ready to accept output
val out_valid = Output(Bool()) // Output data is valid
val out_s     = Output(UInt()) // Result
```

### Handshake Protocol
- A transaction occurs when both `valid` and `ready` are high on the same clock edge
- `in_ready` indicates the module can accept new data
- Backpressure propagates through module chains via the ready signals
- The pipeline stalls when `out_valid` is high but `out_ready` is low

### Example Usage
```scala
// Connect two modules in a chain
module_a.out_ready := module_b.in_ready
module_b.in_valid  := module_a.out_valid
module_b.in_a      := module_a.out_s
```

## Usage

### Prerequisites

- **Scala**: 2.13.12
- **SBT**: 1.7.2 or later
- **Chisel**: 6.0.0 (managed by SBT)
- **Verilator**: For simulation (optional)

### Generating Verilog

To generate SystemVerilog RTL for a specific module, modify `src/main/scala/Generate/generate.scala`:

```scala
object generate extends App {
  private def genVerilog(mod: => RawModule): Unit = {
    val gen: () => RawModule = () => mod
    (new ChiselStage).execute(
      Array("--target", "systemverilog"),
      Seq(ChiselGeneratorAnnotation(gen),
        FirtoolOption("--disable-all-randomization"),
        FirtoolOption("-strip-debug-info"),
        FirtoolOption("--disable-annotation-unknown")
      ),
    )
  }

  // Generate desired module
  // Import the formats first: import FloatingPoint.{FP32, FP64, BF16}
  genVerilog(new FP_exp(FP32))  // 32-bit exponential unit
}
```

Generated SystemVerilog files will be placed in the project root directory.

## Module Instantiation Examples

### Floating-Point Adder

```scala
import FloatingPoint._
import FloatingPoint.fpu._

// 32-bit adder with 7-stage pipeline
val adder = Module(new FP_add(FORMAT = FP32, latency = 7))
adder.io.out_ready := true.B
adder.io.in_valid := input_valid
adder.io.in_a := operand_a
adder.io.in_b := operand_b
val result = adder.io.out_s
val result_valid = adder.io.out_valid
```

### Floating-Point Divider
```scala
// 32-bit divider with 23 iterations and 23-cycle latency
val divider = Module(new FP_div(FORMAT = FP32, L = 23, latency = 23))
divider.io.out_ready := downstream_ready
divider.io.in_valid := input_valid
divider.io.in_a := dividend
divider.io.in_b := divisor
val quotient = divider.io.out_s
```

### CORDIC Cosine/Sine
```scala
// 32-bit cos/sin with 23 CORDIC iterations
val trig = Module(new FP_cos(FORMAT = FP32, iters = 23))
trig.io.out_ready := true.B
trig.io.in_valid := angle_valid
trig.io.in_angle := angle_ieee754
val cos_result = trig.io.out_cos
val sin_result = trig.io.out_sin
```

## Algorithm Details

### Division and Square Root
Both `FP_div` and `FP_sqrt` use **digit-recurrence algorithms**, computing one bit of the result per iteration. The `L` parameter controls the number of iterations (typically equal to mantissa width), while `latency` controls how iterations are distributed across pipeline stages.

### CORDIC (COordinate Rotation DIgital Computer)
Trigonometric and hyperbolic functions use the CORDIC algorithm, which computes results through iterative shift-and-add operations. The `ucordic` module supports three modes:
- **Circular mode** (mu = 1): cos, sin, atan
- **Linear mode** (mu = 0): multiplication, division
- **Hyperbolic mode** (mu = -1): sinh, cosh, atanh, exp, ln

Both the `cordic` and `ucordic` engines carry **internal fractional guard bits** (`G = ceil(log2(iters)) + 2`) throughout the iteration datapath. Inputs are up-shifted into the wider fixed-point domain on entry, every micro-rotation is computed at the extended precision, and the result is rounded-to-nearest back down to the requested `fbits` at the output. This prevents the per-iteration right-shift truncations from accumulating into the low result bits.

The angle / atanh / 2^-i constant tables and the CORDIC gain inverses are generated by the `hpmath` helper using `BigDecimal` arithmetic (~120 significant digits) and rounded-to-nearest, so the constants are accurate for every supported format up to FP128 — including the `atan(1)` table entry, which the naive Maclaurin series cannot resolve.

### Cosine / Sine Range Reduction
`FP_cos` reduces an arbitrary angle to `[0, 2*pi)` by computing `x / (2*pi)` (with a full-mantissa digit-recurrence divide), taking the fractional part via `FP_floor`, and multiplying back by `2*pi`. The reduced angle is then converted to a fixed-point format that maximizes fractional precision (a small number of integer bits for the `[0, 2*pi)` range, the rest fractional) before entering the circular CORDIC engine.

### Arctangent
`FP_atan` converts the input to fixed-point, runs a vectoring-mode CORDIC, and converts the resulting angle back to float. The fixed-point format is chosen to spend most of its width on fractional bits (`fbits ~ mantissa + headroom`) rather than splitting the width evenly — `atan(x)` saturates toward `+/-pi/2` for large `|x|`, so only a few integer bits are needed.

### Exponential (e^x)
`FP_exp` uses range reduction to decompose `x / ln(2) = w + f` into an integer part `w` and fractional part `f`. The fractional part is computed via a hyperbolic CORDIC engine (`e^(f * ln(2)) = 2^f`), while the integer part becomes an exponent bias adjustment.

#### Multiplierless CSD Constant Multiplication
Constant multiplications by `ln(2)` and `1/ln(2)` are implemented using **Canonical Signed Digit (CSD) encoding**, which decomposes fixed-point constants into a minimal set of shift-and-add/subtract operations — eliminating the need for hardware multipliers. The CSD algorithm guarantees no two consecutive non-zero digits, minimizing the number of additions/subtractions required. This is a significant area optimization for ASIC and FPGA targets, as it replaces wide multipliers with a small number of barrel shifts and adders.

The `ln(2)` and `1/ln(2)` constants are specified with over 300 decimal digits of precision, sufficient to support all standard formats through FP128 (112-bit mantissa) without loss of accuracy from constant truncation. The constants are rounded-to-nearest when scaled to fixed point, and the `>> scale` descale after each CSD multiply also rounds-to-nearest rather than truncating.

### Accumulation (Kulisch-style)
`FP_acc` is a **fixed-point exact accumulator** rather than a chain of floating-point adds. Each input is converted to a wide *signed two's-complement* fixed-point value and added into a running register that is sized to hold every in-range term — and the partial sums — without loss. Because the additions are exact, the result is independent of summation order and free of the catastrophic cancellation that plagues naive same-order floating-point summation. Rounding (RNE, with guard/round/sticky) happens exactly once, when the final sum is normalized back to the IEEE output.

The fixed-point geometry is derived from the requested dynamic range:
- `fracBits = (maxExp - minExp) + mantissa` — keeps the smallest supported term's full mantissa
- `headroom = ceil(log2(iters)) + 1` — carry-growth bits so summing `iters` terms cannot overflow
- the accumulator is `headroom + (mantissa+1) + fracBits + 1` bits (the `+1` is the sign)

Streaming control: assert `in_last` with the final term of a batch to emit the result and clear the register, or assert `clear` to discard the running sum. The fixed `iters` parameter still acts as a default flush length. `out_inexact` flags a rounded (or saturated) result, and `out_overflow` flags a result beyond the format's range.

### Precision and Rounding
Every lossy step in the datapath rounds-to-nearest (rather than truncating toward zero) and carries guard/round/sticky information so error does not accumulate or bias:

- **`FP_add`, `FP_mult`, `FP_div`, `FP_sqrt`** carry guard + round + sticky bits below the mantissa and apply IEEE **round-to-nearest-even (RNE)**. They are bit-exact correctly-rounded against a software FP64 reference.
- **`FloatTOFixed` / `FixedTOFloat`** round-to-nearest-even when aligning/extracting (with sticky capture), instead of truncating.
- **`cordic` / `ucordic`** carry internal fractional guard bits and round the result back to `fbits`.
- **`FP_acc`** accumulates exactly in fixed point and rounds-to-nearest-even once, at the final normalization.
- **Constant tables and gains** are generated at high precision (`hpmath`, BigDecimal) and rounded-to-nearest.

As a result the transcendental units (`FP_cos`, `FP_atan`, `FP_exp`) track the software (CPU) FP64 results to within a few ULP across the full input range; the underlying CORDIC cores reproduce software cos/sin to the limit of the fixed-point representation.

### Overflow/Underflow Handling
All modules implement saturation arithmetic:
- **Overflow**: Result saturates to maximum representable value
- **Underflow**: Result saturates to minimum normalized value

#### Input Saturation
All FPU modules clamp input exponents to the `[min_exp, max_exp]` range at the input stage, preventing undefined behavior for out-of-range or special values. `FP_exp` further narrows the accepted input range (capping at `bias + exponent - 2` and flooring at `bias - mantissa`) to avoid catastrophic overflow or underflow of the exponential output.

## Verification

Accuracy regression suites live in `src/test/scala/` and run under ScalaTest with the
Verilator backend. Each suite drives random inputs through the hardware and compares the
output against a software FP64 reference.

| Spec | Modules under test | Reports |
|------|--------------------|---------|
| `RoundingSpec` | `FP_add`, `FP_mult` | exact bit-pattern mismatches, off-by-1-ULP count, avg/max relative & absolute error |
| `DivSqrtSpec` | `FP_div`, `FP_sqrt` | exact bit-pattern mismatches, off-by-1-ULP count, avg/max relative & absolute error |
| `TranscendentalSpec` | `FP_cos`, `FP_atan`, `FP_exp` | avg/max relative & absolute error vs `Math.cos`/`sin`/`atan`/`exp` |
| `CordicCoreSpec` | `cordic`, `ucordic` | per-input cos/sin error for the raw CORDIC cores |
| `FP_accSpec` | `FP_acc` | avg/max relative error vs a BigDecimal exact sum, a small-vs-large stress case, and `clear` control |

The basic arithmetic specs (`RoundingSpec`, `DivSqrtSpec`) expect **zero** mismatches —
the hardware is bit-exact correctly-rounded against the FP64 reference. The transcendental
specs print average and maximum error so precision can be tracked over time. `FP_accSpec`
compares against a `BigDecimal` exact sum (rounded once to the format), confirming the
accumulator stays within ~1 ULP even for mixed large/small magnitude inputs.

Run all suites:

```bash
sbt test
```

Run a single suite:

```bash
sbt "testOnly TB.RoundingSpec"
sbt "testOnly TB.DivSqrtSpec"
sbt "testOnly TB.TranscendentalSpec"
sbt "testOnly TB.CordicCoreSpec"
sbt "testOnly TB.FP_accSpec"
```

## License

BSD 3-Clause License

Copyright (c) 2025, The Regents of the University of California, through Lawrence Berkeley National Laboratory and University of Houston-Clear Lake.

See [LICENSE](LICENSE) for full terms.

## Copyright Notice

Code Generators for Floating-Point Unit Design in Integrated Circuits (OpenFloat) Copyright (c) 2025, The Regents of the University of California,
through Lawrence Berkeley National Laboratory (subject to receipt of
any required approvals from the U.S. Dept. of Energy) and University of Houston-Clear Lake,  All rights reserved.

If you have questions about your rights to use or distribute this software,
please contact Berkeley Lab's Intellectual Property Office at
IPO@lbl.gov.

NOTICE.  This Software was developed under funding from the U.S. Department
of Energy and the U.S. Government consequently retains certain rights.  As
such, the U.S. Government has been granted for itself and others acting on
its behalf a paid-up, nonexclusive, irrevocable, worldwide license in the
Software to reproduce, distribute copies to the public, prepare derivative
works, and perform publicly and display publicly, and to permit others to do so.

This software was developed under funding from the U.S. Department of Energy.
