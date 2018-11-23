package com.lineate.scala


import org.apache.kafka.streams.kstream.ForeachAction
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.spark.streaming.dstream.DStream

import scala.reflect.ClassTag

package object conversion {

  object test {

    implicit object TestStreamOps extends AsComputation[Stream] {
      override def map[I, O: ClassTag](c: Stream[I])(f: (I) => O): Stream[O] = c.map(f)

      override def filter[I, O: ClassTag](c: Stream[I])(f: (I) => Boolean): Stream[I] = c.filter(f)

      override def flatMap[I, O: ClassTag](c: Stream[I])(f: (I) => Iterable[O]): Stream[O] = c.flatMap(f)

      override def to[I: ClassTag](c: Stream[I])(destination: I => Unit): Unit = {
        c.foreach(destination(_))
      }
    }

  }

  object kafka {
    type KStreamStringKey[I] = KStream[String, I]

    implicit object KStreamAnyStreamOps$ extends AsComputation[KStreamStringKey] {

      import org.apache.kafka.streams.scala.kstream._


      override def map[I, O: ClassTag](c: KStreamStringKey[I])(f: (I) => O): KStream[String, O] = c.mapValues(f)

      override def filter[I, O: ClassTag](c: KStreamStringKey[I])(f: (I) => Boolean): KStream[String, I] =
        c.filter((_, e) => f(e))

      override def flatMap[I, O: ClassTag](c: KStreamStringKey[I])(f: (I) => Iterable[O]): KStream[String, O] =
        c.flatMapValues(f)

      override def to[I: ClassTag](c: KStreamStringKey[I])(destination: I => Unit): Unit = {
        c.inner.foreach(new ForeachAction[String, I] {
          override def apply(k: String, v: I): Unit = destination(v)
        })
      }
    }

  }

  object spark {
    implicit object DStreamMonadic extends AsComputation[DStream] {
      override def map[I, O: ClassTag](c: DStream[I])(f: (I) => O): DStream[O] = c.map(f)

      override def filter[I, O: ClassTag](c: DStream[I])(f: (I) => Boolean): DStream[I] = c.filter(f)

      override def flatMap[I, O: ClassTag](c: DStream[I])(f: (I) => Iterable[O]): DStream[O] = c.flatMap(f)

      override def to[I: ClassTag](c: DStream[I])(destination: I => Unit): Unit = {
        c.foreachRDD(_.foreach(destination(_)))
      }
    }
  }
}

