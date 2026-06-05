package TB

import FloatingPoint.fpu._
import Primitives.convert.{convert_IEEE754_to_Decimal, convert_string_to_IEEE_754}
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import FloatingPoint.{FP32, FP64, FloatingPointFormat}

import scala.util.Random

// Accuracy regression specs for the transcendental FP units (cos/sin, atan, exp).
// We compare the hardware decimal output against the software FP64 reference and
// report max / average relative error so precision improvements are measurable.
class TranscendentalSpec extends AnyFlatSpec with ChiselScalatestTester {

  private def relErr(exp: Double, obs: BigDecimal): Double = {
    if (exp == 0.0) (obs.toDouble - exp).abs
    else ((obs.toDouble - exp).abs / exp.abs)
  }

  private def summarize(name: String, errs: Seq[Double]): Unit = {
    val maxE = errs.max
    val avgE = errs.sum / errs.length
    println(f"$name%-12s avg_rel_err=${avgE}%.3e  max_rel_err=${maxE}%.3e  (n=${errs.length})")
  }

  // Absolute-error summary (relative error is misleading near function zeros, e.g.
  // cos near +/-pi/2 where the true value -> 0).
  private def absErr(exp: Double, obs: BigDecimal): Double = (obs.toDouble - exp).abs
  private def summarizeAbs(name: String, errs: Seq[Double]): Unit = {
    val maxE = errs.max
    val avgE = errs.sum / errs.length
    println(f"$name%-12s avg_abs_err=${avgE}%.3e  max_abs_err=${maxE}%.3e  (n=${errs.length})")
  }

  behavior of "FP_cos accuracy"
  it should "track software cos/sin" in {
    val fmt: FloatingPointFormat = FP32
    val iters = fmt.mantissa + 8
    val rnd = new Random(7)
    val n = 2000
    test(new FP_cos(fmt, iters)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      c.io.in_valid.poke(true.B)
      val angles = Array.fill(n)((rnd.nextDouble() * 2 - 1) * math.Pi) // [-pi, pi]
      val cosE = new Array[BigDecimal](n); val sinE = new Array[BigDecimal](n); var outc = 0
      for (i <- 0 until n) {
        c.io.in_angle.poke(convert_string_to_IEEE_754(angles(i).toString, fmt).U)
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) {
          cosE(outc) = convert_IEEE754_to_Decimal(c.io.out_cos.peek().litValue, fmt)
          sinE(outc) = convert_IEEE754_to_Decimal(c.io.out_sin.peek().litValue, fmt)
          outc += 1
        }
      }
      c.io.in_valid.poke(false.B)
      while (outc < n) {
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) {
          cosE(outc) = convert_IEEE754_to_Decimal(c.io.out_cos.peek().litValue, fmt)
          sinE(outc) = convert_IEEE754_to_Decimal(c.io.out_sin.peek().litValue, fmt)
          outc += 1
        }
      }
      summarize("cos", (0 until n).map(i => relErr(math.cos(angles(i)), cosE(i))))
      summarize("sin", (0 until n).map(i => relErr(math.sin(angles(i)), sinE(i))))
      summarizeAbs("cos", (0 until n).map(i => absErr(math.cos(angles(i)), cosE(i))))
      summarizeAbs("sin", (0 until n).map(i => absErr(math.sin(angles(i)), sinE(i))))
    }
  }

  behavior of "FP_atan accuracy"
  it should "track software atan" in {
    val fmt: FloatingPointFormat = FP32
    val iters = fmt.mantissa + 4
    val rnd = new Random(8)
    val n = 2000
    test(new FP_atan(fmt, iters)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      c.io.in_valid.poke(true.B)
      val xs = Array.fill(n)((rnd.nextDouble() * 2 - 1) * 4.0) // [-4, 4]
      val res = new Array[BigDecimal](n); var outc = 0
      for (i <- 0 until n) {
        c.io.in_tan.poke(convert_string_to_IEEE_754(xs(i).toString, fmt).U)
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) { res(outc) = convert_IEEE754_to_Decimal(c.io.out_atan.peek().litValue, fmt); outc += 1 }
      }
      c.io.in_valid.poke(false.B)
      while (outc < n) {
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) { res(outc) = convert_IEEE754_to_Decimal(c.io.out_atan.peek().litValue, fmt); outc += 1 }
      }
      summarize("atan", (0 until n).map(i => relErr(math.atan(xs(i)), res(i))))
      summarizeAbs("atan", (0 until n).map(i => absErr(math.atan(xs(i)), res(i))))
    }
  }

  behavior of "FP_exp accuracy"
  it should "track software exp" in {
    val fmt: FloatingPointFormat = FP32
    val rnd = new Random(9)
    val n = 2000
    test(new FP_exp(fmt)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      c.io.in_valid.poke(true.B)
      val xs = Array.fill(n)((rnd.nextDouble() * 2 - 1) * 5.0) // [-5, 5]
      val res = new Array[BigDecimal](n); var outc = 0
      for (i <- 0 until n) {
        c.io.in_data.poke(convert_string_to_IEEE_754(xs(i).toString, fmt).U)
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) { res(outc) = convert_IEEE754_to_Decimal(c.io.out_data.peek().litValue, fmt); outc += 1 }
      }
      c.io.in_valid.poke(false.B)
      while (outc < n) {
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) { res(outc) = convert_IEEE754_to_Decimal(c.io.out_data.peek().litValue, fmt); outc += 1 }
      }
      summarize("exp", (0 until n).map(i => relErr(math.exp(xs(i)), res(i))))
    }
  }
}
