package tutorail.cats

import cats.Functor
import cats.instances.int._
import cats.instances.option._
import tutorail.cats.Tree.{ branch, leaf }
import tutorail.cats.functor.Functors

trait Tree[+T]
case class Leaf[+T](value: T) extends Tree[T]
case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

object Tree {
  def display[T](t: Tree[T]): Unit = t match {
    case Leaf(v) => println(s" $v ")
    case Branch(v, l, r) =>
      display(l)
      println(s" $v ")
      display(r)
  }

  def branch[T](value: T, left: Tree[T], right: Tree[T]): Tree[T] = Branch(value, left, right)
  def leaf[T](value: T): Tree[T] = Leaf(value)
}

object BinaryFunctorTree extends App {
  val tree: Tree[Int] = branch(10, branch(5, leaf(3), leaf(8)), branch(15, leaf(11), branch(20, leaf(18), leaf(25))))
  implicit object BinaryTreeFunctor extends Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(value)                => Leaf(f(value))
      case Branch(value, left, right) => Branch(f(value), map(left)(f), map(right)(f))
    }
  }

  Tree.display(tree)
  Tree.display(Functors.increment(tree))

}
