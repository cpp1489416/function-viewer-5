package xyz.cxc6922.functionviewer2.model.cache

import java.awt.Image

import org.springframework.stereotype.Component

import scala.collection.mutable

@Component
class JobCache {
  private val imageMap: mutable.Map[String, Image] = mutable.Map()
  private val progressMap: mutable.Map[String, Double] = mutable.Map()

  def putImage(id: String, image: Image): Unit = {
    imageMap.put(id, image)
  }

  def getImage(id: String): Image = {
    imageMap.get(id) match {
      case None => null
      case Some(image) =>
        imageMap.remove(id)
        image
    }
  }

  def setProgress(id: String, progress: Double): Unit = {
    progressMap.put(id, progress)
  }

  def getProgress(id: String): Double = {
    progressMap.get(id) match {
      case None => 2.0
      case Some(progress) => progress
    }
  }
}
