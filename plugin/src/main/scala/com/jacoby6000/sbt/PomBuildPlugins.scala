package com.jacoby6000.sbt

import sbt.internal.LoadedPlugins
import scala.xml._
import scala.sys.props

object PomBuildPlugins {

  def buildPluginsXml(plugins: LoadedPlugins, ignorePlugins: Set[String]) = {
    val pluginReport = plugins.pluginData.report.get
    val detectedAutoPlugins = plugins.detected.autoPlugins

    val poms = 
      detectedAutoPlugins.map(plugin => 
        Class.forName(plugin.name)
          .getProtectionDomain()
          .getCodeSource()
          .getLocation()
          .getFile()
        )
        .filterNot(_.contains(props.getOrElse("sbt.boot.directory", props("user.home") ++ "/.sbt/boot")))
        .distinct
        .filter(_.endsWith(".jar"))
        .map(_.dropRight(4) ++ ".pom")
    
    println(poms.mkString("\n"))
    
    val pluginsXml = poms.flatMap { pom =>
      val xml = XML.loadFile(pom) \\ "project"
      val artifactId = (xml \ "artifactId").text
      
      if(ignorePlugins.contains(artifactId))
        None
      else {
        val groupId = (xml \ "groupId").text
        val version = (xml \ "version").text
        Some(<plugin><groupId>{groupId}</groupId><artifactId>{artifactId}</artifactId><version>{version}</version></plugin>)
      }
    }

    <plugins>{pluginsXml}</plugins>
  }
          
}
