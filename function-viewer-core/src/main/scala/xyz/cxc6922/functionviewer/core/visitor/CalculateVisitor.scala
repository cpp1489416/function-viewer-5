package xyz.cxc6922.functionviewer.core.visitor

import xyz.cxc6922.functionviewer.core.ast._
import xyz.cxc6922.functionviewer.core.runtime.Environment

import scala.beans.BeanProperty

class CalculateVisitor(@BeanProperty var environment: Environment = new Environment()
                      ) extends Visitor[Double] {

  override def visit(node: Node): Double = {
    Double.PositiveInfinity
  }

  override def visit(node: ConstantNode): Double = {
    node.value
  }

  override def visit(node: VariableNode): Double = {
    environment.get(node.name) match {
      case Some(value) =>
        value match {
          case value: Double => value
          case value: Float => value.toDouble
          case value: Int => value.toDouble
          case value: Long => value.toDouble
          case _ => value.toString.toDouble
        }
      case None => Double.PositiveInfinity // alert(error)
    }
  }

  override def visit(node: NegativeNode): Double = {
    -1 * node.accept(this)
  }

  override def visit(node: PlusNode): Double = {
    node.left.accept(this) + node.right.accept(this)
  }

  override def visit(node: MinusNode): Double = {
    node.left.accept(this) - node.right.accept(this)
  }

  override def visit(node: MultiplyNode): Double = {
    node.left.accept(this) * node.right.accept(this)
  }

  override def visit(node: DivideNode): Double = {
    node.left.accept(this) / node.right.accept(this)
  }
}
