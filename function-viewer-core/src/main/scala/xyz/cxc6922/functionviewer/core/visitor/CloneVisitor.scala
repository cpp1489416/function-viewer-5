package xyz.cxc6922.functionviewer.core.visitor

import xyz.cxc6922.functionviewer.core.node._

class CloneVisitor extends Visitor {
  override def visit(node: Node): Unit = {
    // alert error
  }

  override def visit(node: ConstantNode): ConstantNode = {
    new ConstantNode(node.value, node.valueType)
  }

  override def visit(node: VariableNode): VariableNode = {
    new VariableNode(node.name)
  }

  override def visit(node: NegativeNode): NegativeNode = {
    new NegativeNode(node.child)
  }

  override def visit(node: PlusNode): PlusNode = {
    new PlusNode(
      node.left.accept(this).asInstanceOf[Node],
      node.right.accept(this).asInstanceOf[Node]
    )
  }

  override def visit(node: MinusNode): MinusNode = {
    new MinusNode(
      node.left.accept(this).asInstanceOf[Node],
      node.right.accept(this).asInstanceOf[Node]
    )
  }

  override def visit(node: MultiplyNode): MultiplyNode = {
    new MultiplyNode(
      node.left.accept(this).asInstanceOf[Node],
      node.right.accept(this).asInstanceOf[Node]
    )
  }

  override def visit(node: DivideNode): DivideNode = {
    new DivideNode(
      node.left.accept(this).asInstanceOf[Node],
      node.right.accept(this).asInstanceOf[Node]
    )
  }
}
