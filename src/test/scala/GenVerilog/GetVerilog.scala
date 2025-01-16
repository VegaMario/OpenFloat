package GenVerilog
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import chiseltest.WriteVcdAnnotation
import chiseltest.VerilatorBackendAnnotation
import chisel3.stage.ChiselGeneratorAnnotation
import circt.stage.{ChiselStage, FirtoolOption}
import FP_Modules.FPUnits._
import FP_Modules.FormatConvert._
import Binary_Modules.BinaryDesigns._

import java.io.PrintWriter
import scala.util.Random
object GetVerilog {
  def genDouble(min: Double, max: Double): Double = {
    val random = new Random()
    val randomDoubleInRange = min + (max - min) * random.nextDouble()
    randomDoubleInRange
  }

  class BasicTest extends AnyFlatSpec with ChiselScalatestTester {
    behavior of "DUT"
    it should "do something" in {
      // simple test to see output of multiplier module
      // since vcd is enabled, look for a test_run_dir directory to be created, it will have vcd files when run
      test(new FP_add(32,7)).withAnnotations(Seq(VerilatorBackendAnnotation, WriteVcdAnnotation)) {c=>
        c.io.in_en.poke(true.B)
        c.io.in_valid.poke(true.B)
        c.io.in_a.poke(convert_string_to_IEEE_754("12.2", 32))
        c.io.in_b.poke(convert_string_to_IEEE_754("41.6", 32))
        c.clock.step(7)
        println(s"output: ${convert_IEEE754_to_Decimal(c.io.out_s.peek().litValue, 32)}")
      }
    }
  }


  def main(args:Array[String]): Unit = {
    // execute test
    val t = new BasicTest()
    t.execute()

    // generate verilog
    (new ChiselStage).execute(
      Array("--target", "systemverilog"),
      Seq(ChiselGeneratorAnnotation(() => new FP_add(32,7))
        ,
        FirtoolOption("--disable-all-randomization"),
        FirtoolOption("-strip-debug-info")
      ),
    )

  }
}
