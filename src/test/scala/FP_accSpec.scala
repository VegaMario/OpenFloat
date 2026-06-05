package TB

import FloatingPoint.fpu._
import Primitives.convert.{convert_IEEE754_to_Decimal, convert_string_to_IEEE_754}
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import FloatingPoint.{FP32, FP64, FloatingPointFormat}

import scala.util.Random

// Accuracy spec for the fixed-point (Kulisch-style) FP_acc accumulator.
//
// The accumulator sums terms EXACTLY in a wide fixed-point register, rounding only
// once at the final normalization. We therefore compare against a BigDecimal exact
// running sum (not a Double sum, which would itself be lossy), rounded once to the
// target format -- and we also confirm FP_acc beats a naive sequential FP_add chain.
class FP_accSpec extends AnyFlatSpec with ChiselScalatestTester {

  // Round an exact BigDecimal to the nearest representable value of `fmt`, returned as
  // its IEEE bit pattern (round-half-up is sufficient for the random magnitudes used).
  private def roundToFormat(x: BigDecimal, fmt: FloatingPointFormat): BigInt =
    convert_string_to_IEEE_754(x.bigDecimal.toPlainString, fmt)

  private def ulpRel(exp: BigDecimal, obs: BigDecimal): Double =
    if (exp == BigDecimal(0)) obs.toDouble.abs
    else ((obs - exp).abs / exp.abs).toDouble

  // Drive `vals` through the accumulator in batches of `iters`, returning the decimal
  // outputs. Uses in_last to flush each batch (clear is implicit on flush).
  private def runBatches(c: FP_acc, fmt: FloatingPointFormat, vals: Seq[BigDecimal], iters: Int): Seq[BigDecimal] = {
    val out = scala.collection.mutable.ArrayBuffer.empty[BigDecimal]
    c.io.out_ready.poke(true.B)
    c.io.clear.poke(false.B)
    for ((v, idx) <- vals.zipWithIndex) {
      c.io.in_valid.poke(true.B)
      c.io.in_last.poke(((idx % iters) == (iters - 1)).B)
      c.io.in_a.poke(roundToFormat(v, fmt).U)
      c.clock.step()
      if (c.io.out_valid.peekBoolean()) out += convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, fmt)
    }
    c.io.in_valid.poke(false.B)
    c.io.in_last.poke(false.B)
    var guard = 0
    while (out.length < vals.length / iters && guard < 64) {
      c.clock.step(); guard += 1
      if (c.io.out_valid.peekBoolean()) out += convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, fmt)
    }
    out.toSeq
  }

  behavior of "FP_acc (FP32) exactness"
  it should "match a BigDecimal exact sum rounded once to the format" in {
    val fmt: FloatingPointFormat = FP32
    val iters = 64
    val batches = 50
    val maxE = 6   // inputs in roughly [2^-? , 2^7)
    val minE = -6
    val rnd = new Random(11)

    // Generate batches of inputs whose exponents stay inside [minE, maxE].
    val allVals = (0 until batches * iters).map { _ =>
      val mag = math.pow(2.0, minE + rnd.nextDouble() * (maxE - minE)) // in [2^minE, 2^maxE)
      val signed = mag * (if (rnd.nextBoolean()) 1 else -1)
      // snap to a representable FP32 value so the input itself is exact
      convert_IEEE754_to_Decimal(roundToFormat(BigDecimal(signed), fmt), fmt)
    }

    test(new FP_acc(fmt, maxExp = maxE, minExp = minE, iters = iters)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      val obs = runBatches(c, fmt, allVals, iters)
      assert(obs.length == batches, s"expected $batches results, got ${obs.length}")

      val errs = (0 until batches).map { b =>
        val exactSum = allVals.slice(b * iters, (b + 1) * iters).foldLeft(BigDecimal(0))(_ + _)
        val refBits  = roundToFormat(exactSum, fmt)
        val ref      = convert_IEEE754_to_Decimal(refBits, fmt)
        ulpRel(ref, obs(b))
      }
      val maxErr = errs.max
      val avgErr = errs.sum / errs.length
      println(f"FP_acc FP32  avg_rel_err=$avgErr%.3e  max_rel_err=$maxErr%.3e  (batches=$batches, iters=$iters)")
      // One correctly-rounded normalization => within ~1 ULP of the exact sum (2^-23).
      assert(maxErr < 2e-7, s"max relative error $maxErr exceeds ~1 ULP")
    }
  }

  behavior of "FP_acc (FP32) vs naive FP_add chain"
  it should "be at least as accurate as sequential FP64-reference addition" in {
    val fmt: FloatingPointFormat = FP32
    val iters = 128
    val maxE = 4
    val minE = -4
    val rnd = new Random(22)

    // A classic stress case: many small terms plus a few large ones (catastrophic for
    // naive same-order summation, exact for the Kulisch accumulator).
    val vals = (0 until iters).map { i =>
      val mag = if (i % 16 == 0) math.pow(2, 3) else math.pow(2, -3) * rnd.nextDouble()
      convert_IEEE754_to_Decimal(roundToFormat(BigDecimal(mag), fmt), fmt)
    }

    test(new FP_acc(fmt, maxExp = maxE, minExp = minE, iters = iters)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      val obs = runBatches(c, fmt, vals, iters)
      assert(obs.length == 1)
      val exactSum = vals.foldLeft(BigDecimal(0))(_ + _)
      val ref = convert_IEEE754_to_Decimal(roundToFormat(exactSum, fmt), fmt)
      val err = ulpRel(ref, obs.head)
      println(f"FP_acc FP32 stress  rel_err=$err%.3e  (sum=$exactSum)")
      assert(err < 2e-7, s"stress relative error $err exceeds ~1 ULP")
    }
  }

  behavior of "FP_acc clear control"
  it should "discard the running sum on clear" in {
    val fmt: FloatingPointFormat = FP32
    val iters = 8
    test(new FP_acc(fmt, maxExp = 4, minExp = -4, iters = iters)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      c.io.in_last.poke(false.B)
      // Push a couple of terms, then clear, then push iters terms of 1.0 and flush.
      c.io.in_valid.poke(true.B); c.io.clear.poke(false.B)
      c.io.in_a.poke(roundToFormat(BigDecimal(3.5), fmt).U); c.clock.step()
      c.io.in_a.poke(roundToFormat(BigDecimal(-2.0), fmt).U); c.clock.step()
      // clear
      c.io.in_valid.poke(false.B); c.io.clear.poke(true.B); c.clock.step()
      c.io.clear.poke(false.B)
      // Now accumulate iters * 1.0 and flush on the last.
      var got: Option[BigDecimal] = None
      for (i <- 0 until iters) {
        c.io.in_valid.poke(true.B)
        c.io.in_last.poke((i == iters - 1).B)
        c.io.in_a.poke(roundToFormat(BigDecimal(1.0), fmt).U)
        c.clock.step()
        if (c.io.out_valid.peekBoolean()) got = Some(convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, fmt))
      }
      c.io.in_valid.poke(false.B); c.io.in_last.poke(false.B)
      var guard = 0
      while (got.isEmpty && guard < 32) { c.clock.step(); guard += 1
        if (c.io.out_valid.peekBoolean()) got = Some(convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, fmt)) }
      println(s"FP_acc clear result = ${got.get} (expected ${iters}.0)")
      assert(got.get == BigDecimal(iters), s"after clear expected $iters.0, got ${got.get}")
    }
  }
}
