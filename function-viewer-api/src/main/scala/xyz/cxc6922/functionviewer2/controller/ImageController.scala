package xyz.cxc6922.functionviewer2.controller

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage

import javax.imageio.ImageIO
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam}
import xyz.cxc6922.functionviewer2.service.FunctionImageProvider

@Controller
@RequestMapping(value = {
  Array("image")
})
class ImageController {

  @RequestMapping(value = {
    Array("generate")
  })
  def generate(@RequestParam("function") function: String,
               request: HttpServletRequest,
               response: HttpServletResponse): Unit = {
    response.setContentType("image/png")
    val image :BufferedImage = createImage()
    ImageIO.write(image, "png", response.getOutputStream)
  }

  def createImage(): BufferedImage = {
    new FunctionImageProvider().generateImage()
  }

}
