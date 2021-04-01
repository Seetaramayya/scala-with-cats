package com.seeta.chapter2.monoids.cats

import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.instances.map._
import cats.instances.tuple._
import cats.syntax.semigroup._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class SuperAdderSpec extends AnyWordSpec with Matchers {

  "SuperAdderSpec" should {

    "return correct summed value when genericAdd is invoked" in {
      SuperAdder.genericAdd(List(1, 2, 3)) shouldBe 6
      SuperAdder.genericAdd(List(Some(1), None, Some(2))) shouldBe Some(3)
      SuperAdder.genericAdd(List(Map(1 -> 1, 2 -> 2), Map(1 -> 1, 3 -> 3))) shouldBe Map(1 -> 2, 2 -> 2, 3 -> 3)
    }

    "return correct summed value when intAdd is invoked" in {
      SuperAdder.intAdd(List(1, 2, 3)) shouldBe 6
    }
  }

  "Cats" should {
    "add (or combine) any type" in {

      Option(1) |+| Option.empty[Int] |+| Option(2) shouldBe Option(3)

      val map1 = Map("a" -> 1, "b" -> 1)
      val map2 = Map("b" -> 1, "c" -> 1)
      map1 |+| map2 shouldBe Map("a" -> 1, "b" -> 2, "c" -> 1)

      ("hello", 1) |+| ("world", 2) shouldBe ("helloworld", 3)
    }
  }
}
