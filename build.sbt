name := "scala-with-cats"

version := "1.0"

val catsVersion = "2.1.1"
val scalaTestVersion = "3.2.7"

scalaVersion := "2.13.3"


libraryDependencies ++= Seq (
"org.typelevel"          %% "cats-core"                 % catsVersion,
"org.scalatest"          %% "scalatest"                 % scalaTestVersion             % Test
)

scalacOptions ++= Seq("-language:higherKinds")
