package xyz.cxc6922.functionviewer.core.ast

trait Node {
  def accept(visitor: Visitor): Any
}
