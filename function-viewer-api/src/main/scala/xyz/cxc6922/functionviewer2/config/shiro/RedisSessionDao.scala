package xyz.cxc6922.functionviewer2.config.shiro

import java.util.concurrent.TimeUnit
import java.{io, util}

import scala.collection.JavaConversions._
import org.apache.shiro.session.Session
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

import scala.collection.mutable

@Component
class RedisSessionDao extends AbstractSessionDAO {
  @Autowired
  private val redisTemplate: RedisTemplate[String, Object] = null

  val SESSION_PREFIX = "shiro_redis_session: "

  val SESSION_EXPIRES_MINUTES: Long = 30

  override def doCreate(session: Session): io.Serializable = {
    val sessionId = generateSessionId(session)
    assignSessionId(session, sessionId)
    redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId.toString, session, SESSION_EXPIRES_MINUTES, TimeUnit.MINUTES)
    sessionId
  }

  override def doReadSession(sessionId: io.Serializable): Session = {
    val session = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId.toString).asInstanceOf[Session]
    if (session != null) {
      redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId.toString, session, SESSION_EXPIRES_MINUTES, TimeUnit.MINUTES)
    }
    session
  }

  override def update(session: Session): Unit = {
    redisTemplate.opsForValue().set(SESSION_PREFIX + session.getId, session, SESSION_EXPIRES_MINUTES, TimeUnit.MINUTES)
  }

  override def delete(session: Session): Unit = {
    redisTemplate.delete(SESSION_PREFIX + session.getId)
  }

  override def getActiveSessions: util.Collection[Session] = {
    val set = new util.HashSet[Session]
    val keys = redisTemplate.keys(SESSION_PREFIX + "*")
    for (key <- keys) {
      set.add(redisTemplate.opsForValue().get(key).asInstanceOf[Session])
    }
    set
  }
}
