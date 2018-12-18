package xyz.cxc6922.functionviewer2.config.shiro

import org.apache.shiro.authc._
import org.apache.shiro.authz.{AuthorizationInfo, SimpleAuthorizationInfo}
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import xyz.cxc6922.functionviewer2.model.po.Role

class ShiroRealm extends  AuthorizingRealm {
  // 身份验证
  override def doGetAuthorizationInfo(principalCollection: PrincipalCollection): AuthorizationInfo = {
    val info = new SimpleAuthorizationInfo()
    info.addRole(new Role().name)
    info
  }

  // 用户验证
  @throws(classOf[AuthenticationException])
  override def doGetAuthenticationInfo(authenticationToken: AuthenticationToken): AuthenticationInfo = {
    val usernamePasswordToken = authenticationToken.asInstanceOf[UsernamePasswordToken]
    val name = usernamePasswordToken.getPrincipal.toString
    val password = new String(usernamePasswordToken.getPassword)
    if (!name.equals("admin") || !password.equals("password")) {
      throw new AuthenticationException("用户名或密码错误")
    }
    val info = new SimpleAuthenticationInfo(name, password, getName)
    info
  }
}
