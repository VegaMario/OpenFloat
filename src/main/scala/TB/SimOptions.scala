package TB

import chiseltest._
import firrtl2.annotations.Annotation

// Shared chiseltest backend options, so every spec drives the same simulator setup.
object SimOptions {

  // Requires Verilator <= 5.048; see the Prerequisites section of the README.
  val verilator: Seq[Annotation] = Seq(
    VerilatorBackendAnnotation
  )
}
