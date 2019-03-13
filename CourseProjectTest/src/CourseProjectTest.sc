import java.awt.image.BufferedImage
import java.awt.Color

class RgbBitmap(val width:Int, val height:Int) {
  val image=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

  def fill(c:Color)={
    val g=image.getGraphics()
    g.setColor(c)
    g.fillRect(0, 0, width, height)
  }

  def setPixel(x:Int, y:Int, c:Color)=image.setRGB(x, y, c.getRGB())
  def getPixel(x:Int, y:Int)=new Color(image.getRGB(x, y))
}

class DrawingEngine {

  sealed abstract class Figure();
  case class FigNil() extends Figure;
  case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
  case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
  case class Circle(x1: Int, y1: Int, r: Int) extends Figure;
  //case class TextAt(x1: Int, y1: Int, txt:String) extends Figure;
  //case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;

  sealed abstract class FigureList();
  case class LstNil() extends FigureList;
  case class Cons(f:Figure,lst:FigureList) extends FigureList;
//Bresenham's line algorithm taken from https://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Scala
  def bresenham(bm:RgbBitmap, x0:Int, y0:Int, x1:Int, y1:Int, c:Color)={
    val dx=math.abs(x1-x0)
    val sx=if (x0<x1) 1 else -1
    val dy=math.abs(y1-y0)
    val sy=if (y0<y1) 1 else -1

    def it=new Iterator[Tuple2[Int,Int]]{
      var x=x0; var y=y0
      var err=(if (dx>dy) dx else -dy)/2
      def next={
        val res=(x,y)
        val e2=err;
        if (e2 > -dx) {err-=dy; x+=sx}
        if (e2<dy) {err+=dx; y+=sy}
        res;
      }
      def hasNext = (sx*x <= sx*x1 && sy*y <= sy*y1)
    }

    for((x,y) <- it)
      bm.setPixel(x, y, c)
  }
//Midpoint circle algorithm taken from https://rosettacode.org/wiki/Bitmap/Midpoint_circle_algorithm#Scala
  def midpoint(bm:RgbBitmap, x0:Int, y0:Int, radius:Int, c:Color)={
    var f=1-radius
    var ddF_x=1
    var ddF_y= -2*radius
    var x=0
    var y=radius

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
  }

  def Draw(g: FigureList, bitmap: RgbBitmap): Unit = g match{
    case LstNil() => return
    case Cons(f,lst) => {f match{
      case FigNil() => return
      case Line(x1,y1,x2,y2) => bresenham(bitmap,x1,y1,x2,y2,new Color(0,0,0))
      case Rectangle(x1,y1,x2,y2) => {
        bresenham(bitmap,x1,y1,x1,y2,new Color(0,0,0))
        bresenham(bitmap,x1,y1,x2,y1,new Color(0,0,0))
        bresenham(bitmap,x1,y2,x2,y2,new Color(0,0,0))
        bresenham(bitmap,x2,y1,x2,y2,new Color(0,0,0))
      }
      case Circle(x,y,r)=>midpoint(bitmap,x,y,r,new Color(0,0,0))
      /*case (TextAt(x,y,s),"TEXT-AT")=>
      case (BoundingBox(x1,y1,x2,y2),"BOUNDING-BOX")=>{
          bresenham(bitmap,x1,y1,x1,y2,new Color(0,0,0))
          bresenham(bitmap,x1,y1,x2,y1,new Color(0,0,0))
          bresenham(bitmap,x1,y2,x2,y2,new Color(0,0,0))
          bresenham(bitmap,x2,y1,x2,y2,new Color(0,0,0))
        }*/
      case _ => return
    }
      Draw(lst, bitmap)}
  }

  val bitMapping = new RgbBitmap(0,0)
  val L = Line(1,1,5,5)
  val C = Circle(1,1,8)
  bitMapping.image.createGraphics()

  Draw(Cons(L,LstNil()),bitMapping)
  Draw(Cons(C,LstNil()),bitMapping)

  def Fill(c:String,g:Figure): Unit ={

  }

}