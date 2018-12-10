package xyz.cxc6922.functionviewer.core.visitor

import xyz.cxc6922.functionviewer.core.ast._

class StringerVisitor extends Visitor {
  var string = ""

  override def visit(node: Node): String = {
    "please implicit the node name : " + node.getClass.getName
  }

  override def visit(node: ConstantNode): String = node.valueType match {
    case ConstantNode.Type.Common => node.value.toString + "(Common)"
    case ConstantNode.Type.One => node.value.toString + "(One)"
    case ConstantNode.Type.NegativeOne => node.value.toString + "(-One)"
    case ConstantNode.Type.Zero => node.value.toString + "(Zero)"
    case ConstantNode.Type.Infinite => node.value.toString + "(Infinite)"
    case _ => node.value.toString + "(Unknown)"
  }

  override def visit(node: VariableNode): String = {
    node.name
  }

  override def visit(node: NegativeNode): String = {
    "-(" + node.child.accept(this) + ")"
  }

  override def visit(node: PlusNode): String = {
    "(" + node.left.accept(this) + " + " + node.right.accept(this) + ")"
  }

  override def visit(node: MinusNode): String = {
    "(" + node.left.accept(this) + " - " + node.right.accept(this) + ")"
  }

  override def visit(node: MultiplyNode): String = {
    "(" + node.left.accept(this) + " * " + node.right.accept(this) + ")"
  }

  override def visit(node: DivideNode): String = {
    "(" + node.left.accept(this) + " / " + node.right.accept(this) + ")"
  }
}
