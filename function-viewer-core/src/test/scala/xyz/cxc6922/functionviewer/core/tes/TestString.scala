package xyz.cxc6922.functionviewer.core.tes

import xyz.cxc6922.functionviewer.core.node._
import xyz.cxc6922.functionviewer.core.visitors.StringerVisitor

object TestString {
  def main(args: Array[String]): Unit = {
    val node = new MultiplyNode(
      new PlusNode(
        new ConstantNode(12),
        new VariableNode("x")
      ),
      new ConstantNode(33)
    )

    val stringerVisitor = new StringerVisitor
    val str = node.accept(stringerVisitor)
    println(str)
  }

  def test(node: Node): Unit = {

  }

}
