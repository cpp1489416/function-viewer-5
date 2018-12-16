package xyz.cxc6922.functionviewer2.service

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage

import lombok.{Getter, Setter, ToString}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Service
import xyz.cxc6922.functionviewer.core.newton.SubNewtonObject
import xyz.cxc6922.functionviewer.core.util.AstUtil

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

@Service
class FunctionImageProvider() {
  private var map: Map = _

  var source: String = "x * x + y * y - 2500"
  var pixelLength: Double = 1.0
  var leftX: Double = -50.0
  var rightX: Double = 50.0
  var upY: Double = 50.0
  var downY: Double = -50.0

  val newton: SubNewtonObject = new SubNewtonObject()

  private def arrayLength: (Int, Int) = {
    val xLength: Int = ((rightX - leftX) / pixelLength).toInt + 1
    val yLength: Int = ((upY - downY) / pixelLength).toInt + 1
    (xLength, yLength)
  }

  def generateImage(): BufferedImage = {
    generateMap()
    map.reverseY()
    val image: BufferedImage = new BufferedImage(arrayLength._1, arrayLength._2, BufferedImage.TYPE_INT_RGB)
    val graphics: Graphics = image.getGraphics
    graphics.setColor(Color.LIGHT_GRAY)
    graphics.fillRect(0, 0, arrayLength._1, arrayLength._2)
    graphics.setColor(Color.BLACK)
    for (x <- 0 until arrayLength._1; y <- 0 until arrayLength._2 if map(x, y)) {
      graphics.fillRect(x, y, 1, 1)
    }
    map.reverseY()
    image
  }

  def generateMap(): Unit = {
    // init map
    map = new Map(arrayLength._1, arrayLength._2)

    // init newton
    newton.node = AstUtil.genNode(source)
    newton.toDifferentiate = Set("x")
    newton.updateDifferentiateNode()

    // epoch
    for (x <- 0 until arrayLength._1; y <- 0 until arrayLength._2) {
      val xVal = leftX + x * pixelLength
      val yVal = downY + y * pixelLength
      newton.environment.put("x", xVal)
      newton.environment.put("y", yVal)
      if (math.abs(newton.newton - xVal) < 0.5) {
        map(x, y) = true
      }
    }
  }

  class Map(val xLength: Int, val yLength: Int) {
    private val map: Array[Array[Boolean]] = new Array[Array[Boolean]](xLength)

    for (row <- 0 until xLength) {
      map(row) = new Array[Boolean](yLength)
    }

    def apply(x: Int, y: Int): Boolean = map(x)(y)

    def update(x: Int, y: Int, rhs: Boolean): Unit = map(x)(y) = rhs

    def reverseY(): Unit = {
      for (x <- 0 until xLength; y <- 0 to yLength / 2) {
        val last = map(x)(y)
        map(x)(y) = map(x)(yLength - 1 - y)
        map(x)(yLength - 1 - y) = last
      }
    }
  }

}
