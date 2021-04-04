package tutorail.cats

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

import cats.Monad //brings Monad Type class definition into the scope
import cats.instances.option._ //brings type class instances for option
import cats.instances.try_._ //brings type class instances for try
import cats.instances.list._ //brings type class instances for list
import cats.instances.future._ //brings type class instances for future

/**
 * A monad is also higher kind of type class, with `pure` and `flatMap`
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
object Monads1 extends App {
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

  println(getPairs(Option(1), Option('a')))
  println(getPairs(Try(1), Try('a')))
  println(getPairs(List(1, 2, 3, 4), List('a', 'b')))
}
