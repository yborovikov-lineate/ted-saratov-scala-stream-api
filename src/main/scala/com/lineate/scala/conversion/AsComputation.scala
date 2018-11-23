package com.lineate.scala.conversion

import scala.reflect.ClassTag


trait AsComputation[C[_]] {
  def map[I, O: ClassTag](c: C[I])(f: (I) => O): C[O]

  def filter[I, O: ClassTag](c: C[I])(f: (I) => Boolean): C[I]

  def flatMap[I, O: ClassTag](c: C[I])(f: (I) => Iterable[O]): C[O]

  def to[I: ClassTag](c: C[I])(destination: I => Unit): Unit
}