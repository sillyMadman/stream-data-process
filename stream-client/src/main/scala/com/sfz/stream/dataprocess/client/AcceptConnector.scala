package com.sfz.stream.dataprocess.client

import com.sfz.stream.Logging


object AcceptConnector extends Enumeration with Logging {
  type AcceptConnector = Value
  val Kafka = Value("kafka")

  def checkConnector(connector: String): Boolean = this.values.exists(_.toString == connector)


}
