package uk.gov.hmrc

import java.io.File

import org.joda.time.DateTime
import uk.gov.hmrc.accessibility.Filesystem._

import scala.io.{Source, BufferedSource}

/**
 * Created by nic on 13/05/2015.
 */
package object accessibility {

  lazy val tmpDir = System.getProperty("java.io.tmpdir")
  val tmpMinsTTL = 10
  
  def resourceAsBufferedSource(resource: String): BufferedSource = {
    val in = getClass.getResourceAsStream(resource)
    Source.fromInputStream(in)
  }

  
  def maybeWriteResourceToTmp(resource: String): File = {
    val outFile = new File(new File(tmpDir), resource)
    if( new DateTime(outFile.lastModified()).plusMinutes(tmpMinsTTL).isBefore(DateTime.now) ) {
      withFileWriter(outFile) { writer =>
        val c = resourceAsBufferedSource(s"/$resource").mkString
        writer.write(c)
      }
    }
    outFile
  }
}
