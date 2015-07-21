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

package uk.gov.hmrc.accessibility

import org.scalatest._

/**
 * Created by prasadsriramula on 21/07/15.
 */
class HtmlInterceptingHttpFiltersSourceAdapterSpec  extends WordSpec with ShouldMatchers{
  /*

    def isInWhiteList(uri: String) = whiteList.exists( _.findAllIn(uri).nonEmpty )

  def shouldIntercept(contentType: String) = contentType.toLowerCase.indexOf("text/html") >= 0
   */

  "Calling HtmlInterceptingHttpFiltersSourceAdapter isInWhiteList" should {

    "return true if passed URI matches any white list entry " in {
      val whiteList = List("^https://github.com/hmrc(.*)".r)
      val ad = new HtmlInterceptingHttpFiltersSourceAdapter((x: InterceptedHtmlPageMessage) => Unit, whiteList)
      ad.isInWhiteList("https://github.com/hmrc/accessibility-driver") shouldBe true
    }

    "return false if passed URI doesn't match any white list entry" in {
      val whiteList = List("^https://github.com/hmrc(.*)".r)
      val ad = new HtmlInterceptingHttpFiltersSourceAdapter((x: InterceptedHtmlPageMessage) => Unit, whiteList)
      ad.isInWhiteList("http://www.bbc.com") shouldBe false
    }

    "return true if white list is empty" in {
      val whiteList = List()
      val ad = new HtmlInterceptingHttpFiltersSourceAdapter((x: InterceptedHtmlPageMessage) => Unit, whiteList)
      ad.isInWhiteList("http://www.bbc.com") shouldBe true
    }

  }

  "Calling HtmlInterceptingHttpFiltersSourceAdapter shouldIntercept" should{
    val whiteList = List("^https://github.com/hmrc(.*)".r)
    val ad = new HtmlInterceptingHttpFiltersSourceAdapter((x: InterceptedHtmlPageMessage) => Unit, whiteList)
    "return true if content type is correct" in {
      ad.shouldIntercept("text/html") shouldBe true
    }

    "return false if content type is incorrect" in {
      ad.shouldIntercept("text/plain") shouldBe false
    }
  }


}
