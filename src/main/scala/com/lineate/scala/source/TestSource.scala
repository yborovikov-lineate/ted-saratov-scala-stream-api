package com.lineate.scala.source

import scala.reflect.ClassTag

class TestSource[V: ClassTag](in: Stream[V]) extends Source[Stream, V] {
  override def init(): Stream[V] = in
}