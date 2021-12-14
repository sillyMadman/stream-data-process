package com.sfz.stream.dataprocess.client

import com.sfz.stream.Logging
import com.sfz.stream.engine.Arguments
import org.apache.commons.cli.{CommandLine, DefaultParser, Options, UnrecognizedOptionException}


class SubmitArguments(args: Array[String]) extends Arguments with Logging {


  logInfo("start parseCommandLine")
  val parseCommandLine: CommandLine = getParseCommandLine(args)
  checkConnector(parseCommandLine)
  initArguments(parseCommandLine)
  logInfo(toString)


  def initArguments(parseCommandLine: CommandLine): Unit = {
    for (i <- parseCommandLine.getOptions.indices) {
      val optionArg = parseCommandLine.getOptions()(i).getLongOpt
      optionArg match {
        case "sourceConnector" => sourceConnector = parseCommandLine.getOptionValue("sourceConnector")
        case "sinkConnector" => sinkConnector = parseCommandLine.getOptionValue("sinkConnector")
        case "kafkaVersion" => kafkaVersion = parseCommandLine.getOptionValue("kafkaVersion")
        case "sourceKafkaServers" => sourceKafkaServers = parseCommandLine.getOptionValue("sourceKafkaServers")
        case "sinkKafkaServers" => sinkKafkaServers = parseCommandLine.getOptionValue("sinkKafkaServers")
        case "sourceTopic" => sourceTopic = parseCommandLine.getOptionValue("sourceTopic")
        case "sinkTopic" => sinkTopic = parseCommandLine.getOptionValue("sinkTopic")
        case "sourceGroupId" => sourceGroupId = parseCommandLine.getOptionValue("sourceGroupId")
        case "sourceSchemaInfo" => sourceSchemaInfo = parseCommandLine.getOptionValue("sourceSchemaInfo")
        case "sinkSchemaInfo" => sinkSchemaInfo = parseCommandLine.getOptionValue("sinkSchemaInfo")
        case "sql" => sql = parseCommandLine.getOptionValue("sql")
        case notMatchArg => logWarning(notMatchArg + "not match arg")


      }
    }

  }

  def getParseCommandLine(args: Array[String]): CommandLine = {
    val options = new Options()
    options.addOption("c1", "sourceConnector", true, "inputConnector")
    options.addOption("c2", "sinkConnector", true, "outputConnector")
    options.addOption("kv", "kafkaVersion", true, "kafkaVersion")
    options.addOption("s1", "sourceKafkaServers", true, "sourceKafkaServers")
    options.addOption("s2", "sinkKafkaServers", true, "sinkKafkaServers")
    options.addOption("t1", "sourceTopic", true, "sourceTopic")
    options.addOption("t2", "sinkTopic", true, "sinkTopic")
    options.addOption("g", "sourceGroupId", true, "sourceGroupId")
    options.addOption("ss1", "sourceSchemaInfo", true, "sourceSchemaInfo")
    options.addOption("ss2", "sinkSchemaInfo", true, "sinkSchemaInfo")
    options.addOption("e", "sql", true, "sql")

    val parser = new DefaultParser()
    var parseCommandLine: CommandLine = null
    try {
      parseCommandLine = parser.parse(options, args)
    } catch {
      case ex: UnrecognizedOptionException =>
        logError("args: " + ex.getOption + " is unrecognized.")
        logError("Invalid from this arguments")
        parseCommandLine = parser.parse(options, args, true)
    }

    parseCommandLine
  }

  def checkConnector(parseCommandLine: CommandLine): Unit = {
    if (!(parseCommandLine.hasOption("sourceConnector") && parseCommandLine.hasOption("sinkConnector"))) {
      logError("args need -sourceConnector <arg> -sourceConnector <arg> or -c1 <arg> -c2 <arg>")
      System.exit(1)
    }
    sourceConnector = parseCommandLine.getOptionValue("sourceConnector")
    sinkConnector = parseCommandLine.getOptionValue("sinkConnector")
    if (!AcceptConnector.checkConnector(sourceConnector)) {
      logError("not support sourceConnector: " + parseCommandLine.getOptionValue("sourceConnector"))
      System.exit(1)
    } else if (!AcceptConnector.checkConnector(sinkConnector)) {
      logError("not support sinkConnector: " + parseCommandLine.getOptionValue("sinkConnector"))
      System.exit(1)
    }

  }


  override def toString = s"SubmitArguments($sourceConnector,$sinkConnector,$kafkaVersion,$sourceKafkaServers, $sinkKafkaServers, $sourceTopic, $sinkTopic, $sourceGroupId, $sourceSchemaInfo, $sinkSchemaInfo, $sql)"
}

