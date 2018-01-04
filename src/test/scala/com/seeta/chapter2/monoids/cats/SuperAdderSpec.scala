package com.seeta.chapter2.monoids.cats

import org.scalatest.{Matchers, WordSpec}


class SuperAdderSpec extends WordSpec with Matchers {

  "SuperAdderSpec" should {

    "return correct summed value when genericAdd is invoked" in {
      import cats.instances.int._
      import cats.instances.option._
      import cats.instances.map._

      SuperAdder.genericAdd(List(1, 2, 3)) shouldBe 6
      SuperAdder.genericAdd(List(Some(1), None, Some(2))) shouldBe Some(3)
      SuperAdder.genericAdd(List(Map(1 -> 1, 2 -> 2), Map(1 -> 1, 3 -> 3))) shouldBe Map(1 -> 2, 2 -> 2, 3 -> 3)
    }

    "return correct summed value when intAdd is invoked" in {
      SuperAdder.intAdd(List(1, 2, 3)) shouldBe 6
    }

  }
}
