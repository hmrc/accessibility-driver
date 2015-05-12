package uk.gov.hmrc.accessibility

sealed trait Message
case object StopMessage extends Message
case class InterceptedHtmlPageMessage(
  uri: String,
  method: String,
  contentType: String,
  body: String,
  hash: String
) extends Message
