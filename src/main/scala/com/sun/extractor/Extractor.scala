package com.sun.extractor

object Extractor {

  def main(args: Array[String]): Unit = {

    "sun@jd" match {
      case Email("sun", "jd") => println("correct")
      case _ => println("wrong")
    }

    val s = "SIS"
    s match {
      case x @ UpperCaseEx() => println(x + " is upper case")
      case _ => println("not upper case")
    }

    val ExpandedEmail(user, topDomain, rest @ _*) = "sun@qq.com"
    println(user + topDomain)

    val Email(userName, domain) = "zink@163.com@jojo"
    println(userName + " " +domain)
  }
}

object UpperCaseEx {
  def unapply(arg: String): Boolean = {
    arg.toUpperCase() == arg
  }
}

object Email {

  def unapply(arg: String): Option[(String, String)] = {
    println("hello")
    val parts = arg.split("@")
    if(parts.length != 2) None
    else Some(parts(0), parts(1))
  }
}

object ExpandedEmail {

  def unapplySeq(arg: String): Option[(String, Seq[String])] = {
    val parts = arg.split("@")
    if(parts.length == 2) Some(parts(0), parts(1).split("\\.").reverse)
    else None
  }
}
