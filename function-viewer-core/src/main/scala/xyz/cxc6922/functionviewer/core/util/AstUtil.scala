package xyz.cxc6922.functionviewer.core.util

import xyz.cxc6922.functionviewer.core.ast.Node
import xyz.cxc6922.functionviewer.core.parsing.{FunctionLexer, LL1FunctionAnalyzer}

object AstUtil {
  def genNode(source: String): Node = {
    new LL1FunctionAnalyzer(new FunctionLexer(source).genAll()).parsePlus()
  }
}
