package au.model

import java.awt.{Color, Font}
import java.awt.image.BufferedImage

//Bitmap class taken from https://rosettacode.org/wiki/Bitmap#Scala
class RgbBitmap(val width: Int, val height: Int) {
  val image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

  def fill(c: Color): Unit = {
    val g = image.getGraphics
    g.setColor(c)
    g.fillRect(0, 0, width, height)
  }

  def setPixel(x: Int, y: Int, c: Color): Unit = image.setRGB(x, y, c.getRGB)

  def getPixel(x: Int, y: Int) = new Color(image.getRGB(x, y))

  def writeText(txt: String, x: Int, y: Int): Unit = {
    val g = image.getGraphics
    g.setFont(new Font("TimesRoman", Font.PLAIN, 14))
    g.setColor(Color.BLACK)
    g.drawString(txt, x, y)
  }
}
