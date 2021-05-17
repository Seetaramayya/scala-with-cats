package tutorail.cats.data

import cats.Id
import cats.data.{ Writer, WriterT }
import cats.instances.list._
import cats.instances.vector._

import scala.annotation.tailrec

/**
 * Writer is a simple wrapper which contains logs and desired value.
 */
object Writers {
  val writer: Writer[List[String], Int] = Writer(List("Started"), 1)

  def countAndSay(n: Int): Unit =
    if (n <= 0) println("Starting!")
    else {
      countAndSay(n - 1)
      println(n)
    }

  def countAndLogSeeta(n: Int): Writer[Vector[String], Int] = {
    @tailrec
    def loop(current: Int, acc: Writer[Vector[String], Int]): Writer[Vector[String], Int] =
      if (current <= 0) {
        val logs: Vector[String] = acc.written
        Writer("Started" +: logs, n)
      } else {
        loop(current - 1, acc.bimap(current.toString +: _, _ => current))
      }

    loop(n - 1, Writer(Vector(n.toString), n))
  }

  def countAndLogDaniel(n: Int): Writer[Vector[String], Int] = {
    if (n <= 0) Writer(Vector("Started"), n)
    else countAndLogDaniel(n - 1).flatMap(_ => Writer(Vector(s"$n"), n))
  }

  def countAndLogSeetaTailRec(n: Int): Writer[Vector[String], Int] = {
    @tailrec
    def loop(current: Int, acc: Writer[Vector[String], Int]): Writer[Vector[String], Int] = {
      if (current > n) acc
      else loop(current + 1, acc.flatMap(_ => Writer(Vector(s"$current"), current)))
    }
    loop(1, Writer(Vector("Started"), 0))
  }

  def seeta(n: Int): Writer[Vector[String], Int] = {
    @tailrec
    def loop(w: Writer[Vector[String], Int]): Writer[Vector[String], Int] = {
      if (w.value >= n) w
      else loop(w.bimap(_ :+ (w.value + 1).toString, _ + 1))
    }
    loop(Writer(Vector("Seeta Starting"), 0))
  }

  def naiveSum(n: Int): Int = {
    if (n <= 0) 0
    else {
      println(s"Now at $n")
      val lowerSum = naiveSum(n - 1)
      println(s"Computed sum ${n - 1} = $lowerSum")
      lowerSum + n
    }
  }

  def naiveSumWithWriter(n: Int): Writer[Vector[String], Int] = {
    if (n <= 0) Writer(Vector(), 0)
    else {
      for {
        _ <- Writer(Vector(s"Now at $n"), n)
        lowerSum <- naiveSumWithWriter(n - 1)
        _ <- Writer(Vector(s"Computed sum ${n - 1} = $lowerSum"), n)
      } yield lowerSum + n
    }
  }

  def main(args: Array[String]): Unit = {
    val writerA = Writer(Vector("A1", "A2"), 10)
    val writerB = Writer(Vector("B1"), 40)
    val combinedWriter = for {
      va <- writerA
      vb <- writerB
    } yield va + vb

//    println(naiveSum(10))
    naiveSumWithWriter(10).written.foreach(println)
  }
}
