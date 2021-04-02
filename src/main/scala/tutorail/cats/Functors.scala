package tutorail.cats

import cats.Functor
import cats.instances.int._
import cats.instances.string._
import cats.instances.list._
import cats.instances.option._
import cats.instances.try_._
import cats.syntax.functor._

import scala.util.Try // Extension methods for functor

// All the above imports can be replaced with import cats._ & import cats.implicits._

//
/**
 * A functor is higher ordered type class. if you provide initial `F` type and a mapping function
 * from an arbitrary type A => B then `map` will transform F[A] => F[B]
 *
 * Definition is something like this
 * {{{
 *   trait Functor[F[_]] {
 *    def map[A, B](init: F[A])(f: A => B): F[B]
 *   }
 * }}}
 *
 * More about functors, See [[https://typelevel.org/cats/typeclasses/functor.html]]
 */
object Functors {
  private val numbers: List[Int] = (1 to 10).toList

  // following map is a extension method for any kind of functor
  def increment[F[_]: Functor](fa: F[Int]): F[Int] = fa.map(_ + 1)

  def main(args: Array[String]): Unit = {
    println("=" * 60)
    println("Using functor map")
    // List functor
    println(Functor[List].map(numbers)(_ + 2)) // is equal to List[Int].map(_ + 2)

    // Option functor
    println(Functor[Option].map(Option(2))(_ + 1)) // Some(3)
    println(Functor[Option].map(Option.empty[Int])(_ + 1)) // None

    // List functor contains Option functor :)
    val optionalNumbers: List[Option[Int]] = numbers.map(Option.apply)
    println(Functor[List].map(optionalNumbers)(x => Functor[Option].map(x)(_ + 2)))

    // Try functor
    println(Functor[Try].map(Try(10))(_ + 10)) // Success(20)

    println("=" * 60)
    println("General increment function with any functor container:")
    println(s"Increment empty list should be empty  : ${increment(List[Int]())}")
    println(s"Increment non-empty list should be    : ${increment(List(1, 2, 3))}")
    println(s"Increment non-empty option should be  : ${increment(Option(1))}")
    println(s"Increment empty option should be      : ${increment(Option.empty[Int])}")
    println(s"Increment non-empty try should be     : ${increment(Try(1))}")
    println(s"Increment empty try should be         : ${increment(Try[Int](throw new IllegalArgumentException("boom")))}")
  }
}
