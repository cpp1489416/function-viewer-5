package xyz.cxc6922.functionviewer2.service

import java.awt.Image
import java.awt.image.BufferedImage
import java.util.UUID
import java.util.concurrent.Executors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.cxc6922.functionviewer2.model.cache.JobCache
import xyz.cxc6922.functionviewer2.model.dto.{GenerateRequestDto, GeneratingStatusResponseDto}

@Service
class FunctionService {

  @Autowired
  private val jobCache: JobCache = null

  private val threadPool = Executors.newCachedThreadPool()

  def startFunctionJob(dto: GenerateRequestDto): String = {
    val id = UUID.randomUUID().toString
    val imageProvider = new FunctionImageProvider()
    imageProvider.source = dto.function
    imageProvider.pixelLength = dto.pixelLength
    imageProvider.leftX = dto.leftX
    imageProvider.rightX = dto.rightX
    imageProvider.upY = dto.upY
    imageProvider.downY = dto.downY
    jobCache.set(id, imageProvider)

    // emit a progress
    threadPool.submit(new Runnable {
      override def run(): Unit = {
        imageProvider.generateMap()
        imageProvider.generateImage()
        jobCache.updateToCanExpires(id)
      }
    })

    id
  }

  def getGeneratingStatus(id: String): Option[GeneratingStatusResponseDto] = {
    jobCache.get(id) match {
      case None => None
      case Some(info) =>
        val info1 = info.asInstanceOf[FunctionImageProvider]
        val ans = new GeneratingStatusResponseDto()
        ans.finished = info1.finished
        ans.mapGenerationProgress = info1.mapGeneratingProgress
        Some(ans)
    }
  }

  def getImage(id: String): Option[BufferedImage] = {
    jobCache.get(id) match {
      case None => None
      case Some(info) =>
        var info1 = info.asInstanceOf[FunctionImageProvider]
        Some(info1.image)
    }
  }

}
