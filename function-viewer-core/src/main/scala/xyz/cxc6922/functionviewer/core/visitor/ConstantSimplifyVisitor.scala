package xyz.cxc6922.functionviewer.core.visitor

import xyz.cxc6922.functionviewer.core.node._

// this version cannot check constant infinite
class ConstantSimplifyVisitor extends Visitor {
  private val cloneVisitor = new CloneVisitor

  private def cloneNode(node: Node): Node = {
    node.accept(cloneVisitor).asInstanceOf[Node]
  }

  private def isConstant(node: Node): Boolean = {
    node.isInstanceOf[ConstantNode]
  }

  private def isNegative(node: Node): Boolean = {
    node.isInstanceOf[NegativeNode]
  }

  private def genNegativeNode(originNode: Node): Node = {
    originNode match {
      case originNode: NegativeNode =>
        return cloneNode(originNode.child)
    }
    new NegativeNode(originNode)
  }

  override def visit(node: Node): Node = {
    // error
    cloneNode(node)
  }

  override def visit(node: ConstantNode): Node = {
    cloneNode(node)
  }

  override def visit(node: VariableNode): Node = {
    cloneNode(node)
  }

  override def visit(node: NegativeNode): Node = {
    val child = constantSimplify(node.child)
    child match {
      case child: ConstantNode => // child is constant
        child.valueType match {
          case ConstantNode.Type.Zero =>
            return new ConstantNode(ConstantNode.Type.Zero)
          case ConstantNode.Type.One =>
            return new ConstantNode(ConstantNode.Type.NegativeOne)
          case ConstantNode.Type.NegativeOne =>
            return new ConstantNode(ConstantNode.Type.One)
          case ConstantNode.Type.Common =>
            return new ConstantNode(-1 * child.value)
          case _ =>
        }
    }

    new NegativeNode(child) // cannot simplify
  }

  override def visit(node: PlusNode): Node = {
    val left = constantSimplify(node.left)
    val right = constantSimplify(node.right)
    simplifyPlus(left, right)
  }

  override def visit(node: MinusNode): Node = {
    val left = constantSimplify(node.left)
    val right = constantSimplify(node.right)
    simplifyMinus(left, right)
  }

  override def visit(node: MultiplyNode): Node = {
    val left = constantSimplify(node.left)
    val right = constantSimplify(node.right)

    left match {
      case left: ConstantNode => // left is constant
        if (left.valueType == ConstantNode.Type.Zero) { // 0 * any == 0
          return left // left == 0
        } else if (left.valueType == ConstantNode.Type.One) { // 1 * any == 1
          return right
        }

        right match {
          case right: ConstantNode =>
            if (right.valueType == ConstantNode.Type.Zero) { // constant * 0 = constant
              return right
            } else if (right.valueType == ConstantNode.Type.One) { // constant * 1 = constant
              return left
            } else if (right.valueType == ConstantNode.Type.NegativeOne &&
              left.valueType == ConstantNode.Type.NegativeOne) { // -1 * -1 = 1
              return new ConstantNode(ConstantNode.Type.One)
            } else {
              return new ConstantNode(left.value * right.value)
            }
          case _ =>
        }

        if (left.valueType == ConstantNode.Type.NegativeOne) { // -1 * val = -val
          return genNegativeNode(right) // risk: -(-val)
        }
      case _ =>
    }

    right match {
      case right: ConstantNode =>
        if (right.valueType == ConstantNode.Type.Zero) { // any * 0 == 0
          return right
        } else if (right.valueType == ConstantNode.Type.One) { // any * 1 == any
          return left
        } else if (right.valueType == ConstantNode.Type.NegativeOne) { // val * -1 == -val
          return genNegativeNode(left) // risk: -(-val)
        }
      case _ =>
    }

    new MultiplyNode(left, right) // cannot simplify
  }

