package xyz.cxc6922.functionviewer.core.tes.analyzer

import java.util.Scanner

import xyz.cxc6922.functionviewer.core.ast.Node
import xyz.cxc6922.functionviewer.core.parsing.{FunctionLexer, LL1FunctionAnalyzer}
import xyz.cxc6922.functionviewer.core.visitor.{ConstantSimplifyVisitor, DifferentiateVisitor, StringerVisitor}

object TestLexer {
  /**
    * 3 * 333333 / 3 + 3* (3 + d)
    * List(<3, Number, NumberLong>
    * , <*, Operator, None>
    * , <333333, Number, NumberLong>
    * , </, Operator, None>
    * , <3, Number, NumberLong>
    * , <+, Operator, None>
    * , <3, Number, NumberLong>
    * , <*, Operator, None>
    * , <(, Bracket, BracketLeft>
    * , <3, Number, NumberLong>
    * , <+, Operator, None>
    * , <d, Identifier, None>
    * , <), Bracket, BracketRight>
    * , <(End), End, None>
    * )
    * (((3.0(Common) * 333333.0(Common)) / 3.0(Common)) + (3.0(Common) * (3.0(Common) + d)))
    * simplified: (333333.0(Common) + (3.0(Common) * (3.0(Common) + d)))
    * differentiated: ((((((0.0(Zero) * 333333.0(Common)) + (3.0(Common) * 0.0(Zero))) * 3.0(Common)) - ((3.0(Common) * 333333.0(Common)) * 0.0(Zero))) / (3.0(Common) * 3.0(Common))) + ((0.0(Zero) * (3.0(Common) + d)) + (3.0(Common) * (0.0(Zero) + 1.0(One)))))
    * differentiate simplified: 3.0(Common)
    */

  def main(args: Array[String]): Unit = {
    val str = "a*3/3fdsfadfds83r3r33.3334234234/*f(3fafa/)3/dfadsf"
    var tokenList = new FunctionLexer(str).genAll()
    println(tokenList)
    var scanner = new Scanner(System.in)
    while (true) {
      val myStr = scanner.nextLine()
      tokenList = new FunctionLexer(myStr).genAll()
      println(tokenList)
      val node = new LL1FunctionAnalyzer(tokenList).parsePlus()
      println(node.accept(new StringerVisitor))
      println("simplified: " + node.accept(new ConstantSimplifyVisitor).accept(new StringerVisitor))
      println("differentiated: " + node.accept(new DifferentiateVisitor).accept(new StringerVisitor))
      println("differentiate simplified: " + node.accept(new DifferentiateVisitor).accept(new ConstantSimplifyVisitor)
        .accept(new StringerVisitor))
    }
  }
}
