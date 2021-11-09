package com.jacoby6000.sbt


import java.io.File
import sbt.Keys._
import sbt._

import scala.xml._
import scala.sys.props

object SbtPomBuild extends AutoPlugin {
  val autoImport = SbtPomBuildKeys

  import autoImport._

  override val projectSettings: Seq[Setting[_]] = Seq(
    pomBuildPluginsIgnoreNames := Seq("^sbt-bloop$"),
    pomBuildPluginsIgnoreClasses := Seq(),
    pomBuildPluginsIgnoreDirs := {
      val sep = File.separatorChar
      Seq(file(props.getOrElse("sbt.boot.directory", props("user.home") ++ s"${sep}.sbt${sep}boot")), baseDirectory.value / "project")
    },
    generateBuildPluginsXml := {
      val extracted = Project.extract(state.value)
      val currentProject = extracted.currentRef
      PomBuildPlugins.buildPluginsXml(
        extracted.currentUnit.unit.plugins, 
        pomBuildPluginsIgnoreNames.value, 
        pomBuildPluginsIgnoreClasses.value, 
        pomBuildPluginsIgnoreDirs.value,
        streams.value.log
      )
    },
    makePom := {
      val log = streams.value.log
      log.info("Adding build plugins to pom XML")
      val createdPom = makePom.value
      val xml = XML.loadFile(createdPom)
      val updatedXml = 
        xml match {
          case e @ Elem(_, _, _, _, content @ _*) => 
            e.copy(child = content :+ <build>{generateBuildPluginsXml.value}</build>)
        }
      val formattedXml = new scala.xml.PrettyPrinter(80, 2).format(updatedXml)
      log.info("Writing updated XML to " + createdPom)
      sbt.io.IO.write(createdPom, formattedXml)
      createdPom
    }
  )
}

object SbtPomBuildKeys {
  val generateBuildPluginsXml = taskKey[NodeSeq]("The pom xml for the build section.")
  val pomBuildPluginsIgnoreNames = settingKey[Seq[String]]("Remove any build plugin names that match these patterns.")
  val pomBuildPluginsIgnoreClasses = settingKey[Seq[String]]("Remove any build plugin class names that match these patterns.")
  val pomBuildPluginsIgnoreDirs = settingKey[Seq[File]]("Remove any build plugins from these directories.")
}
