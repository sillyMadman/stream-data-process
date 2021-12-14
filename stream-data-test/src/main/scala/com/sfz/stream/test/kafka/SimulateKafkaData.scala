package com.sfz.stream.test.kafka

import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.Duration
import java.util
import java.util.{Date, Properties}

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}


object SimulateKafkaData {

  var KAFKA_SOURCE_SERVERS = "localhost:9092"
  var KAFKA_SINK_SERVERS = "localhost:9092"

  def initConfig(): Unit = {

    val prop = new Properties()
    prop.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream("config.properties"), "UTF-8"))
    KAFKA_SOURCE_SERVERS = prop.getProperty("kafka.source.servers")
    KAFKA_SINK_SERVERS = prop.getProperty("kafka.sink.servers")
    println("KAFKA_SOURCE_SERVERS:" + KAFKA_SOURCE_SERVERS)
    println("KAFKA_SINK_SERVERS:" + KAFKA_SINK_SERVERS)

  }

  def main(args: Array[String]): Unit = {

    initConfig()

    createThreadAndStart(startKafkaDataProducer())

    createThreadAndStart(startKafkaDataConsumer())


  }


  /**
   * Used to get test data from Kafka
   */
  def startKafkaDataConsumer(): Unit = {


    val prop = new Properties
    prop.put("bootstrap.servers", KAFKA_SINK_SERVERS)
    prop.put("key.deserializer", classOf[StringDeserializer].getName)
    prop.put("value.deserializer", classOf[StringDeserializer].getName)
    prop.put("group.id", "test")
    prop.put("enable.auto.commit", "true")
    prop.put("auto.commit.interval.ms", "5000")
    prop.put("auto.offset.reset", "earliest")


    val consumer = new KafkaConsumer[String, String](prop)
    val topics = new util.ArrayList[String]
    topics.add("streamdata-sink-test")
    consumer.subscribe(topics)

    while (true) {

      val poll = consumer.poll(Duration.ofSeconds(2))

      println("=============" + new Date())
      val it = poll.iterator()
      while (it.hasNext) {
        val msg = it.next()
        println(s"partition: ${msg.partition()}, offset: ${msg.offset()}, key: ${msg.key()}, value: ${msg.value()}")
      }
      consumer.commitSync()

    }


  }

  /**
   * Used to send test data to Kafka
   */
  def startKafkaDataProducer(): Unit = {

    val prop = new Properties

    prop.put("bootstrap.servers", KAFKA_SOURCE_SERVERS)
    prop.put("key.serializer", classOf[StringSerializer].getName)
    prop.put("value.serializer", classOf[StringSerializer].getName)

    val producer = new KafkaProducer[String, String](prop)

    val topic = "streamdata-source-test"

    while (true) {
      for (age <- 0 to 100) {
        val eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)
        val message = "{\"age\":" + age + ",\"name\":\"sillyMadman\",\"eventTime\":\"" + eventTime + "\"}"

        producer.send(new ProducerRecord[String, String](topic, message))
        Thread.sleep(100)
      }

    }

    producer.close()
  }

  /**
   * Create and start a child thread.
   *
   * @param  fun : Pass the function to be called.
   * @return return object of thread.
   */

  def createThreadAndStart(fun: => Unit): Thread = {
    val runnable = new Runnable {
      override def run(): Unit = fun
    }
    val thread = new Thread(runnable)
    thread.start()
    thread
  }
}