/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.hmrc.accessibility

import org.joda.time.DateTime
import Filesystem._

object ReportWriter {

  val targetDir = "target/accessibility-reports/"
  
  def createAccessibilityReport(runStamp: String, interceptedHtmlPage: InterceptedHtmlPageMessage): Unit = {

    val htmlFile = withFileWriter(targetDir+runStamp, interceptedHtmlPage.hash+"-page.html") { writer =>
      writer.write(interceptedHtmlPage.body)
    }

    val output = AccessibilityTool.runAccessibilityReport(htmlFile)

    val reportFile = withFileWriter(targetDir+runStamp, interceptedHtmlPage.hash+"-report.html") { writer =>
      writer.write {
        s"""<pre>$output</pre>"""
      }
    }

    val framesFile = withFileWriter(targetDir+runStamp, interceptedHtmlPage.hash+"-frames.html") { writer =>
      writer.write {
        s"""<frameset rows="50%,50%">
          |  <frame src="${interceptedHtmlPage.hash}-page.html" name="page">
          |  <frame src="${interceptedHtmlPage.hash}-report.html" name="report">
          |</frameset>""".stripMargin
      }
    }
  }
  
  
  def createReportWrapper(pages: Set[InterceptedHtmlPageMessage], runStamp: String, runTime: DateTime): Unit = {
    
    val head =
      s"""<html>
        |  <head>
        |    <title>Accessibility Report - $runTime</title>
        |  </head>
        |  <body>
        |    <h1>Accessibility Report - $runTime</h1>
        |    <hr>
        |    <ul>""".stripMargin
    
    val body = pages map { p =>
      s"""<li><a href="${p.hash}-frames.html" target="report">${p.uri}</a></li>"""
    } mkString("\n")
    
    val foot =
      """    </ul>
        |  </body>
        |</html>""".stripMargin
    
    withFileWriter(targetDir+runStamp, "nav.html") { writer =>
      writer.write( Seq(head, body, foot).mkString("\n") )
    }
    
    val frames =
      """<frameset cols="25%,*">
        |  <frame src="nav.html">
        |  <frame name="report">
        |</frameset>""".stripMargin

    withFileWriter(targetDir+runStamp, "report.html") { writer =>
      writer.write( frames )
    }
  }
}
