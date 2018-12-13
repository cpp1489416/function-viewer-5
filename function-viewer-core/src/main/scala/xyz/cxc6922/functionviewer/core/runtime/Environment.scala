package xyz.cxc6922.functionviewer.core.runtime

import scala.collection.mutable
import scala.collection.mutable.Stack

class Environment {
  var variablesStack: mutable.Stack[mutable.Map[String, Any]] = mutable.Stack(mutable.Map())

  def this(map: mutable.Map[String, Any]) = {
    this()
    variablesStack.push(map)
  }

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
