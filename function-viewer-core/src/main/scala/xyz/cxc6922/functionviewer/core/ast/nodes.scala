package xyz.cxc6922.functionviewer.core.ast

import scala.beans.BeanProperty

trait UnaryNode extends Node {}

trait BinaryNode extends Node {
  var left: Node
  var right: Node
}

trait PlusMinusNode extends BinaryNode {}

trait MultiplyDivideNode extends BinaryNode {}

class VariableNode(val name: String) extends UnaryNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

object ConstantNode {

  object Type extends Enumeration {
    type Type = Value
    val Common, Zero, One, NegativeOne, Infinite = Value
  }

}

class ConstantNode(@BeanProperty var value: Double,
                   @BeanProperty var valueType: ConstantNode.Type.Value = ConstantNode.Type.Common
                  ) extends UnaryNode {
  def this(valueType: ConstantNode.Type.Value) {
    this(
      valueType match {
        case ConstantNode.Type.Common => 0
        case ConstantNode.Type.Zero => 0
        case ConstantNode.Type.One => 1
        case ConstantNode.Type.NegativeOne => -1
        case ConstantNode.Type.Infinite => Double.PositiveInfinity
        case _ => 0
      },
      valueType
    )

  }

  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

class NegativeNode(@BeanProperty var child: Node) extends UnaryNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

class CosNode(@BeanProperty var child: Node) extends UnaryNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

class PlusNode(@BeanProperty var left: Node, @BeanProperty var right: Node) extends PlusMinusNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

class MinusNode(@BeanProperty var left: Node, @BeanProperty var right: Node) extends PlusMinusNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

class MultiplyNode(@BeanProperty var left: Node, @BeanProperty var right: Node) extends MultiplyDivideNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}

class DivideNode(@BeanProperty var left: Node, @BeanProperty var right: Node) extends MultiplyDivideNode {
  override def accept[T](visitor: Visitor[T]): T = {
    visitor.visit(this)
  }
}
