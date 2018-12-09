package xyz.cxc6922.functionviewer.core.parsing

import scala.beans.BeanProperty

object Token {

  object Type extends Enumeration {
    type Type = Value
    val End = Value(0)
    val Number = Value(1)
    val Identifier = Value(2) // a english identifier
    val Operator = Value(3)
    val Bracket = Value(4)
    val Unknown = Value(5)
  }

  def fromSource(source: String): Token = {
    source match {
      case "+" | "-" | "*" | "/" => new Token(source, Type.Operator)
      case "(" | ")" => new Token(source, Type.Bracket)
      case _ =>
        try {
          source.toDouble
          new Token(source, Type.Number)
        } catch {
          case e: Exception =>
            new Token(source, Type.Identifier)
        }
    }
  }

  def genEnd: Token = {
    new Token("#", Type.End)
  }
}

class Token(@BeanProperty val source: String = "",
            @BeanProperty val sourceType: Token.Type.Value = Token.Type.Unknown,
            @BeanProperty val lineNumber: Long = -1,
            @BeanProperty val columnNumber: Long = -1) {
}
