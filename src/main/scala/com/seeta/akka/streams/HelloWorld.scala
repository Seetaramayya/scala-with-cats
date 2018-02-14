package com.seeta.akka.streams

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString

import scala.concurrent.Future

object HelloWorld extends App {
  private val FACTORIALS = "target/factorials.txt"
  println("Akka streams example")

  implicit val system = ActorSystem("HelloWorldStreamSystem")

  implicit val materializer = ActorMaterializer()
  val source: Source[Int, NotUsed] = Source(1 to 100)

  implicit val ec = system.dispatcher
  val result: Future[Unit] = source.runForeach(println).map(_ => ())
  val factorials = source.scan(BigInt(1))((acc, i) => acc * i)

  private val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get(FACTORIALS))

  val factorialResult: Future[Unit] =
    factorials.map(num => ByteString(s"$num\n")).runWith(sink).map(_ => ())

  //TODO: what is the best way to terminate ???
  for {
    _ <- result
    _ <- factorialResult
  } {
    system.terminate()
  }
}
