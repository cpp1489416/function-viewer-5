package xyz.cxc6922.functionviewer2.controller

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage

import com.fasterxml.jackson.databind.ObjectMapper
import javax.imageio.ImageIO
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import xyz.cxc6922.functionviewer2.model.dto.{GenerateRequestDto, GeneratingStatusResponseDto, RestApiResult}
import xyz.cxc6922.functionviewer2.service.{FunctionImageProvider, FunctionService}

@Controller
@RequestMapping(value = {
  Array("image")
})
class ImageController {
  @Autowired
  val imageProvider: FunctionImageProvider = null

  @Autowired
  val functionService: FunctionService = null

  @RequestMapping(value = {
    Array("generate")
  })
  def generateGet(@RequestParam("function") function: String,
                  request: HttpServletRequest,
                  response: HttpServletResponse): Unit = {
    response.setContentType("image/png")
  }

  @PostMapping(value = {
    Array("generate")
  })
  def generate(@RequestBody json: GenerateRequestDto,
               request: HttpServletRequest,
               response: HttpServletResponse) {
    response.setContentType("image/png")
    imageProvider.source = json.function
    imageProvider.pixelLength = json.pixelLength
    imageProvider.leftX = json.leftX
    imageProvider.rightX = json.rightX
    imageProvider.upY = json.upY
    imageProvider.downY = json.downY

    imageProvider.generateImage()
    val image = imageProvider.image
    ImageIO.write(image, "png", response.getOutputStream)
  }

  @PostMapping(value = {
    Array("startJob")
  })
  @ResponseBody
  def startGenerate(@RequestBody json: GenerateRequestDto): RestApiResult = {
    val id = functionService.startFunctionJob(json)
    new RestApiResult(id)
  }

  @RequestMapping(value = {
    Array("queryJobStatus")
  })
  @ResponseBody
  def queryJobStatus(@RequestParam(value = "id") id: String): RestApiResult = {
    functionService.getGeneratingStatus(id) match {
      case None => new RestApiResult(-1, "no such job")
      case Some(response) => new RestApiResult(response)
    }
  }

  @RequestMapping(value = {
    Array("queryImage")
  })
  def queryImage(@RequestParam(value = "id") id: String,
                 request: HttpServletRequest,
                 response: HttpServletResponse): Unit = {
    def error(msg: String): Unit = {
      response.setStatus(500)
      response.setContentType("application/json")
      val objectMapper = new ObjectMapper()
      objectMapper.writeValue(response.getOutputStream, new RestApiResult(-1, msg))
    }

    functionService.getImage(id) match {
      case None =>
        error("no such id")
      case Some(image) =>
        if (image == null) {
          error("image is not generated yet")
        } else {
          response.setContentType("image/png")
          ImageIO.write(image, "png", response.getOutputStream)
        }
    }
  }
}
