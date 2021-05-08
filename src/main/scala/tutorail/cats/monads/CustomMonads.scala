package tutorail.cats.monads

import cats.Monad //
import cats.instances.int._
import cats.syntax.applicative._ // pure
import cats.syntax.flatMap._ // flatMap
import cats.syntax.functor._ // map

import scala.annotation.tailrec

object CustomMonads {
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

  //TODO Either, List Monad

  def main(args: Array[String]): Unit = {
    val tree: Tree[Int] = Branch(Branch(Leaf(10), Leaf(12)), Branch(Leaf(20), Leaf(30)))
    println(tree)
    println(tree.flatMap(i => Branch(Leaf(i - 1), Leaf(i + 1))))
  }

}
