package com

package object seeta {
  //Data or Model classes
  final case class Person(name: String, age: Int, color: String)
  final case class Box[A](value: A)

  //Utilities
  def personToString(p: Person): String = s"${p.name} is a ${p.age} year old ${p.color} person."
}
