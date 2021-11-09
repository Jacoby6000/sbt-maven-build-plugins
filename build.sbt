lazy val plugin = project
  .in(file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    organization := "com.github.jacoby6000",
    name := "sbt-pom-build",
    publishMavenStyle := true,
    scalaVersion := "2.12.15",
    //crossScalaVersions := Seq("2.12.15", "2.13.7"),
    scriptedLaunchOpts ++= scriptedLaunchOpts.value ++ Seq(
      "-Xmx1024M",
      "-Dplugin.version=" + version.value
    ),
    scriptedBufferLog := false,
    ThisBuild / scmInfo := Some(ScmInfo(
      url("https://github.com/jacoby6000/sbt-pom-build"), 
      "scm:git@github.com:jacoby6000/sbt-pom-build.git"
    )),
    ThisBuild / developers := List(Developer(id="Jacoby6000", name="Jacob Barber", email="jacoby6000@gmail.com", url=url("https://github.com/jacoby6000"))),
    ThisBuild / description := "An sbt plugin for adding build plugin dependencies to pom files",
    ThisBuild / publishTo := sonatypePublishToBundle.value,
    ThisBuild / homepage := Some(url("https://github.com/jacoby6000")),
    ThisBuild / licenses := Seq("GPL V3" -> url("https://github.com/Jacoby6000/sbt-pom-build/blob/main/LICENSE")),
    usePgpKeyHex("3387004BD9DE85CC273C7E167DCF7CDCC3B06D98")
  )

lazy val root = project
  .in(file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(plugin)
