# OpenFloat

**Code Generators for Floating-Point Unit Design in Integrated Circuits**

OpenFloat is a parameterized floating-point unit (FPU) design generator developed using [Chisel](https://www.chisel-lang.org/), a hardware construction language embedded in Scala. It generates synthesizable RTL for IEEE 754 compliant floating-point arithmetic operations targeting FPGA and ASIC implementations.

## Overview

OpenFloat provides a library of highly configurable floating-point arithmetic modules that support multiple IEEE 754 precision formats. The design emphasizes:

## Project Structure

```
OpenFloat/
├── src/main/scala/
│   ├── FloatingPoint/          # Core FPU modules
│   │   ├── FloatingPointFormat.scala # Supported Floating Point Formats
│   │   └── fpu.scala           # All floating-point operation implementations
│   ├── Primitives/             # Low-level building blocks
│   │   ├── primitives.scala    # Arithmetic primitives (adders, dividers, CORDIC, etc.)
│   │   └── convert.scala       # IEEE 754 format conversion utilities
│   ├── Generate/               # RTL generation utilities
│   │   └── generate.scala      # SystemVerilog output generator
│   └── TB/                     # Test benches
│       └── testbench.scala     # Verification test suites
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

### Basic Arithmetic Operations

| Module | Description | Parameters |
|--------|-------------|------------|
| `FP_add` | Floating-point addition | `FORMAT`: FloatingPointFormat, `pd`: pipeline depth (1, 3, 7, 10, 11, 13) |
| `FP_mult` | Floating-point multiplication | `FORMAT`: FloatingPointFormat, `pd`: pipeline depth (1, 3, 7, 8, 10, 13) |
| `FP_div` | Digit-recurrence division | `FORMAT`: FloatingPointFormat, `L`: iterations, `latency`: pipeline stages |
| `FP_sqrt` | Digit-recurrence square root | `FORMAT`: FloatingPointFormat, `L`: iterations, `latency`: pipeline stages |

### Transcendental Functions

| Module | Description | Parameters |
|--------|-------------|------------|
| `FP_cos` | Cosine and Sine (CORDIC-based) | `FORMAT`: FloatingPointFormat, `iters`: CORDIC iterations |
| `FP_atan` | Arctangent (CORDIC-based) | `FORMAT`: FloatingPointFormat, `iters`: CORDIC iterations |
| `FP_exp` | Exponential function (e^x) | `FORMAT`: FloatingPointFormat |

### Utility Modules

| Module | Description | Parameters |
|--------|-------------|------------|
| `FP_acc` | Floating-point accumulator | `FORMAT`: FloatingPointFormat, `iters`: accumulation count, `ExpExp`, `ExpMSB`, `LSB` |
| `FP_floor` | Floor function | `FORMAT`: FloatingPointFormat |
| `FloatTOFixed` | Float to fixed-point conversion | `FORMAT`: FloatingPointFormat, `ibits`: integer bits, `fbits`: fractional bits |
| `FixedTOFloat` | Fixed-point to float conversion | `FORMAT`: FloatingPointFormat, `ibits`: integer bits, `fbits`: fractional bits |

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
| `cordic` | Fixed-point CORDIC processor (rotation/vectoring modes) |
| `ucordic` | Universal CORDIC (circular, linear, hyperbolic modes) |
| `cos`, `atan`, `exp` | Fixed-point trigonometric/exponential wrappers |

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
  genVerilog(new FP_mult(FP32, 7))  // 32-bit multiplier with 7-stage pipeline
  genVerilog(new FP_div(FP64, ...))  // 64-bit divider
}
```

Generated SystemVerilog files will be placed in the project root directory.

## Module Instantiation Examples

### Floating-Point Adder
```scala
import FloatingPoint._
import FloatingPoint.fpu._

// 32-bit adder with 7-stage pipeline
val adder = Module(new FP_add(FORMAT = FP32, pd = 7))
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
Both `FP_div` and `FP_sqrt` use **digit-recurrence algorithms**, computing one bit of the result per iteration. The `L` parameter controls the number of iterations (typically equal to mantissa width + 1), while `latency` controls how iterations are distributed across pipeline stages.

### CORDIC (COordinate Rotation DIgital Computer)
Trigonometric and hyperbolic functions use the CORDIC algorithm, which computes results through iterative shift-and-add operations. The `ucordic` module supports three modes:
- **Circular mode** (mu = 1): cos, sin, atan
- **Linear mode** (mu = 0): multiplication, division
- **Hyperbolic mode** (mu = -1): sinh, cosh, atanh, exp, ln

### Overflow/Underflow Handling
All modules implement saturation arithmetic:
- **Overflow**: Result saturates to maximum representable value
- **Underflow**: Result saturates to minimum normalized value

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
