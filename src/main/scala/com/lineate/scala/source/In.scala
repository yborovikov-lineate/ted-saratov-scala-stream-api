package com.lineate.scala.source

import scala.reflect.ClassTag

object In {
  def kafka[V: ClassTag](topic: String)(mapper: String => V) = new KafkaStreamSource[V](topic)(mapper)

  def spark[V: ClassTag](topic: String)(mapper: String => V) = new DStreamSource[V](topic)(mapper)

  def input[V: ClassTag](vs: V*) = new TestSource(Stream(vs: _*))
}
