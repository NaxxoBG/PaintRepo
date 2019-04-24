import java.awt.{Color, Font}
import java.awt.image.BufferedImage

import Parser._
import java.io.File

import BresenhamLineAlgorithm._
import javax.imageio.ImageIO

//Bitmap class taken from https://rosettacode.org/wiki/Bitmap#Scala
class RgbBitmap(val width:Int, val height:Int) {
  val image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

  def fill(c:Color)={
    val g = image.getGraphics
    g.setColor(c)
    g.fillRect(0, 0, width, height)
  }

  def setPixel(x:Int, y:Int, c:Color)= image.setRGB(x, y, c.getRGB())
  def getPixel(x:Int, y:Int) = new Color(image.getRGB(x, y))
  def writeText(txt: String, x:Int, y:Int) = {
    val g = image.getGraphics
    g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
    g.setColor(Color.BLACK)
    g.drawString(txt, x, y)
  }
}

  def pointDrawer(pointList: PointList, bm:RgbBitmap, c: Color):Unit = pointList match {
    case PointListNil() => Unit
    case PointListCons(hd, tl) => bm.setPixel(hd.x, hd.y, c); pointDrawer(tl, bm, c)
  }

  def DrawLine(bm:RgbBitmap, c: Color, x0: Int, y0: Int, x1: Int, y1: Int: Unit = {
    pointDrawer(LineWrapper(x0, y0, x1, y1), bm, c)
  }

  def DrawCircle(bm:RgbBitmap, c:Color, x0:Int, y0:Int, radius:Int): Unit = {
    pointDrawer(midpointWrapper(x0, y0, radius), bm, c, x_low, y_low, x_high, y_high)
  }



  //Midpoint circle algorithm taken from https://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm#Scala
  def midpoint(bm:RgbBitmap, x0:Int, y0:Int, radius:Int, c:Color)={
    var f=1-radius
    var ddF_x=1
    var ddF_y= -2*radius
    var x=0
    var y=radius

    try {
    bm.setPixel(x0, y0+radius, c)
    bm.setPixel(x0, y0-radius, c)
    bm.setPixel(x0+radius, y0, c)
    bm.setPixel(x0-radius, y0, c)

    while(x < y)
    {
      if(f >= 0)
      {
        y-=1
        ddF_y+=2
        f+=ddF_y
      }
      x+=1
      ddF_x+=2
      f+=ddF_x

      bm.setPixel(x0+x, y0+y, c)
      bm.setPixel(x0-x, y0+y, c)
      bm.setPixel(x0+x, y0-y, c)
      bm.setPixel(x0-x, y0-y, c)
      bm.setPixel(x0+y, y0+x, c)
      bm.setPixel(x0-y, y0+x, c)
      bm.setPixel(x0+y, y0-x, c)
      bm.setPixel(x0-y, y0-x, c)
    }

    } catch {
      case e: ArrayIndexOutOfBoundsException => e.printStackTrace()
    }
  }

  def DrawImg(figureList: List[Figure], boundingBox: BoundingBox, bitmap: RgbBitmap): Unit = figureList match{
    case List() => Unit
    case f::tl =>
      f match {
        case Line(x1,y1,x2,y2) => DrawLine(bitmap,Color.BLACK, x1,y1,x2,y2)
        case Rectangle(x1,y1,x2,y2) => {
          DrawLine(bitmap, Color.BLACK, math.min(x1, boundingBox.x1), math.min(y1, boundingBox.y1), math.min(x1, boundingBox.y1), math.min(y2, boundingBox.x2))
          DrawLine(bitmap, Color.BLACK, math.min(x1, boundingBox.x1), math.min(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y1, boundingBox.y1))
          DrawLine(bitmap, Color.BLACK, math.min(x1, boundingBox.x1), math.min(y2,boundingBox.y2), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2))
          DrawLine(bitmap, Color.BLACK, math.min(x2, boundingBox.x2), math.min(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2))
        }
        //case Circle(x, y, r)=> DrawCircle(bitmap, Color.BLACK, x, y, r)
        case Circle(x, y, r) => midpoint(bitmap, x, y, r, new Color(0,0,0))
        case TextAt(x, y, s) => bitmap.writeText(s, x, y)
        case BoundingBox(x1,y1,x2,y2) =>
          DrawLine(bitmap, Color.BLACK, x1, y1, x1, y2)
          DrawLine(bitmap, Color.BLACK, x1, y1, x2, y1)
          DrawLine(bitmap, Color.BLACK, x1, y2, x2, y2)
          DrawLine(bitmap, Color.BLACK, x2, y1, x2, y2)
        case _ => return
      }
      DrawImg(tl, boundingBox, bitmap)
  }

  def Fill(c:String, g:Figure): Unit ={

  }

  def testDraw(): BufferedImage = {
    val bitMapping = new RgbBitmap(500,500)
    val L = Line(20, 20, 400, 400)
    val C = Circle(200, 200, 100)
    val T = TextAt(100, 100, "Hello Again World ")
    bitMapping.image.createGraphics()
    bitMapping.fill(Color.WHITE)
    return bitMapping.image
  }

  val bitMapping = new RgbBitmap(500,500)
  val L = Line(20, 20, 400, 400)
  val C = Circle(200, 200, 100)
  val T = TextAt(100, 100, "Hello Again World ")
  bitMapping.image.createGraphics()
  bitMapping.fill(Color.WHITE)

  DrawImg(List(L, C, T, Nil()), bitMapping)
  ImageIO.write(bitMapping.image, "jpg", new File("/Users/simonthranehansen/Documents/tester.jpg"))