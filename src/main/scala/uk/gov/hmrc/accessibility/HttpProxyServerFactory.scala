package uk.gov.hmrc.accessibility

import org.littleshoot.proxy._
import org.littleshoot.proxy.impl.DefaultHttpProxyServer

/**
 * Created by nic on 29/04/2015.
 */
object HttpProxyServerFactory {
    
  def buildHtmlInterceptingProxy(port: Int, handler: InterceptedHtmlPageMessage => Unit): HttpProxyServerBootstrap = {
    DefaultHttpProxyServer.bootstrap()
      .withPort(port)
      .withFiltersSource(new HtmlInterceptingHttpFiltersSourceAdapter(handler))
  }
}
