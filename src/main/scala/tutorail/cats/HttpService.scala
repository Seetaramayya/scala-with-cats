package tutorail.cats

import cats.Monad
import cats.instances.option._
import cats.instances.try_._
import cats.instances.future._
import cats.instances.either._

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

case class Connection(host: String, port: String)
trait HttpService[M[_]] {
  def getConnection(cfg: Map[String, String]): M[Connection]
  def issueRequest(connection: Connection, payload: String): M[String]
}
object HttpService {
  val config: Map[String, String] = Map[String, String]("host" -> "localhost", "port" -> "4040")

  object OptionHttpService extends HttpService[Option] {
    override def getConnection(cfg: Map[String, String]): Option[Connection] = (cfg.get("host"), cfg.get("port")) match {
      case (Some(host), Some(port)) => Option(Connection(host, port))
      case _                        => None
    }
    override def issueRequest(connection: Connection, payload: String): Option[String] =
      if (payload.length < 20) Option("request (payload) has been accepted") else None
  }

  object TryHttpService extends HttpService[Try] {
    override def getConnection(cfg: Map[String, String]): Try[Connection] = (cfg.get("host"), cfg.get("port")) match {
      case (Some(host), Some(port)) => Success(Connection(host, port))
      case _                        => Failure(new IllegalArgumentException("config does not have host and port"))
    }
    override def issueRequest(connection: Connection, payload: String): Try[String] =
      if (payload.length < 20) Success("request (payload) has been accepted")
      else Failure(new IllegalArgumentException("given payload is greater than 20 characters"))
  }

  type ErrorOr[T] = Either[String, T]
  object ErrorOrHttpService extends HttpService[ErrorOr] {
    override def getConnection(cfg: Map[String, String]): ErrorOr[Connection] = (cfg.get("host"), cfg.get("port")) match {
      case (Some(host), Some(port)) => Right(Connection(host, port))
      case _                        => Left("config does not have host and port")
    }
    override def issueRequest(connection: Connection, payload: String): ErrorOr[String] =
      if (payload.length < 20) Right("request (payload) has been accepted")
      else Left("given payload is greater than 20 characters")
  }

  object FutureOrHttpService extends HttpService[Future] {
    override def getConnection(cfg: Map[String, String]): Future[Connection] = (cfg.get("host"), cfg.get("port")) match {
      case (Some(host), Some(port)) => Future.successful(Connection(host, port))
      case _                        => Future.failed(new IllegalArgumentException("config does not have host and port"))
    }
    override def issueRequest(connection: Connection, payload: String): Future[String] =
      if (payload.length < 20) Future.successful("request (payload) has been accepted")
      else Future.failed(new IllegalArgumentException("given payload is greater than 20 characters"))
  }

  def getResponse[M[_]: Monad](httpService: HttpService[M], payload: String, cfg: Map[String, String] = config): M[String] = {
    import cats.syntax.flatMap._
    import cats.syntax.functor._
    for {
      connection <- httpService.getConnection(cfg)
      response <- httpService.issueRequest(connection, payload)
    } yield response
  }

  type Identity[T] = T
  implicit object IdentityMonad extends Monad[Identity] {
    override def pure[A](x: A): Identity[A] = x
    override def flatMap[A, B](fa: Identity[A])(f: A => Identity[B]): Identity[B] = f(fa)

    @tailrec
    override def tailRecM[A, B](a: A)(f: A => Identity[Either[A, B]]): Identity[B] = f(a) match {
      case Right(b) => b
      case Left(a)  => tailRecM(a)(f)
    }
  }
}

object TestHttpService extends App {
  import HttpService._

  val responseOptionFor = for {
    conn <- OptionHttpService.getConnection(HttpService.config)
    response <- OptionHttpService.issueRequest(conn, "Hello, Http service")
  } yield response

  import scala.concurrent.ExecutionContext.Implicits.global

  println(getResponse(OptionHttpService, "Hello, http service"))
  println(getResponse(TryHttpService, "Hello, http service"))
  println(getResponse(ErrorOrHttpService, "Hello, http service"))

  getResponse(FutureOrHttpService, "Hello, http service with future M").onComplete {
    case Success(value) => println(s"Success: $value")
    case Failure(ex)    => println(s"Failure: $ex")
  }

  // Another Exercise
  sealed trait Tree[+T]
  case class Leaf[+T](value: T) extends Tree[T]
  case class Branch[+T](left: Tree[T], right: Tree[T]) extends Tree[T]

  object Tree {
    implicit object TreeMonad extends Monad[Tree] {
      override def pure[A](a: A): Tree[A] = Leaf(a)

      override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fa match {
        case Leaf(a)             => f(a)
        case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
      }

      override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {
        def stackRec(currentNode: Tree[Either[A, B]]): Tree[B] = currentNode match {
          case Branch(leftTreeEither, rightTreeEither) => Branch(stackRec(leftTreeEither), stackRec(rightTreeEither))
          case Leaf(Left(a))                           => stackRec(f(a))
          case Leaf(Right(b))                          => Leaf(b)
        }
        // stackRec(f(a))

        /*

        Input Tree:
                     1
                 2       3
              L1   R2  R3   R4

          tr(List(1), Set(), List())
          tr(List(3, 2, 1), Set(1), List())
          tr(List(R4, R3, 3, 2, 1), Set(3, 1), List())
          tr(List(R3, 3, 2, 1), Set(3, 1), List(B4))
          tr(List(3, 2, 1), Set(3, 1), List(B3, B4))
          tr(List(2, 1), Set(3, 1), List(B34))
          tr(List(R2, L1, 2, 1), Set(2, 3, 1), List(B34))
          tr(List(L1, 2, 1), Set(2, 3, 1), List(B2, B34))
          tr(List(R1, 2, 1), Set(2, 3, 1), List(B2, B34))
          tr(List(2, 1), Set(2, 3, 1), List(B1, B2, B34))
          tr(List(1), Set(2, 3, 1), List(B12, B34))
          tr(List(), Set(2, 3, 1), List(B1234))
          B1234
         */
        @tailrec
        def tailRec(todo: List[Tree[Either[A, B]]], expanded: Set[Tree[Either[A, B]]], done: List[Tree[B]]): Tree[B] = {
          if (todo.isEmpty) done.head
          else {
            todo.head match {
              case Leaf(Left(a))  => tailRec(f(a) :: todo.tail, expanded, done)
              case Leaf(Right(b)) => tailRec(todo.tail, expanded, Leaf(b) :: done)
              case node @ Branch(left, right) =>
                if (!expanded.contains(node)) {
                  tailRec(right :: left :: todo, expanded + node, done)
                } else {
                  val newLeft = done.head
                  val newRight = done.tail.head
                  val newBranch = Branch(newLeft, newRight)
                  tailRec(todo.tail, expanded, newBranch :: done.drop(2))
                }
            }
          }
        }

        tailRec(List(f(a)), Set(), List())
      }
    }
  }
  import Tree._
  import cats.syntax.flatMap._
  import cats.syntax.functor._
  val tree: Tree[Int] = Branch(Branch(Leaf(10), Leaf(12)), Branch(Leaf(20), Leaf(30)))
  println(tree)
  println(tree.flatMap(i => Branch(Leaf(i - 1), Leaf(i + 1))))

}
