import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.PublishingSettings._
import uk.gov.hmrc.{ShellPrompt, SbtBuildInfo}

object HmrcBuild extends Build {
  import de.heikoseeberger.sbtheader.AutomateHeaderPlugin
  import uk.gov.hmrc.DefaultBuildSettings._
  import uk.gov.hmrc.PublishingSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}

  val nameApp = "accessibility-driver"
  val versionApp = "1.2.0"

  val appDependencies = Seq(
    "org.seleniumhq.selenium" % "selenium-java" % "2.45.0",
    "org.seleniumhq.selenium" % "selenium-firefox-driver" % "2.45.0",
    "org.littleshoot" % "littleproxy" % "1.0.0-beta8",
    "joda-time" % "joda-time" % "2.7",
    "org.joda" % "joda-convert" % "1.7",
    "commons-codec" % "commons-codec" % "1.10",
    "commons-io" % "commons-io" % "2.4",
    "org.jsoup" % "jsoup" % "1.8.2",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )

  lazy val playBreadcrumb = Project(nameApp, file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(version := versionApp)
    .settings(scalaSettings : _*)
    .settings(defaultSettings(false) : _*)
    .settings(
      targetJvm := "jvm-1.7",
      shellPrompt := ShellPrompt(versionApp),
      libraryDependencies ++= appDependencies,
      scalaVersion := "2.11.5",
      organization := "uk.gov.hmrc",
      crossScalaVersions := Seq("2.10.4", "2.11.5"),
      resolvers := Seq(
        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/",
        "typesafe-snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
      )
    )
    .settings(publishAllArtefacts: _*)
    .settings(SbtBuildInfo(): _*)
    .settings(POMMetadata(): _*)
    .settings(HeaderSettings())
}

object POMMetadata {
  def apply() = {
    pomExtra :=
      <url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          </license>
        </licenses>
        <scm>
          <connection>scm:git@github.com:hmrc/accessibility-driver.git</connection>
          <developerConnection>scm:git@github.com:hmrc/accessibility-driver.git</developerConnection>
          <url>git@github.com:hmrc/accessibility-driver.git</url>
        </scm>
        <developers>
          <developer>
            <id>nicfellows</id>
            <name>Nic Fellows</name>
            <url>http://www.nicshouse.co.uk</url>
          </developer>
        </developers>
  }
}

object HeaderSettings {
  import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._
  import de.heikoseeberger.sbtheader.license.Apache2_0

  def apply() = headers := Map("scala" -> Apache2_0("2015", "HM Revenue & Customs"))
}
