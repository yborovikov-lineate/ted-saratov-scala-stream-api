package com.lineate.scala.source

import com.lineate.scala.Config
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.reflect.ClassTag

class DStreamSource[V: ClassTag](topic: String)(mapper: String => V) extends Source[DStream, V] with Serializable {

  @transient private  lazy val ssc: StreamingContext = {
    val conf = new SparkConf().setMaster("local[2]").setAppName(Config.AppName)
    new StreamingContext(conf, Seconds(10))
  }

  override def init(): DStream[V] = {
    KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](List(topic), Map(
        "group.id" -> "test-group-id",
        "bootstrap.servers" -> Config.KafkaHostString,
        "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
        "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
      ))
    )
      .map(r => mapper(r.value()))
  }

  override def start() = {
    ssc.start()
    ssc.awaitTermination()
  }
}