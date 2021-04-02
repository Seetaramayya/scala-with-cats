package com.seeta.chapter1.typeclass

import java.util.Date

import cats.instances.int._
import cats.instances.string._
import cats.instances.long._
import cats.instances.option._
import cats.Eq
import cats.syntax.eq._
import cats.syntax.option._
import com.seeta.Person

/**
 * ==Cats Eq==
 * is another type class which supports ''type-safe equality''
 *
 * == Custom Eq Type==
 * custom type can be defined with ''Eq.instance'' method which accepts
 * a function of type ''(A, A) => Boolean''
 *
 *
 * @author Seeta (Ramayya) Vadali
 */

object EqCustomInstances {
  implicit val eqDate: Eq[Date] = Eq.instance[Date] { (date1, date2) => date1.getTime === date2.getTime }

  implicit val eqPerson: Eq[Person] = Eq.instance[Person] { (p1, p2) => (p1.name === p2.name) && (p1.age === p2.age) && (p1.color === p2.color) }
}

object CatsEqDemo extends App {

  println(123 === 123)
  println(Option(123) === Option.empty[Int])

  println(123.some === none[Int])

  // Different types: Option[String] & Option[Int] comparision
  // expected compile time error with cats Eq that is possible
  println("123".some == none[Int])

  import EqCustomInstances._
  val date1 = new Date()
  val date2 = new Date()

  println(date1 === date2)

  println(Person("Seeta Vadali", 36, "brown").some === none[Person])
  println(Person("Seeta Vadali", 36, "brown").some === Person("Seeta Vadali", 36, "brown").some)
}
