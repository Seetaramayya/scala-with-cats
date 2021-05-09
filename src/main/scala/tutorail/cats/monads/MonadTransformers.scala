package tutorail.cats.monads

import java.util.concurrent.Executors

import cats.instances.list._
import cats.instances.future._
import cats.data.OptionT
import cats.data.EitherT

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Monad transformers are not type classes but they are higher kinded types which provides convenience methods
 *
 * `OptionT` is a monad transformer for `Option`.
 * `EitherT` is a monad transformer for `Either`.
 */
object MonadTransformers extends App {
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))
  // Option monad transformer
  val listOfNumberOptions: OptionT[List, Int] = OptionT(List(Option(1), Option(2), Option(3)))
  val listOfCharOptions: OptionT[List, Char] = OptionT(List(Option('a'), Option('b'), Option.empty[Char]))
  val listOfTupleOptions: OptionT[List, (Int, Char)] = for {
    char <- listOfCharOptions
    number <- listOfNumberOptions
  } yield (number, char)

  // Either monad transformer
  val listOfNumberEither: EitherT[List, String, Int] = EitherT.right(List(1, 2))
  val listOfCharEither: EitherT[List, String, Char] = EitherT.right(List('a', 'b'))
  val listOfTupleEither: EitherT[List, String, (Int, Char)] = for {
    number <- listOfNumberEither
    char <- listOfCharEither
  } yield (number, char)

  // Output
  println(listOfTupleOptions.value)
  println(listOfTupleEither.value)

  val bandwidths = Map("server1" -> 50, "server2" -> 300, "server3" -> 170)
  type AsyncResponse[T] = EitherT[Future, String, T]

  def getBandwidth(server: String): AsyncResponse[Int] = bandwidths.get(server) match {
    case None            => EitherT.left(Future(s"Server $server unreachable")) // is === Future[Either[String, Int]](Left(""))
    case Some(bandwidth) => EitherT.right(Future.successful(bandwidth)) // => Future[Right(int)] => Future[Either[String, Int]]
  }

  // Exercise1 check both given servers bandwidth together is > 250 or not
  def canWithStandSurge(s1: String, s2: String): AsyncResponse[Boolean] = {
    for {
      bandwidth1 <- getBandwidth(s1)
      bandwidth2 <- getBandwidth(s2)
    } yield bandwidth1 + bandwidth2 > 250
  }

  // Exercise 2
  def generateTrafficSpikeReport(s1: String, s2: String): AsyncResponse[String] = {
    canWithStandSurge(s1, s2).transform {
      case Left(message)         => Left(message)
      case Right(value) if value => Right("Servers can with stand")
      case Right(value)          => Right("Servers can not with stand")
    }
  }

  generateTrafficSpikeReport("server1", "server3").value.foreach(println)
}
