import sbt.Keys._
import sbt._
import com.disneystreaming.sdp.{ SDPProject, SDPProjectKeys }
import com.jacoby6000.sbt.SbtPomBuild

object ProjectPlugin extends AutoPlugin {
  override def requires = SbtPomBuild
  override def trigger = allRequirements
  override val buildSettings: Seq[Setting[_]] = Seq()
  override val projectSettings: Seq[Setting[_]] = Seq()
}
