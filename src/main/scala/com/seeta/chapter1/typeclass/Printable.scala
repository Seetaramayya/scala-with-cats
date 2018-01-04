package com.seeta.chapter1.typeclass

import com.seeta.{ Person, personToString }

/**
 * ==Type class==
 * is trait that represent some functionality.
 *
 * @tparam A type parameter
 */
trait Printable[A] {
  def format(value: A): String
}

/**
 * Type class instances
 */
object PrintableInstances {
  implicit val stringPrintable: Printable[String] = (value: String) => value
  implicit val intPrintable: Printable[Int] = (value: Int) => s"Int = $value"

  implicit val personPrintable: Printable[Person] = personToString
}

/**
 * ==Type class interface==
 *  is any functionality that we want to expose to users. Interfaces are generic
 *  methods that accepts typeclass instances as a implicit parameters. There are
 *  Two common ways to do so.
 *
 *  ==1. Interface Objects==
 *  These methods are placed in singleton objects.
 */
object Printable {
  def format[A](a: A)(implicit instance: Printable[A]): String = instance.format(a)
  def print[A](a: A)(implicit instance: Printable[A]): Unit = println(format(a))
}

/**
 * ==2. Interface Syntax==
 * This is a alternative approach (to above one), extends methods to existing types.
 * {{{person.show}}}
 */
object PrintableSyntax {
  implicit class PrintableOps[A](a: A) {
    def format(implicit instance: Printable[A]): String = instance.format(a)
    def print(implicit instance: Printable[A]): Unit = println(format)
  }
}

object TypeClassDemo extends App {
  val seeta = Person("Seeta Vadali", 36, "brown")

  //interface syntax (or extension methods)
  import PrintableSyntax._
  import PrintableInstances._
  seeta.print

  //interface objects
  Printable.print(seeta)
}