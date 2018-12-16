package xyz.cxc6922.functionviewer2.model.cache

import java.awt.Image
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.{EnableScheduling, Scheduled}
import org.springframework.stereotype.Component

@Component
@Configuration
@EnableScheduling
class JobCache {
  private val imageMap: ConcurrentHashMap[String, Info] = new ConcurrentHashMap[String, Info]()

  def get(id: String): Option[Object] = {
    if (null == imageMap.get(id)) {
      None
    } else {
      new Some[Object](imageMap.get(id).value)
    }
  }

  def set(id: String, value: Object): Unit = {
    imageMap.put(id, new Info(value))
  }

  def updateToCanExpires(id: String) : Unit = {
    val info = imageMap.get(id)
    if (info != null) {
      info.time = System.currentTimeMillis()
      info.neverExpires = false
    }
  }

  @Scheduled(cron = "* 1 * * * *")
  def removeLast(): Unit = {
    val itr = imageMap.entrySet().iterator()
    while (itr.hasNext) {
      val entry = itr.next()
      val info = entry.getValue
      if (!info.neverExpires && info.expiresMillis + info.time >= System.currentTimeMillis()) {
        itr.remove()
      }
    }
  }

  class Info(var value: Object = null) {
    var time: Double = System.currentTimeMillis()
    var expiresMillis: Double = 1000 * 60 * 60
    var neverExpires : Boolean = true
  }


}
