package com.seeta.chapter3.functors

import com.seeta.Box
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PrintableContramapSpec extends AnyWordSpec with Matchers {

  "Contramap Printable Functor" should {
    "format string or boolean" in {
      import Printable._
      format("hello") shouldBe "hello"
      format(true) shouldBe "yes"

      //
      format(Box(10)) shouldBe "10"
      format(Box(true)) shouldBe "yes"
      format(Box("hello")) shouldBe "hello"
    }
  }
}
