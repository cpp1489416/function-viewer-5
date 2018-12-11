package xyz.cxc6922.functionviewer.core.tes.visitor

import xyz.cxc6922.functionviewer.core.ast._
import xyz.cxc6922.functionviewer.core.visitor.{DifferentiateVisitor, StringerVisitor}

object TestDifferentiate {
  def main(args: Array[String]): Unit = {
    var node: Node = new MultiplyNode(
      new DivideNode(
        new ConstantNode(12),
        new VariableNode("x")
      ),
      new ConstantNode(33)
    )
    node.asInstanceOf[MultiplyDivideNode].left = new NegativeNode(new ConstantNode(-1))
    node = gen1()
    println("origin: " + node.accept(new StringerVisitor))

    val differentiateVisitor = new DifferentiateVisitor(Set("x"))
    val difNode: Node = node.accept(differentiateVisitor).asInstanceOf[Node]

    val str = difNode.accept(new StringerVisitor())
    println("differentiated: " + str)
  }

  def gen1(): Node = {
    new MultiplyNode(
      new DivideNode(
        new ConstantNode(12),
        new VariableNode("x")
      ),
      new ConstantNode(33)
    )
  }
}
