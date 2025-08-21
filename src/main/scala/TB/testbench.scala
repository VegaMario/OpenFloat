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

  private def error(exp: Array[BigDecimal], obs: Array[BigDecimal]): Unit = {
    val error_arr = exp.zip(obs).map(x=>{
      ((x._2 - x._1).abs / x._1) * 100
    })
    val max_err = error_arr.max
    val avg_err = error_arr.sum / error_arr.length
    println(s"AVG Error Cos: ${avg_err}%")
    println(s"Largest Error Cos: ${max_err}%\n")
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

  class CORDIC_TEST extends AnyFlatSpec with ChiselScalatestTester{
    behavior of "CORDIC_TEST"
    it should "do something" in {
      val fbits = 45
      val bw = 48
      val vmode = false
      val n = 40

      val trials = 1E6.toInt

      val scale_f = BigDecimal(2).pow(fbits)
      val rand_in = (0 until trials).map(i=>genDouble(-2*Math.PI,2*Math.PI))
      val cos_out = rand_in.map(Math.cos)
      val sin_out = rand_in.map(Math.sin)

      val hw_cos_out = Array.fill(trials)(BigDecimal(0))
      val hw_sin_out = Array.fill(trials)(BigDecimal(0))
      val hw_clk = Array.fill(trials)(BigInt(0))

      val total_in = trials
      val total_out = trials

      var in_cnt = 0
      var out_cnt = 0
      var clk = 0

      test(new cordic(bw, vmode, fbits, n, n)).withAnnotations(Seq(VerilatorBackendAnnotation)){c=>
        c.io.in_en.poke(true.B)
        while(in_cnt < total_in){
          c.io.in_valid.poke(true.B)
          c.io.in_d.poke((rand_in(in_cnt) * scale_f).toBigInt)
          c.clock.step()
          c.io.in_valid.poke(false.B)
          clk += 1
          in_cnt += 1
          if(c.io.out_valid.peekBoolean()){
//            println(clk)
            hw_cos_out(out_cnt) = BigDecimal(c.io.out_x.peekInt()) / scale_f
            hw_sin_out(out_cnt) = BigDecimal(c.io.out_y.peekInt()) / scale_f
            hw_clk(out_cnt) = clk
            out_cnt += 1
          }
        }
        while(out_cnt < total_out){
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            hw_cos_out(out_cnt) = BigDecimal(c.io.out_x.peekInt()) / scale_f
            hw_sin_out(out_cnt) = BigDecimal(c.io.out_y.peekInt()) / scale_f
            hw_clk(out_cnt) = clk
            out_cnt += 1
          }
        }
        c.clock.step(10)
        val error_cos = cos_out.zip(hw_cos_out).map(x=>{
          ((x._2 - x._1).abs / x._1) * 100
        })
        val error_sin = sin_out.zip(hw_sin_out).map(x=>{
          ((x._2 - x._1).abs / x._1) * 100
        })

        val max_err_cos = error_cos.max
        val max_err_sin = error_sin.max
        val avg_err_cos = error_cos.sum / error_cos.length
        val avg_err_sin = error_sin.sum / error_sin.length
        println(s"AVG Error Cos: ${avg_err_cos}%")
        println(s"AVG Error Sin: ${avg_err_sin}%")
        println(s"Largest Error Cos: ${max_err_cos}%")
        println(s"Largest Error Sin: ${max_err_sin}%")
      }
    }
  }

  class UCORDIC_TEST extends AnyFlatSpec with ChiselScalatestTester{
    behavior of "UCORDIC_TEST"
    it should "do something" in {
      val fbits = 46
      val bw = 48
      val n = 64

      val trials = 1E3.toInt

      val scale_f = BigDecimal(2).pow(fbits)
      val rand_in = (0 until trials).map(i=>genDouble(-2,2))
      val rand_in2 = (0 until trials).map(i=>genDouble(-2,2))
      val out = rand_in.zip(rand_in2).map(x=>x._1 * x._2)

      val hw_out = Array.fill(trials)(BigDecimal(0))
      val hw_clk = Array.fill(trials)(BigInt(0))

      val total_in = trials
      val total_out = trials

      var in_cnt = 0
      var out_cnt = 0
      var clk = 0

      test(new ucordic(bw, fbits, n, n)).withAnnotations(Seq(VerilatorBackendAnnotation)){c=>
        c.io.in_en.poke(true.B)
        c.io.ctrl_vectoring.poke(false.B)
        c.io.ctrl_mode.poke(0.S)
        while(in_cnt < total_in){
          c.io.in_valid.poke(true.B)
          c.io.in_x.poke((rand_in(in_cnt) * scale_f).toBigInt)
          c.io.in_z.poke((rand_in2(in_cnt) * scale_f).toBigInt)
          c.clock.step()
          c.io.in_valid.poke(false.B)
          clk += 1
          in_cnt += 1
          if(c.io.out_valid.peekBoolean()){
            //            println(clk)
            hw_out(out_cnt) = BigDecimal(c.io.out_y.peekInt()) / scale_f
            hw_clk(out_cnt) = clk
            out_cnt += 1
          }
        }
        while(out_cnt < total_out){
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            hw_out(out_cnt) = BigDecimal(c.io.out_y.peekInt()) / scale_f
            hw_clk(out_cnt) = clk
            out_cnt += 1
          }
        }
        c.clock.step(10)
        val error = out.zip(hw_out).zip(rand_in).zip(rand_in2).map(x=>{
          println(s"${x._1._2} x ${x._2} = exp: ${x._1._1._1}, obs: ${x._1._1._2}")
          ((x._1._1._2 - x._1._1._1).abs / x._1._1._1) * 100
        })

        val max_err = error.max
        val avg_err = error.sum / error.length
        println(s"AVG Error: ${avg_err}%")
        println(s"Largest Error: ${max_err}%")
      }
    }
  }

  class UCORDIC_TEST2 extends AnyFlatSpec with ChiselScalatestTester{
    behavior of "UCORDIC_TEST2"
    it should "do something" in {
      val fbits = 46
      val bw = 48
      val n = 48

      val trials = 1E3.toInt

      val scale_f = BigDecimal(2).pow(fbits)
      val rand_in = (0 until trials).map(i=>genDouble(0.1,0.9))
      val rand_in2 = (0 until trials).map(i=>genDouble(-1,1))
      val out = rand_in.zip(rand_in2).map(x=>Math.exp(x._2))

      val K_inv = ((0.607252935009 * scale_f).toBigInt).asSInt((bw+1).W)
      val Kprime_inv = ((1.207497067763 * scale_f).toBigInt).asSInt((bw+1).W)
      val one = (BigDecimal(1.0) * scale_f).toBigInt.asSInt((bw+1).W)
      val zero = 0.S((bw+1).W)

      val hw_out = Array.fill(trials)(BigDecimal(0))
      val hw_clk = Array.fill(trials)(BigInt(0))

      val total_in = trials
      val total_out = trials

      var in_cnt = 0
      var out_cnt = 0
      var clk = 0

      test(new ucordic(bw, fbits, n, n)).withAnnotations(Seq(VerilatorBackendAnnotation, WriteFstAnnotation)){c=>
        c.io.in_en.poke(true.B)
        c.io.ctrl_vectoring.poke(false.B)
        c.io.ctrl_mode.poke(-1.S)
        while(in_cnt < total_in){
          c.io.in_valid.poke(true.B)
          c.io.in_x.poke(Kprime_inv)
          c.io.in_y.poke(Kprime_inv)
          c.io.in_z.poke((rand_in2(in_cnt) * scale_f).toBigInt)
//          c.io.in_z.poke((rand_in(in_cnt) * scale_f).toBigInt)
          c.clock.step()
          c.io.in_valid.poke(false.B)
          clk += 1
          in_cnt += 1
          if(c.io.out_valid.peekBoolean()){
            //            println(clk)
            hw_out(out_cnt) = BigDecimal(c.io.out_y.peekInt()) / scale_f
            hw_clk(out_cnt) = clk
            out_cnt += 1
          }
        }
        while(out_cnt < total_out){
          c.clock.step()
          clk += 1
          if(c.io.out_valid.peekBoolean()){
            hw_out(out_cnt) = BigDecimal(c.io.out_y.peekInt()) / scale_f
            hw_clk(out_cnt) = clk
            out_cnt += 1
          }
        }
        c.clock.step(10)
        val error = out.zip(hw_out).zip(rand_in).zip(rand_in2).map(x=>{
          println(s"Math.exp(${x._2}) = expected: ${x._1._1._1}, observed: ${x._1._1._2}")
          ((x._1._1._2 - x._1._1._1).abs / x._1._1._1) * 100
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
//  runTest(new SQRT_TEST)
//  runTest(new DIV_TEST)
  runTest(new UCORDIC_TEST2)
}
