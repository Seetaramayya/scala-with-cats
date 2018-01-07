package com.seeta.chapter3.functors

import org.scalatest.{Matchers, WordSpec}
import cats.syntax.functor._

class FunctorTreeSpec extends WordSpec with Matchers {
  "Tree" should {
    "map properly, due to functor nature" in {
      Tree.branch(Leaf(1), Leaf(2)).map(_ + 1) shouldBe Branch(Leaf(2), Leaf(3))
      Tree.leaf(1).map(_ * 5) shouldBe Leaf(5)
    }
  }

}
