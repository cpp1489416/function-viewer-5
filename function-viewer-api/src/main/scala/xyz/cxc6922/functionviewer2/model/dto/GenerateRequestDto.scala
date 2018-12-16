package xyz.cxc6922.functionviewer2.model.dto

import scala.beans.BeanProperty

@BeanProperty
class GenerateRequestDto {
  @BeanProperty val function: String = "x * x + y * y - 250"

  @BeanProperty val leftX: Double = -50

  @BeanProperty val rightX: Double = 50

  @BeanProperty val upY: Double = 50

  @BeanProperty val downY: Double = -50

  @BeanProperty val pixelLength: Double = 1

}
