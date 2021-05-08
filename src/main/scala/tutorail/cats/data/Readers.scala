package tutorail.cats.data

import cats.data.Reader

object Readers {
  case class Configuration(dbUser: String, dbPass: String, host: String, port: Int, nThreads: Int, replyTo: String)
  case class DBConnection(dbUser: String, dbPass: String) {
    def getOrderStatus(orderId: Long): String = "Shipped"
    def getLastOrderId(userName: String): Long = 123456789
  }
  case class HttpService(host: String, port: Int) {
    def start(): Unit = println(s"Started service on $host:$port")
  }
  case class EmailService(replyTo: String) {
    def sendEmail(To: String, content: String): String = s" From: $replyTo, \n To: $To, \n Body: $content"
  }

  def getOrderStatus(userName: String): Reader[Configuration, String] = {
    val dbConnectionReader: Reader[Configuration, DBConnection] = Reader(config => DBConnection(config.dbUser, config.dbPass))
    for {
      lastOrderId <- dbConnectionReader.map(_.getLastOrderId(userName))
      lastOrderStatus <- dbConnectionReader.map(_.getOrderStatus(lastOrderId))
    } yield lastOrderStatus
  }

  def notifyLastOrderStatus(userName: String, to: String): Reader[Configuration, String] = {
    val emailServiceReader: Reader[Configuration, EmailService] = Reader(config => EmailService(config.replyTo))
    val lastOrderStatusReader: Reader[Configuration, String] = getOrderStatus(userName)
    // lastOrderStatusReader.flatMap { status => emailServiceReader.map(_.sendEmail(to, s"Your order has the status: $status")) }
    for {
      orderStatus <- lastOrderStatusReader
      email <- emailServiceReader.map(_.sendEmail(to, s"Your order has the status: $orderStatus"))
    } yield email

  }

  val config = Configuration("dbuser", "dbpass", "localhost", 8080, 8, "cats@scala.com")
  def main(args: Array[String]): Unit = {
    println(notifyLastOrderStatus("someUser", "to@to.com").run(config))
  }
}
