package xyz.cxc6922.functionviewer.core.parsing

import scala.collection.mutable.ListBuffer

object FunctionLexer {

  object State extends Enumeration {
    type State = Value
    var Start: FunctionLexer.State.Value = Value
    val A1_Identifier: FunctionLexer.State.Value = Value
    val B1_Number: FunctionLexer.State.Value = Value
    val B2_Number: FunctionLexer.State.Value = Value
    val C1_Bracket: FunctionLexer.State.Value = Value
    val D1_Operator: FunctionLexer.State.Value = Value
    val End: FunctionLexer.State.Value = Value
  }

}

class FunctionLexer(var source: String) {
  private var tokenList = ListBuffer[Token]()
  private var state = FunctionLexer.State.Start
  private var lastIndex: Int = 0
  private var nextIndex: Int = 0
  private var lastRow: Int = 1
  private var lastColumn: Int = 1
  private var currentRow: Int = 1
  private var currentColumn: Int = 1

  private def lookAhead(): Char = {
    if (nextIndex >= source.length) {
      '\u0000'
    } else {
      source.charAt(nextIndex)
    }
  }

  private def lookCurrent(): Char = {
    if (nextIndex >= 0) source.charAt(nextIndex - 1) else '\u0000'
  }

  private def moveNext(): Char = {
    nextIndex += 1
    val curChar = lookAhead()
    if (curChar == '\n') { // nextLine
      currentRow += 1
      currentColumn = 1
    } else {
      currentColumn += 1
    }
    curChar
  }

  private def currentTokenSource(): String = {
    source.substring(lastIndex, nextIndex)
  }

  private def dropToken(): Unit = {
    lastIndex = nextIndex
    lastRow = currentRow
    lastColumn = currentRow
  }

  private def pushToken(token: Token): Token = {
    var ans = token.clone()
    ans.lineNumber = lastRow
    ans.columnNumber = lastColumn
    tokenList += ans
    lastIndex = nextIndex
    lastRow = currentRow
    lastColumn = currentColumn
    ans
  }

  private def isBracket(c: Char): Boolean = {
    c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}'
  }

  private def isLeftBracket(c: Char): Boolean = {
    isBracket(c) && (c == '(' || c == '[' || c == '{')
  }

  private def isRightBracket(c: Char): Boolean = {
    isBracket(c) && !isLeftBracket(c)
  }

  private def isOperator(c: Char): Boolean = {
    c == '+' || c == '-' || c == '*' || c == '/'
  }

  def genAll(): List[Token] = {
    tokenList = ListBuffer[Token]()
    state = FunctionLexer.State.Start
    lastIndex = 0
    nextIndex = 0
    lastRow = 1
    lastColumn = 1
    currentRow = 1
    currentColumn = 1
    epochStart()
    tokenList.toList
  }

  def epochStart(): Unit = {
    lookAhead() match {
      case c if c.isWhitespace =>
        moveNext()
        dropToken()
        state = FunctionLexer.State.Start
        epochStart()
      case c if c.isLetter =>
        moveNext()
        state = FunctionLexer.State.A1_Identifier
        epochIdentifier()
      case c if c.isDigit =>
        moveNext()
        state = FunctionLexer.State.B1_Number
        epochNumber()
      case c if c == '.' =>
        moveNext()
        state = FunctionLexer.State.B2_Number
        epochNumber()
      case c if isBracket(c) =>
        moveNext()
        state = FunctionLexer.State.C1_Bracket
        epochBracket()
      case c if isOperator(c) =>
        moveNext()
        state = FunctionLexer.State.D1_Operator
        epochOperator()
      case c if c == '\u0000' =>
        moveNext()
        state = FunctionLexer.State.End
        epochEnd()
      case _ =>
    }
  }

  def epochIdentifier(): Unit = {
    lookAhead() match {
      case c if c.isLetterOrDigit =>
        moveNext()
        epochIdentifier()
      case _ =>
        pushToken(new Token(currentTokenSource(), Token.Type.Identifier))
        state = FunctionLexer.State.Start
        epochStart()
    }
  }

  def epochNumber(): Unit = {
    state match {
      case FunctionLexer.State.B1_Number =>
        lookAhead() match {
          case c if c.isDigit =>
            moveNext()
            epochNumber()
          case c if c == '.' =>
            moveNext()
            state = FunctionLexer.State.B2_Number
            epochNumber()
          case _ =>
            pushToken(new Token(currentTokenSource(), Token.Type.Number, Token.SecondType.NumberLong))
            state = FunctionLexer.State.Start
            epochStart()
        }
      case FunctionLexer.State.B2_Number =>
        lookAhead() match {
          case c if c.isDigit =>
            moveNext()
            epochNumber()
          case _ =>
            pushToken(new Token(currentTokenSource(), Token.Type.Number, Token.SecondType.NumberDouble))
            state = FunctionLexer.State.Start
            epochStart()
        }
      case _ => // cannot happen
    }
  }

  def epochBracket(): Unit = {
    val secondType = if (isLeftBracket(lookCurrent())) {
      Token.SecondType.BracketLeft
    } else {
      Token.SecondType.BracketRight
    }
    pushToken(new Token(currentTokenSource(), Token.Type.Bracket, secondType))
    state = FunctionLexer.State.Start
    epochStart()
  }

  def epochOperator(): Unit = {
    pushToken(new Token(currentTokenSource(), Token.Type.Operator))
    state = FunctionLexer.State.Start
    epochStart()
  }

  def epochEnd(): Unit = {
    pushToken(Token.genEnd)
  }

}
