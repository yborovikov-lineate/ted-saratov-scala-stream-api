package com.lineate.scala.source

import java.util.Properties

import com.lineate.scala.Config
import com.lineate.scala.conversion.kafka.KStreamStringKey
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import scala.reflect.ClassTag

class KafkaStreamSource[V: ClassTag](topic: String)(mapper: String => V) extends Source[KStreamStringKey, V] {
  private lazy val builder = new StreamsBuilder()

  override def init(): KStreamStringKey[V] = {
    val values: KStreamStringKey[V] = builder.stream[String, String](topic).mapValues(mapper(_))
    values
  }

  override def start() = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, Config.AppName)
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Config.KafkaHostString)
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)

    val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
    streams.cleanUp()
    streams.start()
  }
}



