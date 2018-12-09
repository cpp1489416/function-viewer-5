package xyz.cxc6922.functionviewer.core.tes.analyzer

import xyz.cxc6922.functionviewer.core.parsing.{FunctionLexer, LL1FunctionAnalyzer, Token}
import xyz.cxc6922.functionviewer.core.visitor.StringerVisitor

object TestLL1 {

  def main(args: Array[String]): Unit = {
    val list = List(
      Token.fromSource("("),
      Token.fromSource("a"),
      Token.fromSource("+"),
      Token.fromSource("3"),
      Token.fromSource(")"),
      Token.fromSource("*"),
      Token.fromSource("4"),
      Token.fromSource("/"),
      Token.fromSource("ddd"),
      Token.fromSource("-"),
      Token.fromSource("3"),
      Token.genEnd
    )

    val analyzer = new LL1FunctionAnalyzer(list)

    val node = analyzer.parsePlus()
    val str = node.accept(new StringerVisitor)
    println(str)
  }
}
