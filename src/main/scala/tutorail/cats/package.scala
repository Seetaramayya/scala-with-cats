package tutorail

package object cats {
  case class Expense(id: Long, amount: Double)

  object Data {
    val expenses: List[Expense] = List(
      Expense(1, 20.1),
      Expense(2, 20.1),
      Expense(3, 10.0),
      Expense(4, 100.0)
    )

    val phonebooks: List[Map[String, Int]] = List(
      Map(
        "seeta" -> 123,
        "vadali" -> 213
      ),
      Map(
        "ramayya" -> 998,
        "vadali" -> 765
      ),
      Map(
        "sr" -> 420,
        "SR" -> 320
      )
    )
  }
}
