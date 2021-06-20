package tutorail.cats.eval

import cats.Eval

/**
 * Eval comes in 3 flavours
 *   - Eval.now which evaluates immediately
 *   - Eval.later which evaluates later point (lazily when asked) and memorize the result (call by name parameter : =>)
 *   - Eval.always which evaluates later point when ever asked, it is not memorize the result. (call by name parameter : =>)
 *
 * Eval has map and flatMap so, it can be used in for comprehension.
 */
object Evaluation {

  val instantly: Eval[Int] = Eval.now {
    println("now")
    1
  }

  val later: Eval[Int] = Eval.later {
    println("later")
    2
  }

  val always: Eval[Int] = Eval.always {
    println("always")
    3
  }

  def defer[T](eval: => Eval[T]): Eval[T] = Eval.later(()).flatMap(_ => eval)

  def reverseList[T](list: List[T]): List[T] = {
    if (list.isEmpty) Nil
    else reverseList(list.tail) :+ list.head
  }

  def reverseEval[T](list: List[T]): Eval[List[T]] = {
    if (list.isEmpty) Eval.later(list)
    else Eval.later(()).flatMap(_ => reverseEval(list.tail).map(_ :+ list.head))
  }

  def main(args: Array[String]): Unit = {
    println(reverseList(List(1, 2, 3)))
    println(reverseEval((1 to 10000).toList).value)
  }
}