  override def visit(node: DivideNode): Node = {
    val left = constantSimplify(node.left)
    val right = constantSimplify(node.right)

    left match {
      case left: ConstantNode =>
        if (left.valueType == ConstantNode.Type.Zero) {
          return left // 0 / val = 0
        }

        right match {
          case right: ConstantNode =>
            if (left.valueType == ConstantNode.Type.One &&
              right.valueType == ConstantNode.Type.NegativeOne) {
              return left // 1 / 1 = 1
            } else if (left.valueType == ConstantNode.Type.One &&
              right.valueType == ConstantNode.Type.NegativeOne) {
              return right // 1 / -1 = -1
            } else if (left.valueType == ConstantNode.Type.NegativeOne &&
              right.valueType == ConstantNode.Type.One) {
              return left // -1 / 1 = -1
            } else if (left.valueType == ConstantNode.Type.NegativeOne &&
              right.valueType == ConstantNode.Type.NegativeOne) {
              return new ConstantNode(ConstantNode.Type.One) // -1 / -1 = 1
            } else {
              return new ConstantNode(left.value / right.value) // constant / constant
            }

          case _ =>
        }
      case _ =>
    }

    right match {
      case right: ConstantNode =>
        if (right.valueType == ConstantNode.Type.One) {
          return left // val / 1 = val
        } else if (right.valueType == ConstantNode.Type.NegativeOne) {
          return genNegativeNode(left) // val / -1 = -val
        } else { // val / constant = (1/constant) * val
          return new MultiplyNode(new ConstantNode(1.0 / right.value), left)
        }
      case _ =>
    }

    new DivideNode(left, right) // cannot simplify
  }

  def constantSimplify(node: Node): Node = {
    node.accept(this).asInstanceOf[Node]
  }

  def simplifyPlus(left: Node, right: Node): Node = {
    left match {
      case left: NegativeNode => // left node is negative
        return simplifyMinus(right, left.child)

      case left: ConstantNode => // left node is constant
        if (left.valueType == ConstantNode.Type.Zero) {
          return right // left constant node is 0
        }

        right match {
          case right: ConstantNode => // left is constant and right is constant
            if (right.valueType == ConstantNode.Type.Zero) {
              return left // right == 0, no need to calculate again and left could == 1
            } else if (left.valueType == ConstantNode.Type.One && right.valueType == ConstantNode.Type.NegativeOne ||
              left.valueType == ConstantNode.Type.NegativeOne && right.valueType == ConstantNode.Type.One) {
              return new ConstantNode(ConstantNode.Type.Zero) // -1 + 1 || 1 + -1
            } else {
              return new ConstantNode(left.value + right.value)
            }
          case _ =>
        }

      case _ =>
    }

    right match {
      case right: NegativeNode =>
        return simplifyMinus(left, right.child)

      case right: ConstantNode => // right is constant
        if (right.valueType == ConstantNode.Type.Zero) {
          return left
        }

      case _ =>
    }

    new PlusNode(left, right) // cannot simplify
  }

  def simplifyMinus(left: Node, right: Node): Node = {
    right match {
      case right: NegativeNode =>
        return simplifyPlus(left, right.child) // no way like -1 - (-1)
      case _ =>
    }

    left match {
      case left: ConstantNode => // left node is constant
        right match {
          case right: ConstantNode => // left is constant and right is constant
            if (left.valueType == ConstantNode.Type.Zero) {
              // 0 - -1 || 0 - 1
              if (right.valueType == ConstantNode.Type.One) {
                return new ConstantNode(ConstantNode.Type.NegativeOne) // 0 - 1 == -1
              } else if (right.valueType == ConstantNode.Type.NegativeOne) {
                return new ConstantNode(ConstantNode.Type.One) // 0 - -1 == 1
              }
            }

            if (right.valueType == ConstantNode.Type.Zero) {
              return left // right == 0, no need to calculate again and left could == 1
            } else if (left.valueType == ConstantNode.Type.One && right.valueType == ConstantNode.Type.One ||
              left.valueType == ConstantNode.Type.NegativeOne && right.valueType == ConstantNode.Type.NegativeOne) {
              return new ConstantNode(ConstantNode.Type.Zero) // -1 + 1 = 0
            } else {
              return new ConstantNode(left.value - right.value)
            }
          case _ =>
        }

        if (left.valueType == ConstantNode.Type.Zero) {
          // 0 - val == -val
          return genNegativeNode(right)
        }

      case _ =>
    }

    right match {
      case right: ConstantNode => // right is constant
        if (right.valueType == ConstantNode.Type.Zero) {
          return left
        }
      case _ =>
    }

    new MinusNode(left, right) // cannot simplify
  }
}
