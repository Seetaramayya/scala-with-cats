package tutorail.cats.data

import cats.data.Validated

object FormValidationTest {

  type FormValidation[T] = Validated[List[String], T]

  def validateForm(form: Map[String, String]): FormValidation[String] = {
    def existsCheck(key: String): Validated[List[String], String] = Validated.fromOption(form.get(key), List(s"$key is not exists"))

    def nameMustNotBeBlank(name: String): Validated[List[String], String] = Validated.cond(name == "", name, List("name field should not be blank"))

    def emailCheck(email: String): Validated[List[String], String] = Validated.cond(email.contains('@'), email, List("email is not valid"))

    def passwordCheck(password: String): Validated[List[String], String] = Validated.cond(password.length >= 10, password, List("Password must be >= 10"))

    for {
      name <- existsCheck("name").toEither
      email <- existsCheck("email").toEither
      password <- existsCheck("password").toEither
    } existsCheck("name").andThen(name => nameMustNotBeBlank(name))
    existsCheck("email").andThen(email => emailCheck(email))
    existsCheck("password").andThen(password => passwordCheck(password))
  }
  def main(args: Array[String]): Unit = {}
}
