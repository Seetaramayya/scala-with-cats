package tutorail.cats.monoids

import cats.Semigroup._
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.instances.map._
import cats.Monoid
import cats.syntax.monoid._
import tutorail.cats.{ Data, Expense } // extension methods

// All the above imports can be replaced with import cats._ & import cats.implicits._

/**
 * A monoid is a semigroup with an identity. A monoid is a specialization of a semigroup,
 * so
 *   1. `combine` operation must be associative
 *   1. additionally it should satisfy this `combine(x, empty) == combine(empty, x) == x`.
 *
 * Definition is something like this
 * {{{
 *   trait Monoid[A] extends Semigroup[A] {
 *    def empty: A
 *   }
 * }}}
 *
 * More about monoids, See [[https://typelevel.org/cats/typeclasses/monoid.html]]
 */
object Monoids extends App {
  //Exercise 1: implement combine using fold left
  def combineFoldLeft[T](list: List[T])(implicit m: Monoid[T]): T = list.foldLeft(m.empty)(_ |+| _)

  println(combineFoldLeft(List(1, 2, 3)))
  println(combineFoldLeft(List("1", "2", "3")))
  println(combineFoldLeft(List(Option("1"), Option("2"), Option("3"))))

  implicit val expenseMonoid: Monoid[Expense] =
    Monoid.instance[Expense](Expense(Long.MinValue, 0), (a: Expense, b: Expense) => Expense(Math.max(a.id, b.id), a.amount + b.amount))

  println(combineFoldLeft(Data.expenses))

  println(Map("seeta" -> 1) |+| Map("vadali" -> 2, "seeta" -> 2) |+| Map("ramayya" -> 5))
  println(combineFoldLeft(Data.phonebooks))

  // Exercise 2: Shopping cart online
  case class ShoppingCart(items: List[String], total: Double)
  object ShoppingCart {
    val empty: ShoppingCart = ShoppingCart(List(), 0.0)
    def combine(a: ShoppingCart, b: ShoppingCart): ShoppingCart = ShoppingCart(a.items ++ b.items, a.total + b.total)
  }
  implicit val shoppingCartMonoid: Monoid[ShoppingCart] = Monoid.instance[ShoppingCart](ShoppingCart.empty, ShoppingCart.combine)
  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart = combineFoldLeft(shoppingCarts)

  println(checkout(List(ShoppingCart(List("books", "pens"), 10), ShoppingCart(List("keyboard", "headphones", "speakers"), 876))))
}
