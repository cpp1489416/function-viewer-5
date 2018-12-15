package xyz.cxc6922.functionviewer2.service

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage

import lombok.{Getter, Setter, ToString}
import xyz.cxc6922.functionviewer.core.newton.SubNewtonObject
import xyz.cxc6922.functionviewer.core.util.AstUtil

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

class FunctionImageProvider(var source: String = "x * x + y * y - 2500") {

  private val map: Map = new Map(50, 50)
  val newton: SubNewtonObject = new SubNewtonObject()

  def generateImage(): BufferedImage = {
    generateMap()
    val image: BufferedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB)
    val graphics: Graphics = image.getGraphics
    graphics.setColor(Color.LIGHT_GRAY)
    graphics.fillRect(0, 0, 50, 50)
    graphics.setColor(Color.BLACK)
    for (x <- 0 until 50 ; y <- 0 until 50 if map(x, y)) {
        graphics.fillRect(x, y, 1, 1)
    }
    image
  }

  def generateMap(): Unit = {
    newton.node = AstUtil.genNode(source)
    newton.toDifferentiate = Set("x")
    newton.updateDifferentiateNode()
    for (x <- 0 until 50) {
      for (y <- 0 until 50) {
        newton.environment.put("x", x.toDouble)
        newton.environment.put("y", y.toDouble)
        if (math.abs(newton.newton - x) < 0.5) {
          map(x, y) = true
        }
      }
    }
  }

  class Map(val xRange: Int = 50, val yRange: Int = 50) {
    private val map: Array[Array[Boolean]] = new Array[Array[Boolean]](xRange)

    for (row <- 0 until xRange) {
      map(row) = new Array[Boolean](yRange)
    }

    def apply(x: Int, y: Int): Boolean = map(x)(y)

    def update(x: Int, y: Int, rhs: Boolean): Unit = map(x)(y) = rhs
  }

}
