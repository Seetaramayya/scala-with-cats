package com.seeta.chapter4.monad.custom

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class MonadTest extends AnyWordSpec with Matchers {
  "Monad" should {
    "verify map of option monad" in {
      val m = new Monad[Option] {
        override def pure[A](a: A): Option[A] = Some(a)
        override def flatMap[A, B](box: Option[A])(f: (A) => Option[B]): Option[B] = box match {
          case Some(a) => f(a)
          case None => None
        }
      }

      m.map(Some("1"))(_.toInt) shouldBe Some(1)
      m.map(Option.empty[String])(_.toInt) shouldBe None
    }

    "verify map of list monad" in {
      val m = new Monad[List] {
        override def pure[A](a: A): List[A] = List(a)
        override def flatMap[A, B](box: List[A])(f: (A) => List[B]): List[B] = box match {
          case Nil => List()
          case x :: xs => f(x) ++ flatMap(xs)(f)
        }
      }

      m.map(List(1, 2, 3))(x => x * x) shouldBe List(1, 4, 9)
    }
  }
}
