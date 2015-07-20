/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  
  def maybeWriteResourceToTmp(resource: String, force: Boolean = false): File = {
    val outFile = new File(new File(tmpDir), resource)
    if( new DateTime(outFile.lastModified()).plusMinutes(tmpMinsTTL).isBefore(DateTime.now) || force ) {
      withFileWriter(outFile) { writer =>
        val c = resourceAsBufferedSource(s"/$resource").mkString
        writer.write(c)
      }
    }
    outFile
  }
}
