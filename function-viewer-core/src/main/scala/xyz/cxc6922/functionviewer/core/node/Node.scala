package xyz.cxc6922.functionviewer.core.node

trait Node {
  def accept(visitor: Visitor): Any
}
