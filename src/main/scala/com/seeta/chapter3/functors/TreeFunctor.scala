package com.seeta.chapter3.functors

import cats.Functor

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  implicit val treeFunctor = new Functor[Tree] {
    override def map[A, B](tree: Tree[A])(f: (A) => B): Tree[B] = tree match {
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value)         => Leaf(f(value))
    }
  }

  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](a: A): Tree[A] = Leaf(a)
}
