package Generate
import FloatingPoint.fpu._
import Primitives.primitives._
import chisel3._
import chisel3.stage.ChiselGeneratorAnnotation
import circt.stage.{ChiselStage, FirtoolOption}

object generate extends App {
  private def genVerilog(mod: => RawModule): Unit = {
    val gen : () => RawModule = () => mod

    (new ChiselStage).execute(
      Array("--target", "systemverilog"),
      Seq(ChiselGeneratorAnnotation(gen),
        FirtoolOption("--disable-all-randomization"),
        FirtoolOption("-strip-debug-info"),
        FirtoolOption("--disable-annotation-unknown")
      ),
    )
  }

  // generate verilog
  genVerilog(new FP_exp(32))
}
