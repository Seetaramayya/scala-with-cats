package com.seeta.chapter3.functors

import com.seeta.Box

trait Printable[A] {
  self =>
  def format(value: A): String

  def contramap[B](f: B => A): Printable[B] = new Printable[B] {
    override def format(b: B) = self.format(f(b))
  }
}

object Printable {

  // String => Int, Printable[Int]
  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    override def format(value: Int): String = s"$value"
  }

  // String => String, Printable[String]
  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(value: String): String = value
  }

  // Boolean => String, Printable[Boolean]
  implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
    override def format(value: Boolean): String = if (value) "yes" else "no"
  }

  // Box[A] => A, Printable[A] => Printable[Box[A]]
  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap[Box[A]](box => box.value)

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)
}
