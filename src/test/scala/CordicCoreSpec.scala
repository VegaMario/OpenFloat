package TB

import Primitives.primitives._
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

// Isolate the cordic rotation core to localize the cos/sin error.
class CordicCoreSpec extends AnyFlatSpec with ChiselScalatestTester {

  behavior of "raw ucordic circular rotation"
  it should "rotate (Kinv,0) by z to give (cos,sin)" in {
    val bw = 64
    val fbits = 58
    val n = 110
    val scale = BigDecimal(2).pow(fbits)
    val Kinv = (Primitives.primitives.hpmath.circ_gain_inv(n) * scale).setScale(0, BigDecimal.RoundingMode.HALF_UP).toBigInt
    val angles = Seq(0.0, 0.3, 0.7, 1.0)
    test(new ucordic(bw, fbits, n, n)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      c.io.ctrl_vectoring.poke(false.B)
      c.io.ctrl_mode.poke(1.S)
      for (a <- angles) {
        c.io.in_valid.poke(true.B)
        c.io.in_x.poke(Kinv.S)
        c.io.in_y.poke(0.S)
        c.io.in_z.poke((BigDecimal(a) * scale).toBigInt.S)
        c.clock.step()
        c.io.in_valid.poke(false.B)
        var got = false
        while (!got) {
          if (c.io.out_valid.peekBoolean()) {
            val cosv = BigDecimal(c.io.out_x.peek().litValue) / scale
            val sinv = BigDecimal(c.io.out_y.peek().litValue) / scale
            val zv = BigDecimal(c.io.out_z.peek().litValue) / scale
            println(f"  raw($a%+.3f) cos=${cosv.toDouble}%+.10f (abs=${(cosv.toDouble-math.cos(a)).abs}%.2e) sin abs=${(sinv.toDouble-math.sin(a)).abs}%.2e z_resid=${zv.toDouble}%.2e")
            got = true
          } else c.clock.step()
        }
      }
    }
  }

  behavior of "cordic core cos/sin"
  it should "match software cos/sin for in-range angles" in {
    val bw = 64
    val fbits = 58
    val n = 60
    val scale = BigDecimal(2).pow(fbits)
    val angles = Seq(0.0, 0.3, -0.3, 0.7, -0.7, 1.0, -1.0, 1.5, -1.5)
    test(new cordic(bw, false, fbits, n, n)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      for (a <- angles) {
        c.io.in_valid.poke(true.B)
        c.io.in_d.poke((BigDecimal(a) * scale).toBigInt.S)
        c.clock.step()
        c.io.in_valid.poke(false.B)
        var got = false
        while (!got) {
          if (c.io.out_valid.peekBoolean()) {
            val cosv = BigDecimal(c.io.out_x.peek().litValue) / scale
            val sinv = BigDecimal(c.io.out_y.peek().litValue) / scale
            println(f"  cos($a%+.3f) hw=${cosv.toDouble}%+.10f sw=${math.cos(a)}%+.10f abs=${(cosv.toDouble-math.cos(a)).abs}%.2e | sin abs=${(sinv.toDouble-math.sin(a)).abs}%.2e")
            got = true
          } else c.clock.step()
        }
      }
    }
  }

  behavior of "ucordic-based cos primitive"
  it should "match software cos/sin for in-range angles" in {
    val bw = 64
    val fbits = 58
    val n = 60
    val scale = BigDecimal(2).pow(fbits)
    val angles = Seq(0.0, 0.3, 0.7, 1.0, 1.5)
    test(new cos(bw, fbits, n, n)).withAnnotations(Seq(VerilatorBackendAnnotation)) { c =>
      c.io.out_ready.poke(true.B)
      for (a <- angles) {
        c.io.in_valid.poke(true.B)
        c.io.in_angle.poke((BigDecimal(a) * scale).toBigInt.S)
        c.clock.step()
        c.io.in_valid.poke(false.B)
        var got = false
        while (!got) {
          if (c.io.out_valid.peekBoolean()) {
            val cosv = BigDecimal(c.io.out_cos.peek().litValue) / scale
            val sinv = BigDecimal(c.io.out_sin.peek().litValue) / scale
            println(f"  ucos($a%+.3f) hw=${cosv.toDouble}%+.10f sw=${math.cos(a)}%+.10f abs=${(cosv.toDouble-math.cos(a)).abs}%.2e | sin abs=${(sinv.toDouble-math.sin(a)).abs}%.2e")
            got = true
          } else c.clock.step()
        }
      }
    }
  }
}
