package com.sfz.stream

import org.slf4j.{Logger, LoggerFactory}


trait Logging {

   private var log: Logger = _

  protected def logName: String = {
    this.getClass.getName.stripSuffix("$")
  }

  protected def getLogger: Logger = {
    if (log == null) {
      log = LoggerFactory.getLogger(logName)
    }
    log
  }

  protected def logInfo(msg: Any): Unit = {
    getLogger.info(msg.toString)
  }

  protected def logInfo(msg: => String): Unit = {
    getLogger.info(msg)
  }

  protected def logDebug(msg: => String): Unit = {
    getLogger.debug(msg)
  }

  protected def logTrace(msg: => String): Unit = {
    getLogger.trace(msg)
  }

  protected def logWarning(msg: => String): Unit = {
    getLogger.warn(msg)
  }

  protected def logError(msg: => String): Unit = {
    getLogger.error(msg)
  }

  protected def logInfo(msg: => String, throwable: Throwable): Unit = {
    getLogger.info(msg, throwable)
  }

  protected def logDebug(msg: => String, throwable: Throwable): Unit = {
    getLogger.debug(msg, throwable)
  }

  protected def logTrace(msg: => String, throwable: Throwable): Unit = {
    getLogger.trace(msg, throwable)
  }

  protected def logWarning(msg: => String, throwable: Throwable): Unit = {
    getLogger.warn(msg, throwable)
  }

  protected def logError(msg: => String, throwable: Throwable): Unit = {
    getLogger.error(msg, throwable)
  }
}
