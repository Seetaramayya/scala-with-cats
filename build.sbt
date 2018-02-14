name := "scala-with-cats"

version := "1.0"

scalaVersion := "2.12.4"
val akkaVersion = "2.5.9"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq (
  "org.typelevel"          %% "cats-core"                 % "1.0.0",
  "com.typesafe.akka"      %% "akka-stream"               % akkaVersion,
  "com.typesafe.akka"      %% "akka-stream-testkit"       % akkaVersion         % Test,
  "org.scalatest"          %% "scalatest"                 % "3.0.1"         % Test
)
    