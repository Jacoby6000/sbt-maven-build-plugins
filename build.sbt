lazy val plugin = project
  .in(file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-pom-build",
    organization := "com.jacoby6000.sbt",
    publishMavenStyle := true,
    scalaVersion := "2.12.15",
    //crossScalaVersions := Seq("2.12.15", "2.13.7"),
    scriptedLaunchOpts ++= {
    val res = scriptedLaunchOpts.value ++ Seq(
      "-Xmx1024M",
      "-Dplugin.version=" + version.value
    )
    println(res)
    res
    },
    scriptedBufferLog := false
  )

lazy val root = project
  .in(file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(plugin)
