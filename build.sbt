// -- new config
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"
val chiselVersion = "6.0.0"
lazy val root = (project in file("."))
  .settings(
    name := "ChiselFP",
    addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full),
    libraryDependencies += "org.chipsalliance" %% "chisel" % chiselVersion,
    libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "6.0.0",
    assembly / mainClass := Some("GenVerilog.GetVerilog"),
    assembly / assemblyOutputPath := file("./chisel_fpgen.jar")
  )


assemblyMergeStrategy in assembly := {
    case "module-info.class" =>
        // Discard module-info.class files that can cause conflicts
        MergeStrategy.discard
    case PathList("META-INF", xs @ _*) =>
        // Handle some common META-INF files:
        xs.map(_.toLowerCase) match {
            case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
                MergeStrategy.discard
            case ("plexus" :: _) =>
                MergeStrategy.first
            case ("services" :: xs) =>
                MergeStrategy.filterDistinctLines
            case _ =>
                MergeStrategy.first
        }
    case _ =>
        MergeStrategy.first
}