package com.sfz.stream.engine

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import org.apache.flink.configuration.{Configuration, RestOptions}


class DataProcessEngine {

  val CONNECTOR_KAFKA: String = "kafka"
  val TEMPORARY_SOURCE_NAME = "source"
  val TEMPORARY_SINK_NAME = "sink"

  var LAUNCHER_ENVIRONMENT = "local"
  var connectorSourceInfo: String = _
  var connectorSinkInfo: String = _
  var schemaSourceInfo: String = _
  var schemaSinkInfo: String = _
  var sqlQuery: String = _
  var tableSourceSQL: String = _
  var tableSinkSQL: String = _


  def startStreamDataProcess(args: Arguments): Unit = {

    //Get StreamTableEnvironment
    val streamTableEnvironment = initStreamTableEnvironment()


    initTableSQL(args)
    executeFlinkSql(streamTableEnvironment, tableSourceSQL, tableSinkSQL)


  }

  def initStreamTableEnvironment(): StreamTableEnvironment = {
    var executionEnvironment:StreamExecutionEnvironment = null
    //TODO Here,set the incoming LAUNCHER_ENVIRONMENT in the future
    if(LAUNCHER_ENVIRONMENT=="local") {
      //launcher web ui
      val conf = new Configuration
      conf.setString(RestOptions.BIND_PORT, "8081")
       executionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)
    }else {
       executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    }
    val streamSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    StreamTableEnvironment.create(executionEnvironment, streamSettings)


  }


  def executeFlinkSql(streamTableEnvironment: StreamTableEnvironment, sourceTableSQL: String, sinkTableSQL: String): Unit = {


    streamTableEnvironment.executeSql(sourceTableSQL)
    streamTableEnvironment.executeSql(sinkTableSQL)

    //Execute sql query
    val res = streamTableEnvironment.sqlQuery(sqlQuery)

    //Output result
    res.executeInsert(TEMPORARY_SINK_NAME)


  }


  def initTableSQL(args: Arguments): Unit = {
    loadArguments(args)
    tableSourceSQL = generateTableSQL(TEMPORARY_SOURCE_NAME, schemaSourceInfo, connectorSourceInfo)
    tableSinkSQL = generateTableSQL(TEMPORARY_SINK_NAME, schemaSinkInfo, connectorSinkInfo)
  }


  def loadArguments(args: Arguments): Unit = {
    schemaSourceInfo = generateTableSchemaInfo(args.sourceSchemaInfo)
    schemaSinkInfo = generateTableSchemaInfo(args.sinkSchemaInfo)

    if (args.sourceConnector == CONNECTOR_KAFKA) {
      connectorSourceInfo =
        """'connector' = 'kafka',
          |'topic' = '""" + args.sourceTopic +
          """',
            |'properties.bootstrap.servers' = '""" + args.sourceKafkaServers +
          """',
            |'properties.group.id' = '""" + args.sourceGroupId +
          """',
            |'format' = 'json',
            |'scan.startup.mode' = 'latest-offset',
            |'json.fail-on-missing-field' = 'false',
            |'json.ignore-parse-errors' = 'true'"""
    }
    if (args.sinkConnector == CONNECTOR_KAFKA) {
      connectorSinkInfo =
        """'connector' = 'kafka',
          |'topic' = '""" + args.sinkTopic +
          """',
            |'properties.bootstrap.servers' = '""" + args.sinkKafkaServers +
          """',
            |'format' = 'json',
            |'sink.partitioner' = 'round-robin'
            |"""
    }
    sqlQuery = args.sql
  }

  def generateTableSQL(table: String, schemaInfo: String, connectorInfo: String): String = {
    val tableSQL = "create table " + table + "(" + schemaInfo + ") with(" + connectorInfo + ")"
    tableSQL.stripMargin
  }

  def generateTableSchemaInfo(schemaInfo: String): String = {
    val schemaJsonInfo = JSON.parseObject(schemaInfo, Feature.OrderedField)
    val schemaIt = schemaJsonInfo.entrySet().iterator()
    var tableSchemaInfo = ""
    while (schemaIt.hasNext) {
      val entry = schemaIt.next()
      tableSchemaInfo += ", " + entry.getKey + " " + entry.getValue
    }
    tableSchemaInfo = tableSchemaInfo.substring(2)
    tableSchemaInfo
  }


}
