package com.seeta.chapter3.functors

trait Printable[A] {
  self =>
  def format(value: A): String

  def contramap[B](f: B => A): Printable[B] = new Printable[B] {
    override def format(b: B) = self.format(f(b))
  }
}

object Printable {

  implicit val stringPrintable: Printable[String] = str => s"""$str"""
  implicit val booleanPrintable: Printable[Boolean] = boolean => if(boolean) "yes" else "no"

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)
}
