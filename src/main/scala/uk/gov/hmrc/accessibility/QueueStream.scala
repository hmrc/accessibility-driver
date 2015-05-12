package uk.gov.hmrc.accessibility

import java.util.concurrent.LinkedBlockingQueue

class QueueStream extends Iterable[InterceptedHtmlPageMessage] {
  val queue = new LinkedBlockingQueue[Message]
  def put(o: Message) = queue.put(o)
  override def iterator = stream.iterator

  def stream: Stream[InterceptedHtmlPageMessage] = queue.take match {
    case p: InterceptedHtmlPageMessage => p #:: stream
    case StopMessage => Stream.empty
  }
}
