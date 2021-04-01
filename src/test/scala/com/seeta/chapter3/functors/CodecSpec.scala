package com.seeta.chapter3.functors

import com.seeta.Box
import com.seeta.chapter3.functors.Codec.{decode, encode}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CodecSpec extends AnyWordSpec with Matchers {

  "Codec" should {
    "work" in {
      encode(123.4) shouldBe "123.4"
      encode(123) shouldBe "123"
      encode(true) shouldBe "true"
      encode("true") shouldBe "true"

      encode(Box(123.4)) shouldBe "123.4"
      encode(Box(123.4)) should not be 123.4

      decode[Box[Double]]("123.4") shouldBe Box(123.4)
      decode[Box[Boolean]]("false") shouldBe Box(false)
    }
  }
}
