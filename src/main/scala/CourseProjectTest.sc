import java.awt.{Color, Font}
import java.awt.image.BufferedImage

import Parser._
import java.io.File

import BresenhamLineAlgorithm._
import javax.imageio.ImageIO

import scala.annotation.tailrec

//Bitmap class taken from https://rosettacode.org/wiki/Bitmap#Scala
class RgbBitmap(val width: Int, val height: Int) {
  val image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

  def fill(c: Color) = {
    val g = image.getGraphics
    g.setColor(c)
    g.fillRect(0, 0, width, height)
  }

  def setPixel(x: Int, y: Int, c: Color) = image.setRGB(x, y, c.getRGB())

  def getPixel(x: Int, y: Int) = new Color(image.getRGB(x, y))

  def writeText(txt: String, x: Int, y: Int) = {
    val g = image.getGraphics
    g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
    g.setColor(Color.BLACK)
    g.drawString(txt, x, y)
  }
}


def pointDrawer(pointList: PointList, bm: RgbBitmap, c: Color): Unit = pointList match {
  case PointListNil() => Unit
  case PointListCons(hd, tl) => bm.setPixel(hd.x, hd.y, c); pointDrawer(tl, bm, c)
}

def DrawLine(bm: RgbBitmap, c: Color, x0: Int, y0: Int, x1: Int, y1: Int): Unit = {
  pointDrawer(LineWrapper(x0, y0, x1, y1), bm, c)
}

def DrawCircle(bm: RgbBitmap, c: Color, x0: Int, y0: Int, radius: Int): Unit = {
  printf("x0:%d, y0:%d, r:%d \n", x0, y0, radius)
  val points = BresenhamLineAlgorithm.midpointWrapper(x0, y0, radius)
  pointDrawer(points, bm, c)
}

def drawRectangle(x1: Int, y1: Int, x2: Int, y2: Int, bitmap: RgbBitmap, fill:Boolean): Unit = {
  val points = getPointRectangle(x1, y1, x2, y2, fill)
  pointDrawer(points, bitmap, Color.BLACK)
}

def FillCircle(): Unit ={

}

@tailrec
def DrawImg(figureList: List[Figure], boundingBox: BoundingBox, bitmap: RgbBitmap): Unit = figureList match {
  case List() => Unit
  case f :: tl =>
    f match {
      case Fill(c, fig) => fig match {
        case Circle(x, y, r) => FillCircle()
        case Rectangle(x1, y1, x2, y2) =>
          drawRectangle(math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2), bitmap, true)
        case _ => return
      }
      case Line(x1, y1, x2, y2) => DrawLine(bitmap, Color.BLACK, math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2))
      case Rectangle(x1, y1, x2, y2) => {
        drawRectangle(math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2), bitmap, false)
      }
      case Circle(x, y, r)=> DrawCircle(bitmap, Color.BLACK, x, y, r)
      case TextAt(x, y, s) => bitmap.writeText(s, x, y)
      case BoundingBox(x1, y1, x2, y2) => drawRectangle(x1, y1, x2, y2, bitmap, false)
      case _ => return
    }
    DrawImg(tl, boundingBox, bitmap)
}


def testDraw(): BufferedImage = {
  val bitMapping = new RgbBitmap(500, 500)
  val L = Line(20, 20, 400, 400)
  val C = Circle(200, 200, 100)
  val T = TextAt(100, 100, "Hello Again World ")
  val R = Circle(200, 200, 100)
  bitMapping.image.createGraphics()
  bitMapping.fill(Color.WHITE)
  return bitMapping.image
}

val bitMapping = new RgbBitmap(500, 500)
val B = BoundingBox(0, 0, 500, 500)
val fillR = Fill("BLACK", Rectangle(300, 300, 400, 400))
val R = Rectangle(2,2,256,256)
val C = Circle(110, 110, 100)
bitMapping.image.createGraphics()
bitMapping.fill(Color.WHITE)

DrawImg(List(fillR, C, R,  Nil()), B, bitMapping)
ImageIO.write(bitMapping.image, "jpg", new File("/Users/simonthranehansen/Documents/tester.jpg"))