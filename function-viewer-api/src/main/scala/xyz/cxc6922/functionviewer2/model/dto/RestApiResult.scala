package xyz.cxc6922.functionviewer2.model.dto

import scala.beans.BeanProperty

class RestApiResult {
  @BeanProperty var code : Int = 0
  @BeanProperty var msg : String = ""
  @BeanProperty var data : Object = _

  def this(data: Object) {
    this()
    this.data = data
  }

  def this(code: Int, msg: String) {
    this()
    this.code = code
    this.msg = msg
  }
}
