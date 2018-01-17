package com.seeta.chapter3.functors

import org.scalatest.{Matchers, WordSpec}


class PrintableContramapSpec extends WordSpec with Matchers {

  "Contramap Printable Functor" should {
    "format string or boolean" in {
      import Printable._
      format("hello") shouldBe "hello"
      format(true) shouldBe "yes"
    }
  }
}
