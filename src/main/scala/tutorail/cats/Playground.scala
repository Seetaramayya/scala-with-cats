package tutorail.cats

import cats.Eval
import cats.Eq
import cats.instances.int._
import cats.instances.list._
import cats.instances.option._
import cats.syntax.eq._

object Playground extends App {

  println(2 === 3)
  println(List(2) === List(3))
  println(Option(2) === Option.empty[Int])

}
