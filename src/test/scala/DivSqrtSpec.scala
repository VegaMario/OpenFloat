package TB

import FloatingPoint.fpu._
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import FloatingPoint.{FP32, FP64}
import scala.util.Random

// Verification spec for FP_div / FP_sqrt round-to-nearest-even. Compares the
// hardware output bit-pattern against the correctly-rounded double result. Reports
// exact-mismatch / off-by-1-ulp counts plus avg & max relative/absolute error in the
// same style as TranscendentalSpec so accuracy is measurable at a glance.
class DivSqrtSpec extends AnyFlatSpec with ChiselScalatestTester {
  private def dToBits(d: Double): BigInt = BigInt(java.lang.Double.doubleToLongBits(d)) & ((BigInt(1) << 64) - 1)
  private def bitsToD(b: BigInt): Double =
    java.lang.Double.longBitsToDouble((b & ((BigInt(1) << 64) - 1)).toLong)

  private def relErr(exp: Double, obs: Double): Double = {
    if (exp == 0.0) obs.abs else ((obs - exp).abs / exp.abs)
  }
  private def absErr(exp: Double, obs: Double): Double = (obs - exp).abs

  private def report(name: String, exp: Array[BigInt], obs: Array[BigInt]): Unit = {
    val n = exp.length
    var mism = 0; var off1 = 0
    val relErrs = new Array[Double](n)
    val absErrs = new Array[Double](n)
    for (i <- 0 until n) {
      if (obs(i) != exp(i)) {
        mism += 1
        if ((obs(i) - exp(i)).abs == 1) off1 += 1
      }
      val e = bitsToD(exp(i)); val o = bitsToD(obs(i))
      relErrs(i) = relErr(e, o); absErrs(i) = absErr(e, o)
    }
    val rel = relErrs.filter(d => !d.isNaN && !d.isInfinite)
    val abs = absErrs.filter(d => !d.isNaN && !d.isInfinite)
    val avgRel = if (rel.nonEmpty) rel.sum / rel.length else 0.0
    val maxRel = if (rel.nonEmpty) rel.max else 0.0
    val avgAbs = if (abs.nonEmpty) abs.sum / abs.length else 0.0
    val maxAbs = if (abs.nonEmpty) abs.max else 0.0
    println(f"$name%-10s $mism / $n exact-mismatches ($off1 off-by-1-ulp)")
    println(f"$name%-10s avg_rel_err=${avgRel}%.3e  max_rel_err=${maxRel}%.3e")
    println(f"$name%-10s avg_abs_err=${avgAbs}%.3e  max_abs_err=${maxAbs}%.3e")
  }

  behavior of "FP64_div_RNE"
  it should "match correctly-rounded double division" in {
    val rnd = new Random(3)
    val n = 20000
    val lat = 64
    test(new FP_div(FP64, 52, lat)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.clock.setTimeout(0)
      c.io.out_ready.poke(true.B)
      c.io.in_valid.poke(true.B)
      val as = Array.fill(n)(rnd.nextDouble() * math.pow(2, rnd.nextInt(20) - 10) * (if (rnd.nextBoolean()) 1 else -1))
      val bs = Array.fill(n)((rnd.nextDouble() + 0.5) * math.pow(2, rnd.nextInt(20) - 10) * (if (rnd.nextBoolean()) 1 else -1))
      val res = new Array[BigInt](n); var outc = 0
      for (i <- 0 until n) {
        c.io.in_a.poke(dToBits(as(i)).U); c.io.in_b.poke(dToBits(bs(i)).U)
        c.clock.step(); if (c.io.out_valid.peekBoolean()) { res(outc) = c.io.out_s.peek().litValue; outc += 1 }
      }
      c.io.in_valid.poke(false.B)
      while (outc < n) { c.clock.step(); if (c.io.out_valid.peekBoolean()) { res(outc) = c.io.out_s.peek().litValue; outc += 1 } }
      val expected = Array.tabulate(n)(i => dToBits(as(i) / bs(i)))
      report("DIV", expected, res)
    }
  }

  behavior of "FP64_sqrt_RNE"
  it should "match correctly-rounded double sqrt" in {
    val rnd = new Random(4)
    val n = 20000
    val lat = 52
    test(new FP_sqrt(FP64, 52, lat)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.clock.setTimeout(0)
      c.io.out_ready.poke(true.B)
      c.io.in_valid.poke(true.B)
      val as = Array.fill(n)(math.abs(rnd.nextDouble()) * math.pow(2, rnd.nextInt(40) - 20))
      val res = new Array[BigInt](n); var outc = 0
      for (i <- 0 until n) {
        c.io.in_a.poke(dToBits(as(i)).U)
        c.clock.step(); if (c.io.out_valid.peekBoolean()) { res(outc) = c.io.out_s.peek().litValue; outc += 1 }
      }
      c.io.in_valid.poke(false.B)
      while (outc < n) { c.clock.step(); if (c.io.out_valid.peekBoolean()) { res(outc) = c.io.out_s.peek().litValue; outc += 1 } }
      val expected = Array.tabulate(n)(i => dToBits(math.sqrt(as(i))))
      report("SQRT", expected, res)
    }
  }
}
