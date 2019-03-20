package com.sun

object Main {


  implicit val personOrdering = new Ordering[Person]{
    override def compare(x: Person, y: Person): Int = {
      if(x.name == y.name) 0
      else if (x.name > y.name) 1
      else -1
    }
  }

  def implicity[T](implicit arg: T) = arg

  def maxList[T: Ordering](list: List[T]): T = {

    list match {
      case Nil =>
        throw new IllegalArgumentException("empty list")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList(rest)
        if (implicity[Ordering[T]].gt(x, maxRest)) x else maxRest
    }
  }

  def main(args: Array[String]): Unit = {
    val list = List(Person("sun"), Person("wang"))
    println(maxList(list))
  }

  case class Person(name: String)
}
