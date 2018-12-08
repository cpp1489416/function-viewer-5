package xyz.cxc6922.functionviewer.core.node

class VariableNode(val name: String) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

object ConstantNode {

  object Type extends Enumeration {
    val Common, Zero, One, NegativeOne, Infinite = Value
  }

}

class ConstantNode(val value: Double,
                   val valueType: ConstantNode.Type.Value = ConstantNode.Type.Common
                  ) extends Node {
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

  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

class NegativeNode(var child: Node) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

class PlusNode(var left: Node, var right: Node) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

class MinusNode(var left: Node, var right: Node) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

class MultiplyNode(val left: Node, val right: Node) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

class DivideNode(val left: Node, val right: Node) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}

class CosNode(val inside: Node) extends Node {
  override def accept(visitor: Visitor): Any = {
    visitor.visit(this)
  }
}