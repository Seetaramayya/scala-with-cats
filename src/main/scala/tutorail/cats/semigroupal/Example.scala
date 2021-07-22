package tutorail.cats.semigroupal

import cats.Semigroupal
import cats.Monad
import cats.instances.option._
import cats.instances.future._
import cats.instances.list._
import cats.syntax.flatMap._
import cats.syntax.functor._

import java.util.concurrent.Executors
import scala.concurrent.{ ExecutionContext, Future }

/**
 * A semigroupal looks like this
 *
 * trait MySemigroupal {
 *   def production[F[_], A, B](fa: F[A], fb: F[B]): F[(A, B)]
 * }
 *
 */
object Example {

  val optionSemigroupal = Semigroupal[Option]
  val aTupledOption = optionSemigroupal.product(Some("x"), Some(123)) //Some(("x", 123))
  val aNoneOption = optionSemigroupal.product(None, Some(123)) //None

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aTupledFuture = Semigroupal[Future].product(Future(""), Future(123)) // executes parallel and returns tupled result

  val aTupledList = Semigroupal[List].product(List(1, 2, 3), List('a', 'b')) //a cartesian product

  def productWithMonads[F[_], A, B](fa: F[A], fb: F[B])(implicit m: Monad[F]): F[(A, B)] = {
    for {
      a <- fa
      b <- fb
    } yield (a, b)
  }

  // Define a Semigroupal[List] which does a zip
  def zip[A, B](fa: Semigroupal[List], fb: Semigroupal[List]): Semigroupal[List] = fa.product()

  def main(args: Array[String]): Unit = {
    println(aTupledList)
  }
}
