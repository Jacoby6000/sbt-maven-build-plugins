lazy val plugin = project
  .in(file("plugin"))
  .settings(name := "sbt-maven-build-plugins")
  .enablePlugins(SbtPlugin)


lazy val root = project
  .in(file("."))
  .settings(publish / skip := true)
  .aggregate(plugin)
