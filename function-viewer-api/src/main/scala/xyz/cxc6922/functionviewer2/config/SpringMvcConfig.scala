package xyz.cxc6922.functionviewer2.config

import java.util.Properties

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver

@Configuration
class SpringMvcConfig {

  /*@Bean
  def simpleMappingExceptionResolver: SimpleMappingExceptionResolver = {
    val simpleMappingExceptionResolver = new SimpleMappingExceptionResolver
    val props = new Properties()
    // props.setProperty("org.apache.shiro.authz.UnauthenticatedException", "error")
    simpleMappingExceptionResolver.setExceptionMappings(props)
    simpleMappingExceptionResolver
  }*/
}
