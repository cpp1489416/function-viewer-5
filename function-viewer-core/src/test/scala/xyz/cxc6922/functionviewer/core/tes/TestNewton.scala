package xyz.cxc6922.functionviewer.core.tes

import xyz.cxc6922.functionviewer.core.ast.Node
import xyz.cxc6922.functionviewer.core.newton.SubNewtonObject
import xyz.cxc6922.functionviewer.core.runtime.Environment
import xyz.cxc6922.functionviewer.core.util.AstUtil
import xyz.cxc6922.functionviewer.core.visitor.StringerVisitor

import scala.io.StdIn

object TestNewton {
  def main(args: Array[String]): Unit = {
    val node : Node = AstUtil.genNode(" x* x + y * y - 4")
    val environment : Environment = new Environment()
    environment.put("x", 0.0)
    var newton :SubNewtonObject = new SubNewtonObject(node, environment, Set("y"))
    newton.times = 10
    println(newton.differentiatedNode.accept(new StringerVisitor))
    while (true) {
      val y = StdIn.readDouble()
      environment.put("y", y)
      val ans = newton.newton
      println(ans)
    }
  }

}
