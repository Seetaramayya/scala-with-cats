package com.seeta.chapter3.functors

import com.seeta.Box

trait Codec[A] {
  self =>
  def encode(e: A): String
  def decode(d: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
    override def encode(b: B): String = self.encode(enc(b))
    override def decode(d: String): B = dec(self.decode(d))
  }
}

object Codec {
  def encode[A](e: A)(implicit codec: Codec[A]): String = codec.encode(e)
  def decode[A](d: String)(implicit codec: Codec[A]): A = codec.decode(d)

  implicit val stringCodec: Codec[String] = new Codec[String] {
    override def encode(e: String): String = e
    override def decode(d: String): String = d
  }

  //Codec[String] => Codec[Int]
  implicit val intCodec: Codec[Int] = stringCodec.imap(str => str.toInt, int => int.toString)

  //Codec[Boolean] => Codec[Int]
  implicit val booleanCodec: Codec[Boolean] = stringCodec.imap(str => str.toBoolean, boolean => boolean.toString)

  //Codec[Double] => Codec[Int]
  implicit val doubleCodec: Codec[Double] = stringCodec.imap(_.toDouble, _.toString)

  //Codec[Box[A]] => Codec[A]; box => A, A => Box
  implicit def boxCodec[A](implicit codec: Codec[A]): Codec[Box[A]] = codec.imap(a => Box(a), box => box.value)
}
