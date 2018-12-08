package xyz.cxc6922.functionviewer.core.tes

import xyz.cxc6922.functionviewer.core.node._
import xyz.cxc6922.functionviewer.core.visitors.{DifferentiateVisitor, StringerVisitor}

object TestDifferentiate {
  def main(args: Array[String]): Unit = {
    val node = new MultiplyNode(
      new DivideNode(
        new ConstantNode(12),
        new VariableNode("x")
      ),
      new ConstantNode(33)
    )
    node.left = new  NegativeNode(new ConstantNode(-1));
    println("origin: " + node.accept(new StringerVisitor))

    val differentiateVisitor = new DifferentiateVisitor
    val difNode : Node = node.accept(differentiateVisitor).asInstanceOf[Node]

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
