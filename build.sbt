name := "scala-with-cats"

version := "1.0"

scalaVersion := "2.12.4"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq (
  "org.typelevel"          %% "cats-core"                 % "1.0.0",
  "org.scalatest"          %% "scalatest"                 % "3.0.1"         % "test"
)
    