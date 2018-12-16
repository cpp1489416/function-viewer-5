package xyz.cxc6922.functionviewer2.model.dto

import scala.beans.BeanProperty


class GeneratingStatusResponseDto {
  @BeanProperty var mapGenerationProgress: Double = 0

  @BeanProperty var finished: Boolean = false

}
