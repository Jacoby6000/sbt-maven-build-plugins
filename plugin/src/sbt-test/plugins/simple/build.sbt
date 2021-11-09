import java.util.UUID
import scala.sys.process.Process
import scala.xml._

/**
Enable the SbtPomBuild plugin and ensure it is placed in to the plugins xml
*/
lazy val simple = project.in(file("."))
  .enablePlugins(SbtPomBuild)
  .settings(
    pomBuildPluginsIgnoreDirs += file(System.getProperty("java.io.tmpdir")), // scripted tests load boot from tmp
    logLevel := Level.Debug,
    TaskKey[Unit]("verifyPomBuild") := {
      val pomFile = makePom.value
      val xml = XML.loadFile(pomFile.getCanonicalPath())
      println(xml)
      val plugins = xml \\ "project" \ "build" \ "plugins" \ "plugin"

      if(plugins.length != 1) 
        sys.error("Expected 1 plugin. Got " + plugins.length)

      val plugin = plugins.head

      val groupId = (plugin \ "groupId").text
      val artifactId = (plugin \ "artifactId").text
      val version = (plugin \ "version").text

      val identifier = s"$groupId:$artifactId:$version"
      val expectedIdentifier = "com.github.jacoby6000:sbt-pom-build:" + sys.props("plugin.version")

      if(identifier != expectedIdentifier)
        sys.error(s"Wrong plugin in xml.  Got $identifier, expected $expectedIdentifier")
      
    }
  )
