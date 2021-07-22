package tutorail.cats.applicatives

import cats.Applicative
import cats.instances.list

object Example {

  def ap[W[_], A, B](wf: W[A => B])(wa: W[A]): W[B] = ???

  def productWithApplicative[W[_], A, B](wa: W[A], wb: W[B])(implicit applicative: Applicative[W]): W[(A, B)] = {
    applicative.map(wa)(a => applicative.map(wb)(b => (a, b)))

    ???
  }

  def main(args: Array[String]): Unit = {}
}
