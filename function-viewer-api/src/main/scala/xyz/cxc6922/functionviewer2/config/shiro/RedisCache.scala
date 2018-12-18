package xyz.cxc6922.functionviewer2.config.shiro

import java.util
import java.util.concurrent.TimeUnit
import scala.collection.JavaConversions._

import org.apache.shiro.cache.Cache
import org.springframework.data.redis.core.RedisTemplate

class RedisCache[K, V](val name: String, val redisTemplate: RedisTemplate[String, Object]) extends Cache[K, V] {
  val CACHE_EXPIRES_MINUTES: Long = 30
  val CACHE_PREFIX = "redis_cache: "

  private def prefix: String = CACHE_PREFIX + name + ": "

  private def stringKeys(): util.Set[String] = {
    redisTemplate.keys(prefix + "*")
  }

  override def get(key: K): V = {
    val value = redisTemplate.opsForValue().get(prefix + key).asInstanceOf[V]
    redisTemplate.opsForValue().set(prefix + key, value.asInstanceOf[Object], CACHE_EXPIRES_MINUTES, TimeUnit.MINUTES)
    value
  }

  override def put(key: K, value: V): V = {
    redisTemplate.opsForValue().set(prefix + key, value.asInstanceOf[Object], CACHE_EXPIRES_MINUTES, TimeUnit.MINUTES)
    value
  }

  override def remove(key: K): V = {
    val value = redisTemplate.opsForValue().get(prefix + key).asInstanceOf[V]
    redisTemplate.delete(prefix + key)
    value
  }

  override def clear(): Unit = {
    redisTemplate.delete(stringKeys())
  }

  override def size(): Int = {
    keys().size()
  }

  override def keys(): util.Set[K] = {
    val keys = stringKeys()
    val set = new util.HashSet[K]()
    for (key <- keys) {
      set.add(key.asInstanceOf[K])
    }
    set
  }

  override def values(): util.Collection[V] = {
    val keys = stringKeys()
    val values = new util.LinkedList[V]()
    for (k <- keys) {
      values.add(get(k.asInstanceOf[K]))
    }
    values
  }
}
