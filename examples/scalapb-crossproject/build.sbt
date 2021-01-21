import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import scalapb.compiler.Version.scalapbVersion

val sharedSettings = Seq(
  name         := "example",
  version      := "0.1.0",
  scalaVersion := "2.11.12",
  PB.protoSources in Compile := Seq((baseDirectory in ThisBuild).value / "src"/ "main" / "protobuf"),
  PB.targets in Compile := Seq(
    scalapb.gen() -> (sourceManaged in Compile).value / "protos",
  ),
  libraryDependencies += "com.thesamet.scalapb" %%% "scalapb-runtime" % scalapbVersion,
  scalaJSUseMainModuleInitializer := true,
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := "hello"
)

lazy val example = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(sharedSettings)
  .jsSettings(crossScalaVersions := Seq("2.12.13", "2.13.4"))
  .jvmSettings(crossScalaVersions := Seq("2.12.13", "2.13.4"))
  .nativeSettings(crossScalaVersions := Seq("2.11.12"))

lazy val exampleJS = example.js
lazy val exampleJVM = example.jvm
lazy val exampleNative = example.native


// The following is needed since otherwise SBT would create a default root project rooted in `.`
// It will try compiling sources under `src/main/scala`, but without access to the generated code.
// To prevent that, we explictly set the source directory to something inexistent...
lazy val root = project.in(file("."))
  .aggregate(exampleJS, exampleJVM, exampleNative)
  .settings(
    sourceDirectory := file("ignore"),
    publish := {},
  )
