package xyz.cxc6922.functionviewer.core.tes.visitor

import xyz.cxc6922.functionviewer.core.ast.Node
import xyz.cxc6922.functionviewer.core.parsing.{FunctionLexer, LL1FunctionAnalyzer}
import xyz.cxc6922.functionviewer.core.runtime.Environment
import xyz.cxc6922.functionviewer.core.visitor.{CalculateVisitor, StringerVisitor}

import scala.collection.mutable

object TestCalculate {
  def main(args: Array[String]): Unit = {
    val source = "(x * y + z) / 8 * x"
    val environment = new Environment(mutable.Map("x" -> 5, "y" -> 9, "z" -> 3))
    val node : Node = new LL1FunctionAnalyzer(new FunctionLexer(source).genAll()).parsePlus()
    val stringer = node.accept(new StringerVisitor)
    println(stringer)
    val ans = node.accept(new CalculateVisitor(environment))
    println(ans)
  }
}
