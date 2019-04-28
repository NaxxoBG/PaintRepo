package engine

import java.awt.image.BufferedImage
import java.awt.{Color, Font}

import engine.BresenhamLineAlgorithm._
import engine.Parser._
import engine.engineValues._

import scala.annotation.tailrec

object DrawingEngine {

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

  def pointDrawer(pointList: PointList, bm: RgbBitmap, c: Color): Unit = pointList match {
    case PointListNil() => Unit
    case PointListCons(hd, tl) => bm.setPixel(hd.x, hd.y, c); pointDrawer(tl, bm, c)
  }

  def drawLine(bm: RgbBitmap, c: Color, x0: Int, y0: Int, x1: Int, y1: Int): Unit = {
    pointDrawer(LineWrapper(x0, y0, x1, y1), bm, c)
  }

  def drawCircle(bm: RgbBitmap, c: Color, x0: Int, y0: Int, radius: Int): Unit = {
    printf("x0:%d, y0:%d, r:%d \n", x0, y0, radius)
    val points = BresenhamLineAlgorithm.midpointWrapper(x0, y0, radius)
    pointDrawer(points, bm, c)
  }

  def drawRectangle(x1: Int, y1: Int, x2: Int, y2: Int, bitmap: RgbBitmap, fill: Boolean): Unit = {
    val points = getPointRectangle(x1, y1, x2, y2, fill)
    pointDrawer(points, bitmap, Color.BLACK)
  }

  def fillCircle(): Unit = {

  }

  @tailrec
  def drawImg(figureList: List[Figure], boundingBox: BoundingBox, bitmap: RgbBitmap): Unit = figureList match {
    case List() => Unit
    case f :: tl =>
      f match {
        case Fill(_, fig) => fig match {
          case Circle(_, _, _) => fillCircle()
          case Rectangle(x1, y1, x2, y2) =>
            drawRectangle(math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2), bitmap, fill = true)
          case _ => return
        }
        case Line(x1, y1, x2, y2) => drawLine(bitmap, Color.BLACK, math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2))
        case Rectangle(x1, y1, x2, y2) =>
          drawRectangle(math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2), bitmap, fill = false)
        case Circle(x, y, r) => drawCircle(bitmap, Color.BLACK, x, y, r)
        case TextAt(x, y, s) => bitmap.writeText(s, x, y)
        case BoundingBox(x1, y1, x2, y2) => drawRectangle(x1, y1, x2, y2, bitmap, fill = false)
        case _ => return
      }
      drawImg(tl, boundingBox, bitmap)
  }

  /**
    * The method assumes the first element of the syntax tree is a BoundingBox. It draws all figures instantly, based on the passed <br >
    * command, and returns the BufferedImage.
    * @param commands String
    * @return BufferedImage
    */
  def drawSyntaxTree(commands: String): BufferedImage = {
    val bitMapping = new RgbBitmap(500, 500)
    bitMapping.image.createGraphics()
    bitMapping.fill(Color.WHITE)
    val syntaxTree = generateAbstractSyntaxTree(commands)
    if (isSyntaxTreeValid(syntaxTree)) {
      drawImg(syntaxTree, syntaxTree.head.asInstanceOf[BoundingBox], bitMapping)
    }
    bitMapping.image
  }
}