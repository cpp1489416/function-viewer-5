package xyz.cxc6922.functionviewer2.config

import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.session.mgt.SessionManager
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import xyz.cxc6922.functionviewer2.config.shiro.{HeaderSessionManager, RedisCacheManager, ShiroRealm}

@Configuration
class ShiroConfig {
  @Autowired
  val redisCacheManager : RedisCacheManager = null

  @Bean
  def shiroRealm: ShiroRealm = {
    new ShiroRealm
  }

  @Bean
  def sessionManager: SessionManager = {
    val sessionManager = new HeaderSessionManager()
    sessionManager.setGlobalSessionTimeout(15000) // 会话过期时间
    sessionManager.setSessionValidationInterval(1000) // 每秒检测是否过期
    sessionManager.setSessionValidationSchedulerEnabled(true) // 开启会话过期扫描
    sessionManager.setCacheManager(redisCacheManager)
    sessionManager
  }

  @Bean
  def securityManager: SecurityManager = {
    val securityManager = new DefaultWebSecurityManager()
    securityManager.setRealm(shiroRealm)
    securityManager.setSessionManager(sessionManager)
    securityManager.setCacheManager(redisCacheManager)
    securityManager
  }

  @Bean
  def shiroFilterFactoryBean(securityManager: SecurityManager): ShiroFilterFactoryBean = {
    val shiroFilterFactoryBean = new ShiroFilterFactoryBean
    shiroFilterFactoryBean.setSecurityManager(securityManager)
    shiroFilterFactoryBean
  }

  @Bean
  def authorizationAttributeSourceAdvisor(securityManager: SecurityManager) : AuthorizationAttributeSourceAdvisor = {
    val authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor()
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager)
    authorizationAttributeSourceAdvisor
  }

}
