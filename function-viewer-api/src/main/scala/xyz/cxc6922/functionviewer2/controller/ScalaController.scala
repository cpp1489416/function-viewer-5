package xyz.cxc6922.functionviewer2.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import xyz.cxc6922.functionviewer2.entity.po.User

@Controller
@RequestMapping(value = {Array("scala")})
class ScalaController() {

  @RequestMapping(value = {
    Array("testScala")
  })
  @ResponseBody
  def newTask(): Object = {
    val ans = new User()
    ans.setName("scala name")
    ans.setId(-1)
    ans
  }

}
