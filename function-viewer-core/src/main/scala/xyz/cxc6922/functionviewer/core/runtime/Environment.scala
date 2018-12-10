package xyz.cxc6922.functionviewer.core.runtime

import scala.collection.mutable
import scala.collection.mutable.Stack

class Environment {
  var variablesStack: mutable.Stack[mutable.Map[String, Any]] = mutable.Stack(mutable.Map())

  def get(name: String): Option[Any] = {
    val itr = variablesStack.iterator
    while (itr.hasNext) {
      val map = itr.next
      map.get(name) match {
        case Some(value) =>
          return Some(value)
        case None =>
      }
    }
    None
  }

  def contains(name: String): Boolean = {
    get(name).isDefined
  }

  def put(name: String, value: Any): Unit = {
    variablesStack.top += (name -> value)
  }

  def pushStack(): Unit = {
    variablesStack.push(mutable.Map())
  }

  def popStack(): Unit = {
    if (variablesStack.size > 1) {
      variablesStack.pop()
    }
  }

}

object Environment {
  def main(args: Array[String]): Unit = {
    val e = new Environment
    e.put("a", "1")
    e.put("b", "1")
    println(e.get("a"))
    e.pushStack()
    e.put("a", "2")
    println(e.get("a"))
    println(e.get("fdsafsadfsdafdasf"))
    e.popStack()
    println(e.get("a"))
    e.popStack()
    println(e.get("e"))
  }
}
