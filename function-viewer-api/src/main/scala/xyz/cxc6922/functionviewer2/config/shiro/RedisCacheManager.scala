package xyz.cxc6922.functionviewer2.config.shiro

import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

import org.apache.shiro.cache.{Cache, CacheManager}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisCacheManager extends CacheManager {
  @Autowired
  private val redisTemplate: RedisTemplate[String, Object] = null

  private val cacheMap: ConcurrentHashMap[String, Cache[Object, Object]] = new ConcurrentHashMap[String, Cache[Object, Object]]()

  override def getCache[K, V](name: String): Cache[K, V] = {
    val cache = cacheMap.get(name)
    Option(cache).getOrElse({
      val cache = new RedisCache[K, V](name, redisTemplate)
      cacheMap.put(name, cache.asInstanceOf[Cache[Object, Object]])
      cache
    }).asInstanceOf[Cache[K, V]]
  }
}
