import java.util.UUID
import scala.sys.process.Process
import scala.xml._

/**
Enable the SbtPomBuild plugin and ensure it is placed in to the plugins xml
*/
lazy val projectPlugin = project.in(file("."))
  .enablePlugins(SbtPomBuild)
  .settings(
    pomBuildPluginsIgnoreDirs += file(System.getProperty("java.io.tmpdir")), // scripted tests load boot from tmp
    pomBuildPluginsIgnoreClasses += "^[^.]+$",
    logLevel := Level.Debug,
    extraAppenders := {
      val errorLog = ErrorLog.Instance
      val currentFunction = extraAppenders.value
      (key: ScopedKey[_]) => errorLog +: currentFunction(key)
    },
    TaskKey[Unit]("verifyNoFailure") := {
      makePom.value
      val errors = ErrorLog.Instance.errors
      if(errors.nonEmpty) {
        sys.error("Got Errors\n"+errors.reverse.mkString("\n"))
      }
    }
  )
