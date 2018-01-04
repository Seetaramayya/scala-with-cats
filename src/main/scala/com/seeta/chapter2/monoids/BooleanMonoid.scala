package com.seeta.chapter2.monoids

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

sealed trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit instance: Monoid[A]): Monoid[A] = instance
}

object Instances {
  implicit val booleanAndMonoid = new Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = x && y
  }

  implicit val booleanOrMonoid = new Monoid[Boolean] {
    override def empty: Boolean = false
    override def combine(x: Boolean, y: Boolean): Boolean = x || y
  }
}
