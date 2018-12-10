package xyz.cxc6922.functionviewer.core.ast

trait Visitor {

  def visit(node: Node): Any

  def visit(node: ConstantNode): Any

  def visit(node: VariableNode): Any

  def visit(node: NegativeNode): Any

  def visit(node: PlusNode): Any

  def visit(node: MinusNode): Any

  def visit(node: MultiplyNode): Any

  def visit(node: DivideNode): Any
}
