package xyz.cxc6922.functionviewer2.config

import java.io.Serializable
import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.{GenericJackson2JsonRedisSerializer, StringRedisSerializer}
import org.springframework.stereotype.Component
import xyz.cxc6922.functionviewer2.config.shiro.{HeaderSessionManager, NoExceptionJackson2JsonRedisSerializer, Object2DataRedisSerializer}

import scala.beans.BeanProperty

@Configuration
@Component
class RedisConfig {
  private val log = LoggerFactory.getLogger(classOf[HeaderSessionManager])

  @Bean
  def redisTemplate(factory: RedisConnectionFactory): RedisTemplate[String, AnyRef] = try {
    val template = new RedisTemplate[String, AnyRef]
    template.setConnectionFactory(factory)
    val keySerializer = new StringRedisSerializer
    val valueSerializer = new Object2DataRedisSerializer
    template.setDefaultSerializer(valueSerializer)
    template.setKeySerializer(keySerializer)
    template.setHashKeySerializer(keySerializer)
    template.setHashValueSerializer(valueSerializer)
    template.setHashValueSerializer(valueSerializer)
    template.afterPropertiesSet()
    template.opsForValue.set("redisTest", new RedisTest, 30, TimeUnit.MINUTES)
    template
  } catch {
    case e: Exception =>
      log.error("无法连接到redis", e)
      val t = new RedisTemplate[AnyRef, AnyRef]
      t.setConnectionFactory(factory)
      null
  }

  class RedisTest extends Serializable {
    @BeanProperty var aaa: String = "bbb"
  }

}