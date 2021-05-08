package tutorail.cats.semigroup

import cats.Semigroup // Typeclass definition
import cats.instances.int._ //Typeclass instances for int type
import cats.instances.string._ //Typeclass instances for string type
import cats.instances.option._ //Typeclass instances for option type
import cats.syntax.semigroup._ //Semigroup extension methods are brought into the scope such as combine |+|
import tutorail.cats.Expense
import tutorail.cats.Data

/**
 * A semigroup is any set `A` with an __associative__ operation (`combine`).
 *
 * definition is something like this
 * {{{
 *   trait Semigroup[A] {
 *    def combine(a: A, b: A)
 *   }
 * }}}
 *
 * More about semigroups, See [[https://typelevel.org/cats/typeclasses/semigroup.html]]
 */
object Semigroups extends App {
  // this will not compile without int type class instances import
  val naturalCombinationForInts = Semigroup[Int].combine(1, 2) // natural combination for ints is sum

  // this will not compile without string type class instances import
  val naturalCombinationForStrings = Semigroup[String].combine("a", "b") //natural combination for the strings is concatenation

//  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce((x, y) => semigroup.combine(x, y))
  // Exercise 2: Implement above reduceThing with |+| instead of semigroup
  def reduceThings[T: Semigroup](list: List[T]): T = list.reduce((x, y) => x |+| y)

  val numbers = (1 to 10).toList
  val optionalNumbers: List[Option[Int]] = numbers.map(Option.apply)
  val naturalCombinationForListOfInts = reduceThings(numbers)
  val naturalCombinationForListOfStrings = reduceThings(List("I", " like", " scala"))

  println(naturalCombinationForListOfInts)
  println(naturalCombinationForListOfStrings)

  println(reduceThings(optionalNumbers)) // this will not compile without option type class instances import
  println(Semigroup[Option[String]].combine(Option("One"), Option("Two")))
  println(Semigroup[Option[String]].combine(Option("One"), Option.empty[String]))
  println(Semigroup[Option[String]].combine(Option.empty[String], Option("Two")))
  println(Semigroup[Option[String]].combine(Option.empty[String], Option.empty[String]))

  // Exercise 1: Support new type: Create new instance type for the Expense case class
  implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] { (a: Expense, b: Expense) =>
    Expense(Math.max(a.id, b.id), a.amount + b.amount)
  }

  println(reduceThings(Data.expenses))
  println(reduceThings(Data.expenses.map(Option.apply)))

  // extension methods from Semigroup - |+| read as combine

  println("=" * 60)
  println("Semigroup extension methods are brought into scope as well")
  println("=" * 60)
  println(1 |+| 2) // expected: 3
  println("1" |+| "2") // expected: 12
  println(Option("One") |+| Option("Two")) // expected : Some(OneTwo)
  println(Option.empty[String] |+| Option("Two")) // expected : Some(Two)
  println(Option("One") |+| Option.empty[String]) // expected : Some(One)
  println(Option.empty[String] |+| Option.empty[String]) // expected : None

  println(Option(1) |+| Option(2)) //expected: Some(3)
}
