package tutorail.cats.data

import cats.Eval
import cats.data.{IndexedStateT, State}

object FunctionalState {

  case class ShoppingCart(items: Vector[String], total: Double)
  def addToCart(item: String, price: Double): State[ShoppingCart, Double] = State { previousShoppingCart =>
    val currentTotal = previousShoppingCart.total + price
    (ShoppingCart(item +: previousShoppingCart.items, currentTotal), currentTotal)
  }

  def inspect[A, B](f: A => B): State[A, B] = State((a: A) => (a, f(a)))

  def get[A]: State[A, A] = State(current => (current, current))

  def set[A](value: A): State[A, Unit] = State(_ =>  (value, ()))

  def modify[A](f: A => A): State[A, Unit] = State(current => (f(current), ()))

  def main(args: Array[String]): Unit = {

    val result: State[ShoppingCart, Double] = for {
      _ <- addToCart("item1", 3.2)
      _ <- addToCart("item2", 3.3)
      item3 <- addToCart("item3", 3.4)
    } yield item3

    println(result.run(ShoppingCart(Vector(), 0.0)).value)
  }
}
