package com.jacoby6000.sbt

import sbt.Keys._
import sbt._

import scala.xml._

object SbtPomBuildPlugins extends AutoPlugin {
  val autoImport = SbtPomBuildPluginsKeys

  import autoImport._

  override val projectSettings: Seq[Setting[_]] = Seq(
    pomIgnoreBuildPlugins := Set("sbt-bloop"),
    pomPluginsXml := {
      val extracted = Project.extract(state.value)
      val currentProject = extracted.currentRef
      PomBuildPlugins.buildPluginsXml(extracted.currentUnit.unit.plugins, pomIgnoreBuildPlugins.value)
    },
    makePom := {
      val createdPom = makePom.value
      val xml = XML.loadFile(createdPom)
      val updatedXml = 
        xml match {
          case e @ Elem(_, _, _, _, content @ _*) => 
            e.copy(child = content :+ <build>{pomPluginsXml.value}</build>)
        }
      val formattedXml = new scala.xml.PrettyPrinter(80, 2).format(updatedXml)
      sbt.io.IO.write(createdPom, formattedXml)
      createdPom
    }
  )

}

object SbtPomBuildPluginsKeys {
  val pomPluginsXml = taskKey[NodeSeq]("The pom xml for the build section.")
  val pomIgnoreBuildPlugins = settingKey[Set[String]]("Remove any build plugins containing these names.")
}
