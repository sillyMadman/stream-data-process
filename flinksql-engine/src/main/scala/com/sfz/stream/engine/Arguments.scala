package com.sfz.stream.engine


abstract class Arguments {
  var sourceConnector: String = _
  var sinkConnector: String = _
  //kafka conf
  var kafkaVersion: String = _
  var sourceKafkaServers: String = _
  var sinkKafkaServers: String = _
  var sourceTopic: String = _
  var sinkTopic: String = _
  var sourceGroupId: String = _
  var sourceSchemaInfo: String = _
  var sinkSchemaInfo: String = _

  var sql: String = _

}
