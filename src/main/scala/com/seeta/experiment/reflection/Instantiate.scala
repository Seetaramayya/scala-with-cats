package com.seeta.experiment.reflection

import com.seeta.Person

import scala.reflect.runtime.{universe => ru}
import scala.reflect.runtime.universe._

/**
  * In this example trying to instantiate `Person` type using reflection
  */

object Instantiate extends App {
  // This mirror makes available all classes and types that are loaded by current class loader
  val mirror: Mirror = ru.runtimeMirror(getClass.getClassLoader)

  val classPerson: ClassSymbol = ru.typeOf[Person].typeSymbol.asClass
  val classMirror: ClassMirror = mirror.reflectClass(classPerson)

  val constructor: MethodSymbol = ru.typeOf[Person].decl(ru.termNames.CONSTRUCTOR).asMethod
  val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructor)

  val seeta: Any = constructorMirror("Seeta", 36, "brown")
  println(seeta)
}
