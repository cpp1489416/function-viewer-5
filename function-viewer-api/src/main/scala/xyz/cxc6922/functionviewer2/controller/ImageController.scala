package xyz.cxc6922.functionviewer2.controller

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage

import javax.imageio.ImageIO
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, RequestParam}
import xyz.cxc6922.functionviewer2.model.dto.GenerateRequestDto
import xyz.cxc6922.functionviewer2.service.FunctionImageProvider

@Controller
@RequestMapping(value = {
  Array("image")
})
class ImageController {
  @Autowired
  val imageProvider : FunctionImageProvider = null

  @RequestMapping(value = {
    Array("generate")
  })
  def generateGet(@RequestParam("function") function: String,
                  request: HttpServletRequest,
                  response: HttpServletResponse): Unit = {
    response.setContentType("image/png")
    val image: BufferedImage = createImage()
    ImageIO.write(image, "png", response.getOutputStream)
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

    val image =imageProvider.generateImage()
    ImageIO.write(image, "png", response.getOutputStream)
  }

  def createImage(): BufferedImage = {
    new FunctionImageProvider().generateImage()
  }

}
