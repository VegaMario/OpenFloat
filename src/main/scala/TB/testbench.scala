package TB

import FloatingPoint.fpu._
import Primitives.primitives._
import Primitives.convert.{convert_IEEE754_to_Decimal, convert_string_to_IEEE_754}
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import java.math.MathContext
import scala.util.Random

object testbench extends App {
  private def genDouble(min: Double, max: Double): Double = {
    val random = new Random()
    val randomDoubleInRange = min + (max - min) * random.nextDouble()
    randomDoubleInRange
  }

  // tb examples
  class BasicTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "BasicTest"
    it should "do something" in {
      // simple test to see output of multiplier module
      // since fst is enabled, look for a test_run_dir directory to be created, it will have fst files when run
      test(new FP_add(32,7)).withAnnotations(Seq(VerilatorBackendAnnotation, WriteFstAnnotation)) {c=>
        c.io.in_en.poke(true.B)
        c.io.in_valid.poke(true.B)
        c.io.in_a.poke(convert_string_to_IEEE_754("12.2", 32))
        c.io.in_b.poke(convert_string_to_IEEE_754("41.6", 32))
        c.clock.step(7)
        println(s"output: ${convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, 32)}")
      }
    }
  }

  class SQRT_TEST extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "SQRT_TEST"
    it should "do something" in {
      val runs = 1E6.toInt

      val inputs = Array.fill(runs)(BigDecimal(genDouble(0,100000)))
      val expected_outputs = inputs.map(_.bigDecimal.sqrt(MathContext.DECIMAL32)).toArray
      val hardware_inputs = inputs.map(x=>convert_string_to_IEEE_754(x.bigDecimal.toPlainString, 32))
      val observed_output = Array.fill(runs)(BigDecimal(0))

      var cnt_out = 0
      var clk = 0

      test(new FP_sqrt(32, 23, 23)).withAnnotations(Seq(VerilatorBackendAnnotation)) {c=>
        c.clock.setTimeout(0)
        c.io.in_en.poke(true.B)
        c.io.in_valid.poke(true.B)
        for(i <- 0 until runs){
          c.io.in_a.poke(hardware_inputs(i))
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            observed_output(cnt_out) = convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, 32)
            cnt_out += 1
          }
        }

        c.io.in_valid.poke(false.B)

        while(cnt_out < runs){
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            observed_output(cnt_out) = convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, 32)
            cnt_out += 1
          }
        }

        val error = expected_outputs.zip(observed_output).map(x=>{
          ((x._2 - x._1).abs / x._1) * 100
        })

        val max_err = error.max
        val avg_err = error.sum / error.length
        println(s"AVG Error: ${avg_err}%")
        println(s"Largest Error: ${max_err}%")


      }
    }
  }

  class DIV_TEST extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "DIV_TEST"
    it should "do something" in {
      val runs = 1E6.toInt

      val inputs = Array.fill(runs)(BigDecimal(genDouble(0,100000)))
      val inputs2 = Array.fill(runs)(BigDecimal(genDouble(0,100000)))
      val expected_outputs = inputs.zip(inputs2).map(i=>i._1.bigDecimal.divide(i._2.bigDecimal, MathContext.DECIMAL32))
      val hardware_inputs = inputs.map(x=>convert_string_to_IEEE_754(x.bigDecimal.toPlainString, 32))
      val hardware_inputs2 = inputs2.map(x=>convert_string_to_IEEE_754(x.bigDecimal.toPlainString, 32))
      val observed_output = Array.fill(runs)(BigDecimal(0))

      var cnt_out = 0
      var clk = 0

      test(new FP_div(32, 23, 23)).withAnnotations(Seq(VerilatorBackendAnnotation)) {c=>
        c.clock.setTimeout(0)
        c.io.in_en.poke(true.B)
        c.io.in_valid.poke(true.B)
        for(i <- 0 until runs){
          c.io.in_a.poke(hardware_inputs(i))
          c.io.in_b.poke(hardware_inputs2(i))
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            observed_output(cnt_out) = convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, 32)
            cnt_out += 1
          }
        }

        c.io.in_valid.poke(false.B)

        while(cnt_out < runs){
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            observed_output(cnt_out) = convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, 32)
            cnt_out += 1
          }
        }

        val error = expected_outputs.zip(observed_output).map(x=>{
          ((x._2 - x._1).abs / x._1) * 100
        })

        val max_err = error.max
        val avg_err = error.sum / error.length
        println(s"AVG Error: ${avg_err}%")
        println(s"Largest Error: ${max_err}%")


      }
    }
  }

  private def runTest(tb: => AnyFlatSpec with ChiselScalatestTester): Unit = {
    tb.execute()
  }

  // run test
  runTest(new SQRT_TEST)
  runTest(new DIV_TEST)
}
