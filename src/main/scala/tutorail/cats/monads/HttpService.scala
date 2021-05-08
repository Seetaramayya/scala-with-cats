package tutorail.cats.monads

import cats.Monad
import cats.instances.either._
import cats.instances.future._
import cats.instances.option._
import cats.instances.try_._

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

}
