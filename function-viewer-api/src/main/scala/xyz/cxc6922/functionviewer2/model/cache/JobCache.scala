package xyz.cxc6922.functionviewer2.model.cache

import java.awt.Image

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.{EnableScheduling, Scheduled}
import org.springframework.stereotype.Component

import scala.collection.mutable

@Component
@Configuration
@EnableScheduling
class JobCache {
  private val imageMap: mutable.Map[String, ImageInfo] = mutable.Map()

  def getInfo(id: String): Option[ImageInfo] = imageMap.get(id)

  def createInfo(id: String): Unit = {
    imageMap.put(id, new ImageInfo)
  }

  def setProgress(id: String, progress: Double): Unit = {
    getInfo(id) match {
      case None =>
        createInfo(id)
        setProgress(id, progress)
      case Some(info) =>
        info.progress = progress
        info.time = System.currentTimeMillis()
    }
  }

  def setImage(id: String, image: Image): Unit = {
    getInfo(id) match {
      case None =>
        createInfo(id)
        setImage(id, image)
      case Some(info) =>
        info.image = image
        info.time = System.currentTimeMillis()
    }
  }

  @Scheduled(cron = "* 1 * * * *")
  def removeLast(): Unit = {
    imageMap.retain((id, info) => {
      info.expiresMillis + info.time <= System.currentTimeMillis()
    })
  }

  class ImageInfo {
    var time: Long = System.currentTimeMillis()
    var progress: Double = -1
    var finished: Boolean = false
    var image: Image = _
    val expiresMillis: Long = 1000 * 60 * 60
  }

}
