package com.seeta.chapter3.functors.custom

import org.scalatest.{Matchers, WordSpec}

class FunctorSpec extends WordSpec with Matchers {

  "Functor" should {
    "execute basic tests" in {
      implicitly[Functor[List]].map(List(1, 2, 3))(_ + 2) shouldBe List(3, 4, 5)
    }
  }
}
