package xyz.cxc6922.functionviewer.core.tes

import com.fasterxml.jackson.databind.ObjectMapper
import xyz.cxc6922.functionviewer.core.node._
import xyz.cxc6922.functionviewer.core.visitors.StringerVisitor

object TestString {
  val objectMapper = new ObjectMapper

  def main(args: Array[String]): Unit = {
    val node = new MultiplyNode(
      new PlusNode(
        new ConstantNode(12),
        new VariableNode("x")
      ),
      new ConstantNode(33)
    )

    val stringerVisitor = new StringerVisitor
    val str = node.accept(stringerVisitor)

    val json = objectMapper.writeValueAsString(node)

    println(json)
    println(ConstantNode.Type.getClass + " " +ConstantNode.Type.Zero.getClass)
    println(str)
    println(node.toString)
  }

  def test(node: Node): Unit = {

  }

}
