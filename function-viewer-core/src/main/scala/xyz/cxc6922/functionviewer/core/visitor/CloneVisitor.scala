package xyz.cxc6922.functionviewer.core.visitor

import xyz.cxc6922.functionviewer.core.ast._

class CloneVisitor extends Visitor[Node] {
  override def visit(node: Node): Node = {
    // alert error
    node
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
      node.left.accept(this),
      node.right.accept(this)
    )
  }

  override def visit(node: MinusNode): MinusNode = {
    new MinusNode(
      node.left.accept(this),
      node.right.accept(this)
    )
  }

  override def visit(node: MultiplyNode): MultiplyNode = {
    new MultiplyNode(
      node.left.accept(this),
      node.right.accept(this)
    )
  }

  override def visit(node: DivideNode): DivideNode = {
    new DivideNode(
      node.left.accept(this),
      node.right.accept(this)
    )
  }
}
