package com.seeta.chapter3.functors

import cats.syntax.functor._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FunctorTreeSpec extends AnyWordSpec with Matchers {
  "Tree" should {
    "map properly, due to functor nature" in {
      Tree.branch(Leaf(1), Leaf(2)).map(_ + 1) shouldBe Branch(Leaf(2), Leaf(3))
      Tree.leaf(1).map(_ * 5) shouldBe Leaf(5)
    }
  }

}
