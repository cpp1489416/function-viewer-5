package xyz.cxc6922.functionviewer2.controller

import javax.servlet.http.HttpServletResponse
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.{AuthenticationException, UsernamePasswordToken}
import org.apache.shiro.authz.{AuthorizationException, UnauthenticatedException}
import org.apache.shiro.authz.annotation.RequiresRoles
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, RequestMapping, RequestParam, ResponseBody}
import xyz.cxc6922.functionviewer2.model.dto.RestApiResult

@Controller
@RequestMapping(value = {
  Array("user")
})
class UserController() {

  @RequestMapping(value = {
    Array("login")
  })
  @ResponseBody
  def login(@RequestParam("name") name: String, @RequestParam("password") password: String): RestApiResult = {
    val subject = SecurityUtils.getSubject
    val usernamePasswordToken = new UsernamePasswordToken(name, password)
    try {
      subject.login(usernamePasswordToken)
      subject.getSession().setAttribute("d", "eee")
      new RestApiResult(0, "登陆成功")
    } catch {
      case e: AuthenticationException =>
        new RestApiResult(-1, "登陆失败：" + e.getMessage)
    }
  }

  @RequiresRoles(value = {
    Array("admin")
  })
  @RequestMapping(value = {
    Array("heart")
  })
  @ResponseBody
  def heart(): String = {
    "OK" + SecurityUtils.getSubject.getSession().getAttribute("d")
  }

  @RequestMapping(value = {
    Array("en")
  })
  @ResponseBody
  def en(): String = {
    SecurityUtils.getSubject.getSession.setAttribute("en", "en")
    "En" + SecurityUtils.getSubject.getSession.getAttribute("d")
  }

  @ExceptionHandler(value = {
    Array(classOf[UnauthenticatedException])
  })
  @ResponseBody
  def handle1(e: UnauthenticatedException, response: HttpServletResponse): RestApiResult = {
    response.setStatus(401)
    new RestApiResult(-1, "您没有权限")
  }


}
