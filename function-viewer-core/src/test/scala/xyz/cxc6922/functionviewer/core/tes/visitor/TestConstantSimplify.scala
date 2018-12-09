package xyz.cxc6922.functionviewer.core.tes.visitor

import xyz.cxc6922.functionviewer.core.node._
import xyz.cxc6922.functionviewer.core.visitor.{ConstantSimplifyVisitor, DifferentiateVisitor, StringerVisitor}

object TestConstantSimplify {
  val stringerVisitor = new StringerVisitor
  val constantSimplifyVisitor = new ConstantSimplifyVisitor
  val differentiateVisitor = new DifferentiateVisitor
  val testDifferentiate = TestDifferentiate

  def main(args: Array[String]): Unit = {
    // 1 + -1
    var node: Node = new PlusNode(
      new ConstantNode(ConstantNode.Type.One),
      new NegativeNode(
        new ConstantNode(ConstantNode.Type.One)
      )
    )
    node.asInstanceOf[PlusNode].right = new ConstantNode(-134321234)
    test(node)

    // 1 + -0
    node = new PlusNode(
      new ConstantNode(ConstantNode.Type.One),
      new NegativeNode(
        new ConstantNode(ConstantNode.Type.Zero)
      )
    )
    test(node)

    // -1 + 1
    node = new PlusNode(
      new NegativeNode(new ConstantNode(ConstantNode.Type.One)),
      new ConstantNode(ConstantNode.Type.One)
    )
    test(node)

    // -0
    node = new NegativeNode(
      new ConstantNode(ConstantNode.Type.Zero)
    )
    test(node)

    // -1 + -1
    node = new PlusNode(
      new NegativeNode(new ConstantNode(ConstantNode.Type.One)),
      new NegativeNode(new ConstantNode(ConstantNode.Type.One))
    )
    test(node)

    // -1 - -1
    node = new MinusNode(
      new NegativeNode(new ConstantNode(ConstantNode.Type.One)),
      new NegativeNode(new ConstantNode(ConstantNode.Type.One))
    )
    test(node)

    // -1 - -9
    node = new MinusNode(
      new NegativeNode(new ConstantNode(ConstantNode.Type.One)),
      new NegativeNode(new ConstantNode(-9))
    )
    test(node)

    // 0 - 1
    node = new MinusNode(
      new ConstantNode(ConstantNode.Type.Zero),
      new ConstantNode(ConstantNode.Type.One)
    )
    test(node)

    // x / 2
    node = new DivideNode(
      new VariableNode("x"),
      new ConstantNode(2)
    )
    test(node)

    println("before differentiate: " + testDifferentiate.gen1().accept(stringerVisitor))
    node = testDifferentiate.gen1().accept(differentiateVisitor).asInstanceOf[Node]
    test(node)
  }

  def test(node: Node): Unit = {
    println("original: " + node.accept(stringerVisitor))
    val newNode = node.accept(constantSimplifyVisitor).asInstanceOf[Node]
    println("simplified: " + newNode.accept(stringerVisitor))
  }
}
