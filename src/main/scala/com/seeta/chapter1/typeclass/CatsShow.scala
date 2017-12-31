package com.seeta.chapter1.typeclass

import cats.Show
import com.seeta.{Person, personToString}

/**
  * ==Cats Type Classes==
  * The type classes in Cats are defined in `cats` package.
  * {{{
  *   import cats.Show
  * }}}
  *
  * == Importing Default Instances==
  * {{{
  * cats.instances.int provides instances for Int
  * cats.instances.string provides instances for String
  * cats.instances.list provides instances for List
  * cats.instances.option provides instances for Option
  * cats.instances.all provides all instances that are shipped out of the box with Cats
  * }}}
  *
  * ==Importing Interface Syntax==
  *
  * {{{
  *   import cats.syntax.show._
  * }}}
  *
  * @author Seeta (Ramayya) Vadali
  */
object CatsCustomInstances {
  implicit val personShow: Show[Person] = Show.show(personToString)
}

object CatsShowDemo extends App {
  import CatsCustomInstances._ //typeclass instance import
  import cats.syntax.show._ //typeclass interface syntax import
  println(Person("Seeta Vadali", 36, "brown").show)
}
