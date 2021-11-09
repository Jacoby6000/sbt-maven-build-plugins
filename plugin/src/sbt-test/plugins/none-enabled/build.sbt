import java.util.UUID
import scala.sys.process.Process
import scala.xml._

/**
Enable the SbtPomBuild plugin and ensure it is placed in to the plugins xml
*/
lazy val simple = project.in(file("."))
  .settings(
    logLevel := Level.Debug,
    TaskKey[Unit]("verifyPomBuild") := {
      val pomFile = makePom.value
      val xml = XML.loadFile(pomFile.getCanonicalPath())
      val plugins = xml \\ "project" \ "build" \ "plugins" \ "plugin"

      if(plugins.length != 0) 
        sys.error("Expected 0 plugin. Got " + plugins.length + "\n" + xml)
    }
  )
