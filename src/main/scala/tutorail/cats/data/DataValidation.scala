package tutorail.cats.data

import cats.Semigroup
import cats.data.Validated
import cats.instances.list._

object DataValidation {

  def validateEven(n: Int): Validated[List[String], Int] = Validated.cond(n % 2 == 0, n, List("n must be even"))

  def validatePositive(n: Int): Validated[List[String], Int] = Validated.cond(n >= 0, n, List("n must be positive"))

  def validateMax(n: Int): Validated[List[String], Int] = Validated.cond(n <= 100, n, List("n must be less than or equal to 100"))

  def isPrime(n: Int): Boolean = if (n == 2 || n == 1) true else (3 until n).exists(i => n % i != 0)

  def validatePrime(n: Int): Validated[List[String], Int] = Validated.cond(isPrime(n), n, List("n must be prime number"))

  /**
   * n must be positive
   * n must be <= 100
   * n must be prime
   * n must be even
   * @param n
   * @return
   */
  def testNumber(n: Int): Either[List[String], Int] = {
    implicit val intSemiGroup = Semigroup.instance((x: Int, y: Int) => x)

    validateEven(n).combine(validateMax(n)).combine(validatePrime(n)).combine(validatePositive(n)).toEither
  }

  object FormValidation {
    type FormValidation[T] = Validated[List[String], T]

    def validateForm(form: Map[String, String]): FormValidation[Unit] = {
      implicit val stringSemigroup = Semigroup.instance((x: Unit, y: Unit) => x)
      val nameValidated: Validated[List[String], Unit] = Validated.fromOption(form.get("name"), List("name must be specified")).map(_ => ())
      val passwordValidated: Validated[List[String], String] = Validated.fromOption(form.get("password"), List("password must be specified"))
      val passwordLengthValidated: Validated[List[String], Unit] = passwordValidated.andThen(password => Validated.cond(password.length >= 10, (), List("password length must be at least 10")))
      val emailExists: Validated[List[String], String] = Validated.fromOption(form.get("email"), List("email must be specified"))
      val emailValidated: Validated[List[String], Unit] = emailExists.andThen(email => Validated.cond(email.contains("@"), (), List("email should contain @")))

      nameValidated.combine(passwordLengthValidated).combine(emailValidated)
    }
  }

  def main(args: Array[String]): Unit = {
    println(testNumber(2))
    println(testNumber(34))

    val form = Map("name" -> "exists", "password" -> "1234567890", "email" -> "s@s.com")
    println(FormValidation.validateForm(form))
  }
}
