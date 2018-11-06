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

import java.io.{File, FileWriter}

import org.apache.commons.io.FileUtils

object Filesystem {

  def withFileWriter(directory: String, filename: String)(block: FileWriter => Unit): File = {
    val dir = new File(directory)
    FileUtils.forceMkdir(dir)
    val outFile = new File(dir, filename)
    withFileWriter(outFile)(block)
  }

  def withFileWriter(outFile: File)(block: FileWriter => Unit): File = {
    val out = new FileWriter(outFile)
    block(out)
    out.flush()
    out.close()
    outFile
  }
}
