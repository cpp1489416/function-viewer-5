package xyz.cxc6922.functionviewer.core.ast

trait Node {
  def accept[T](visitor: Visitor[T]): T
}
