package com.seeta.chapter2.monoids.cats

import cats.Monoid

object SuperAdder {
  //Hey intellij, I know `sum` exists
  def intAdd(items: List[Int]): Int = items.foldLeft(0)(_ + _)

  def genericAdd[A](items: List[A])(implicit monoid: Monoid[A]): A = items.foldLeft(monoid.empty)(monoid.combine)
}
