package com.lineate.scala.source


trait Source[C[_], V] extends Serializable {
  def init(): C[V]

  def start() = {}
}






