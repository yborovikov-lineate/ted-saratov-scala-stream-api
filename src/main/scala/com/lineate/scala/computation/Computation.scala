package com.lineate.scala.computation


import com.lineate.scala.conversion.AsComputation
import com.lineate.scala.source.Source

import scala.language.higherKinds
import scala.reflect.ClassTag

abstract class Computation[C[_]: AsComputation, I: ClassTag](src: Source[C, _]) {
  def map[O: ClassTag](f: (I) => O): Computation[C, O] = Op[C, O](() => implicitly[AsComputation[C]].map(this.exec())(f), src)

  def filter(f: (I) => Boolean): Computation[C, I] = Op[C, I](() => implicitly[AsComputation[C]].filter(this.exec())(f), src)

  def flatMap[O: ClassTag](f: (I) => Iterable[O]): Computation[C, O] = Op[C, O](() => implicitly[AsComputation[C]].flatMap(this.exec())(f), src)

  private[computation] def exec(): C[I]

  def to(destination: I => Unit): Unit = {
    implicitly[AsComputation[C]].to(this.exec())(destination)
    src.start()
  }
}

object Computation {
  def from[C[_], I: ClassTag](source:  Source[C, I])(implicit monadic: AsComputation[C]): Computation[C, I] =
    Op[C, I](() => source.init(), source)
}

case class Op[C[_]: AsComputation, O: ClassTag](thunk: () => C[O], src: Source[C, _]) extends Computation[C, O](src) {
  private[computation] def exec(): C[O] = thunk()
}



