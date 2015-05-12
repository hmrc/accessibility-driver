package uk.gov.hmrc.accessibility

import java.io.File

import org.apache.log4j.Logger

import scala.io.Source
import scala.sys.process._
import Filesystem._

object AccessibilityTool {
  
  val logger = Logger.getLogger(getClass)

  val tmpDir = System.getProperty("java.io.tmpdir")

  for(file <- Seq("audit.js", "axs_testing.js"))
    if( !new File(new File(tmpDir), file).exists() ) {
      withFileWriter(tmpDir, file) { writer =>
        val in = getClass.getResourceAsStream(s"/$file")
        val c = Source.fromInputStream(in).getLines.mkString("\n")
        writer.write(c)
      }
    }
  
  def runAccessibilityReport(htmlFile: File): String = {
    try {
      val auditJs = (new File(tmpDir, "audit.js")).getAbsolutePath
      val axsTestingJs = (new File(tmpDir, "axs_testing.js")).getAbsolutePath
      val htmlPath = htmlFile.getAbsolutePath      
      val str = Process(Seq("phantomjs", auditJs, axsTestingJs, htmlPath)).!!
      str
    }
    catch {
      case t: Throwable =>
        logger.error("Error while trying to run phantomjs", t)
        ""
    }
  }
}
