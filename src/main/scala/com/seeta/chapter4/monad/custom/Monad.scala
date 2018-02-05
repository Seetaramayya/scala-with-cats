package com.seeta.chapter4.monad.custom

trait Monad[F[_]] {
  def pure[A](a: A): F[A]
  def flatMap[A, B](box: F[A])(f: A => F[B]): F[B]
  def map[A, B](box: F[A])(f: A => B): F[B] = flatMap(box)(a => pure(f(a)))
}
