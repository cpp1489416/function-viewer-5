package xyz.cxc6922.functionviewer.core.parsing

import scala.beans.BeanProperty

object Token {

  object Type extends Enumeration {
    type Type = Value
    val End: Token.Type.Value = Value
    val Number: Token.Type.Value = Value
    val Identifier: Token.Type.Value = Value // a english identifier
    val Operator: Token.Type.Value = Value
    val Bracket: Token.Type.Value = Value
    val Unknown: Token.Type.Value = Value
  }

  object SecondType extends Enumeration {
    type SecondType = Value
    val None: Token.SecondType.Value = Value
    val NumberLong: Token.SecondType.Value = Value
    val NumberDouble: Token.SecondType.Value = Value
    val BracketLeft: Token.SecondType.Value = Value
    val BracketRight: Token.SecondType.Value = Value
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
    new Token("(End)", Type.End)
  }
}

class Token(@BeanProperty var source: String = "",
            @BeanProperty var sourceType: Token.Type.Value = Token.Type.Unknown,
            @BeanProperty var secondType: Token.SecondType.Value = Token.SecondType.None,
            @BeanProperty var lineNumber: Long = -1,
            @BeanProperty var columnNumber: Long = -1) extends Cloneable {
  override def clone(): Token = {
    new Token(source, sourceType, secondType, lineNumber, columnNumber)
  }

  override def toString: String = {
    "<" + source + ", " + sourceType + ", " + secondType + ", " + lineNumber + ", " + columnNumber + ">\n"
  }
}

