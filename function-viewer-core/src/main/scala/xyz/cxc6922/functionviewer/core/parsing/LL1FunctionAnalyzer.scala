package xyz.cxc6922.functionviewer.core.parsing

import java.util

import xyz.cxc6922.functionviewer.core.node._

class LL1FunctionAnalyzer(val source: List[Token]) {

  private var forward: BufferedIterator[Token] = source.iterator.buffered

  private def lookForward(): Token = {
    lookForward(100)
    lookForward(1)
  }

  private def lookForward(count: Int): Token = {
    val (newItr, tmpItr) = forward.duplicate
    forward = newItr.buffered
    for (i <- 1 until count - 1) {
      if (tmpItr.hasNext) {
        tmpItr.next()
      } else {
        return Token.genEnd
      }
    }
    if (tmpItr.hasNext) {
      tmpItr.next()
    } else {
      Token.genEnd
    }
  }

  private def moveNext(): Token = {
    forward.next()
  }

  private def throwException(token: Token): Nothing = {
    throw new Exception("parse error at " + token.lineNumber + ":" + token.columnNumber)
  }

  // plus = multiply plus2
  def parsePlus(): Node = {
    val left = parseMultiply()
    parsePlus2(left)
  }

  // plus2 = (+|-) multiply | plus2 (+|-) plus2 | null
  def parsePlus2(left: Node): Node = {
    var cur = left
    do {
      val token = lookForward()
      token.source match {
        case "+" =>
          moveNext()
          cur = new PlusNode(cur, parseMultiply())
        case "-" =>
          moveNext()
          cur = new MinusNode(cur, parseMultiply())
        case _ =>
          return cur
      }
    } while (true)

    null // cannot happen
  }

  // multiply = single multiply2
  def parseMultiply(): Node = {
    val left = parseSingle()
    parseMultiply2(left)
  }

  // multiply2 = (*|/) single | multiply2 (*|/) multiply2 | null
  def parseMultiply2(left: Node): Node = {
    var cur = left
    do {
      val token = lookForward()
      token.source match {
        case "*" =>
          moveNext()
          cur = new MultiplyNode(cur, parseSingle())
        case "/" =>
          moveNext()
          cur = new DivideNode(cur, parseSingle())
        case _ =>
          return cur
      }
    } while (true)

    null // cannot happen
  }

  // single = x | 1 | (plus)
  def parseSingle(): Node = {
    val token = lookForward()
    var ans: Node = null
    token.sourceType match {
      case Token.Type.Bracket =>
        moveNext() // (
        ans = parsePlus()
        moveNext() // )
      case Token.Type.Identifier =>
        ans = new VariableNode(token.source)
        moveNext()
      case Token.Type.Number =>
        ans = new ConstantNode(token.source.toDouble)
        moveNext()
      case Token.Type.End =>
      case _ =>
        throwException(token)
    }

    ans
  }
}
