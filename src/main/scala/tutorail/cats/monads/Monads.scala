package tutorail.cats.monads

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

/**
 * A monad is also higher kind of type class, with `pure` and `flatMap`.
 * Monads extends Functor
 *
 * Definition is something like this
 * {{{
 *   trait Monad[F[_]] {
 *    def pure[A](a: A): F[A]
 *    def flatMap[A, B](fa: F[A])(fb: A => F[B]): F[B]
 *   }
 * }}}
 *
 * More about monads, See [[https://typelevel.org/cats/typeclasses/monad.html]]
 */
trait SeetaMonad[M[_]] {
  def pure[A](a: A): M[A]
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

  // Exercise: Implement map method
  def map[A, B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => pure(f(a)))
}
object SeetaMonad {
  implicit val optionMonad: SeetaMonad[Option] = new SeetaMonad[Option] {
    override def pure[A](a: A): Option[A] = Option.apply(a)

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]): Option[B] = ma.flatMap(a => f(a))
  }

  def test[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: SeetaMonad[M]): M[(A, B)] = {
    monad.flatMap(ma) { a => monad.map(mb)(b => (a, b)) }
  }
}
object Monads1 extends App {
  import cats.Monad
  import cats.instances.future._
  import cats.instances.list._
  import cats.instances.option._
  import cats.instances.try_._
  import cats.syntax.applicative._
  import cats.syntax.flatMap._ // monad `flatMap` will be brought into the scope

  // lists
  val crossProductList = for {
    number <- List(1, 2, 3)
    char <- List('a', 'b', 'c')
  } yield (number, char)

  println(crossProductList)

  //options
  val numberOptions = Option(1)
  val charOptions = Option('a')
  val crossProductOption = for {
    number <- numberOptions
    char <- charOptions
  } yield (number, char)

  println(crossProductOption)

  //futures
  val numberFuture = Future.successful(1)
  val charFuture = Future.successful('a')
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
  val crossProductFuture = numberFuture.flatMap(number => charFuture.map(ch => (number, ch)))
  crossProductFuture.onComplete {
    case Success(value) => println(value)
    case Failure(ex)    => println(s"failed $ex")
  }

  val listMonad = Monad[List]
  val pureListMonad = listMonad.pure(1)
  println(listMonad.flatMap(pureListMonad)(x => List(x + 1)))

  val optionMonad = Monad[Option]
  val pureOptionMonad = optionMonad.pure(1)
  println(optionMonad.flatMap(pureOptionMonad)(x => Option(x)))

  val futureMonad = Monad[Future]
  val pureFutureMonad = futureMonad.pure(Future.successful(1))
  futureMonad.flatMap(pureFutureMonad)(x => Future(x)).onComplete {
    case Success(result) => println(s"Result is $result")
    case Failure(ex)     => println(s"failed $ex")
  }

  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: Monad[M]): M[(A, B)] = monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))

  def getParisFor[M[_]: Monad, A, B](ma: M[A], mb: M[B]): M[(A, B)] = {
    import cats.syntax.functor._ // brings map in to the scope
    for {
      a <- ma
      b <- mb
    } yield (a, b)
  }

  println(getPairs(Option(1), Option('a')))
  println(getPairs(Try(1), Try('a')))
  println(getPairs(List(1, 2, 3, 4), List('a', 'b')))
  println(getParisFor(List(1, 2, 3, 4), List('a', 'b')))

  // extension methods imports in the case of monad are different structure
  // pure extension method will be brought into scope with import cats.syntax.applicative._
  1.pure[Option] // wraps 1 in Option(1) => Some(1)
  1.pure[Try]
  1.pure[List]

  // flatMap can be brought into the scope with import cats.syntax.flatMap._
  // testing seeta monad
  println(SeetaMonad.test(Option(1), Option('a'))) // expected answer is Some((1, 'a'))
}
