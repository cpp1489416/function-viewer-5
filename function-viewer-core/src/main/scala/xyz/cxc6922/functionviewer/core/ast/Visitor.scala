package xyz.cxc6922.functionviewer.core.ast

trait Visitor[T] {

  def visit(node: Node): T

  def visit(node: ConstantNode): T

  def visit(node: VariableNode): T

  def visit(node: NegativeNode): T

  def visit(node: PlusNode): T

  def visit(node: MinusNode): T

  def visit(node: MultiplyNode): T

  def visit(node: DivideNode): T
}
