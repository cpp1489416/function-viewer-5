package xyz.cxc6922.functionviewer.core.newton

import xyz.cxc6922.functionviewer.core.ast.{ConstantNode, Node}
import xyz.cxc6922.functionviewer.core.runtime.Environment
import xyz.cxc6922.functionviewer.core.visitor.{CalculateVisitor, ConstantSimplifyVisitor, DifferentiateVisitor}

import scala.beans.BeanProperty

class SubNewtonObject(@BeanProperty var node: Node = new ConstantNode(0),
                      @BeanProperty var environment: Environment = new Environment(),
                      @BeanProperty var toDifferentiate: Set[String] = Set(),
                      @BeanProperty var times: Int = 50,
                      @BeanProperty var minimumInfinity: Double = 1e-5,
                      @BeanProperty var maximumAcceptable: Double = 0.1) {

  var differentiatedNode: Node = _
  var differentiateVisitor: DifferentiateVisitor = new DifferentiateVisitor()
  var constantSimplifyVisitor: ConstantSimplifyVisitor = new ConstantSimplifyVisitor()
  var calculateVisitor: CalculateVisitor = new CalculateVisitor()

  updateDifferentiateNode()

  def updateDifferentiateNode(): Unit = {
    calculateVisitor.environment = environment
    differentiateVisitor.toDifferentiate = toDifferentiate
    differentiatedNode = node.accept(differentiateVisitor).accept(constantSimplifyVisitor)
  }

  def pointVisible: Boolean = {
    var ans: Double = environment.get(toDifferentiate.head).asInstanceOf[Double]
    val origin = ans
    for (_ <- 1 to times) {
      val d: Double = differentiatedNode.accept(calculateVisitor)
      val f: Double = node.accept(calculateVisitor)
      if (math.abs(f) < minimumInfinity) {
        environment.put(toDifferentiate.head, origin)
        return true
      }
      if (math.abs(d) < minimumInfinity || math.abs(ans - origin) > maximumAcceptable) {
        environment.put(toDifferentiate.head, origin)
        return false
      }
      environment.put(toDifferentiate.head, ans)
      ans -= f / d
    }
    environment.put(toDifferentiate.head, origin)
    false
  }

  def newton: Double = {
    var ans: Double = environment.get(toDifferentiate.head).get.asInstanceOf[Double]
    val origin = ans
    for (_ <- 1 to times) {
      val d: Double = differentiatedNode.accept(calculateVisitor)
      val f: Double = node.accept(calculateVisitor)
      ans -= f / d
      environment.put(toDifferentiate.head, ans)
    }
    environment.put(toDifferentiate.head, origin)
    ans
  }

}
