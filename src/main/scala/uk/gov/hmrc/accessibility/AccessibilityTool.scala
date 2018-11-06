/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.accessibility

import java.io.File

import org.apache.log4j.Logger

import scala.io.{BufferedSource, Source}
import scala.sys.process._
import Filesystem._

object AccessibilityTool {
  
  val logger = Logger.getLogger(getClass)

  for(resource <- Seq("audit.js", "axs_testing.js"))
    maybeWriteResourceToTmp(resource, force = true)

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
