package com.sfz.stream.dataprocess.client

import com.sfz.stream.Logging
import com.sfz.stream.engine.DataProcessEngine


object StreamDataProcessCli extends Logging {


  def main(args: Array[String]): Unit = {

    logInfo("Launcher stream-data-process ")
    logInfo("start parse command line")
    val arguments = parseArgumentLine(args)

    getDataProcessEngine.startStreamDataProcess(arguments)

  }


  def parseArgumentLine(args: Array[String]): SubmitArguments = {

    new SubmitArguments(args: Array[String])
  }

  def getDataProcessEngine: DataProcessEngine = {
    new DataProcessEngine
  }

}
