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

import java.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.openqa.selenium.WebDriver.{Navigation, Options, TargetLocator}
import org.openqa.selenium.firefox.{FirefoxProfile, FirefoxDriver}
import org.openqa.selenium.{JavascriptExecutor, By, WebDriver, WebElement}
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global


class AccessibilityDriver(val port: Int = 8080) extends WebDriver with JavascriptExecutor {

  val profile = new FirefoxProfile()
  profile.setPreference("network.proxy.type", 1)
  profile.setPreference("network.proxy.http", "localhost")
  profile.setPreference("network.proxy.http_port", port)
  profile.setPreference("network.proxy.no_proxies_on", "")
  val delegate = new FirefoxDriver(profile)
  
  val interceptedPages = new QueueStream

  //Start a thread with the proxy server running
  Future {
    //Run proxy server in main thread (blocking call)
    HttpProxyServerFactory.buildHtmlInterceptingProxy(port, interceptedPages.put).start()
  }

  val runTime = DateTime.now
  val runStamp = DateTimeFormat.forPattern("yyyyMMddHHmmss").print(runTime)

  //Run page interceptor in worker thread
  Future {
    //Consume from interceptedPages EOS
    val pages = for(page <- interceptedPages) yield {
      ReportWriter.createAccessibilityReport(runStamp, page)
      page
    }
    ReportWriter.createReportWrapper(pages.toSet, runStamp, runTime)
  }
  
  override def get(url: String): Unit = delegate.get(url)
  override def getPageSource: String = delegate.getPageSource
  override def findElements(by: By): util.List[WebElement] = delegate.findElements(by)
  override def getWindowHandle: String = delegate.getWindowHandle
  override def manage(): Options = delegate.manage()
  override def getWindowHandles: util.Set[String] = delegate.getWindowHandles
  override def switchTo(): TargetLocator = delegate.switchTo()
  override def close(): Unit = delegate.close()
  override def getCurrentUrl: String = delegate.getCurrentUrl
  override def navigate(): Navigation = delegate.navigate()
  override def getTitle: String = delegate.getTitle
  override def findElement(by: By): WebElement = delegate.findElement(by)

  override def quit(): Unit = {
    interceptedPages.put(StopMessage)
    Thread.sleep(2000)
    delegate.quit()
  }

  override def executeScript(script: String, args: AnyRef*): AnyRef = delegate.executeScript(script, args)
  override def executeAsyncScript(script: String, args: AnyRef*): AnyRef = delegate.executeAsyncScript(script, args)
}

