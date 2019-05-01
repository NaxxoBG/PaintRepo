package au.controller

import java.awt.Color
import java.awt.image.BufferedImage

import au.controller.BresenhamLineAlgorithm._
import au.controller.Parser._
import au.model.RgbBitmap

import scala.annotation.tailrec

object DrawingEngine {

  def pointDrawer(pointList: PointList, bm: RgbBitmap, c: Color): Unit = pointList match {
    case PointListNil() => Unit
    case PointListCons(hd, tl) => bm.setPixel(hd.x, hd.y, c); pointDrawer(tl, bm, c)
  }

  def drawLine(bm: RgbBitmap, c: Color, x0: Int, y0: Int, x1: Int, y1: Int): Unit = {
    pointDrawer(LineWrapper(x0, y0, x1, y1), bm, c)
  }

  def drawCircle(bm: RgbBitmap, c: Color, x0: Int, y0: Int, radius: Int, boundingBox: BoundingBox, fill: Boolean): Unit = {
    var points = BresenhamLineAlgorithm.midpointWrapper(x0, y0, radius)
    if (fill)
      points = BresenhamLineAlgorithm.fillCircle(y0, x0, radius + y0, radius, points)
    points = removePointsOutsideBoundingBox(points, boundingBox)
    pointDrawer(points, bm, c)
  }

  def drawRectangle(x1: Int, y1: Int, x2: Int, y2: Int, bitmap: RgbBitmap, c: Color, fill: Boolean): Unit = {
    val points = getPointRectangle(x1, y1, x2, y2, fill)
    pointDrawer(points, bitmap, c)
  }

  def outsideBoundBox(x: Int, y: Int, boundingBox: BoundingBox): Boolean = {
    x < boundingBox.x1 || x > boundingBox.x2 || y < boundingBox.y1 || y > boundingBox.y2
  }

  def removePointsOutsideBoundingBox(points: PointList, boundingBox: BoundingBox): PointList = {
    @tailrec
    def removePoints(points: PointList, boundingBox: BoundingBox, resultPoints: PointList): PointList = points match {
      case PointListNil() => resultPoints;
      case PointListCons(Coord(x, y), tl) =>
        if (outsideBoundBox(x, y, boundingBox))
          removePoints(tl, boundingBox, resultPoints)
        else removePoints(tl, boundingBox, PointListCons(Coord(x, y), resultPoints))
    }
    val resultPointList = PointListNil()
    removePoints(points, boundingBox, resultPointList)
  }

  def getColorFromString(c: String): Color = {
    try {
      return Class.forName("java.awt.Color").getField(c).get(null).asInstanceOf[Color]
    } catch {
      case e: Exception =>
        return Color.BLACK // Not defined

    }
  }

  @tailrec
  def drawImg(figureList: List[Figure], boundingBox: BoundingBox, bitmap: RgbBitmap): Unit = figureList match {
    case List() => Unit
    case f :: tl =>
      f match {
        case Fill(c, fig) => fig match {
          case Circle(x, y, r) => drawCircle(bitmap, getColorFromString(c), x, y, r, boundingBox, true)
          case Rectangle(x1, y1, x2, y2) =>
            drawRectangle(math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2), bitmap, getColorFromString(c), fill = true)
          case _ => return
        }
        case Line(x1, y1, x2, y2) => drawLine(bitmap, Color.BLACK, math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2))
        case Rectangle(x1, y1, x2, y2) =>
          drawRectangle(math.max(x1, boundingBox.x1), math.max(y1, boundingBox.y1), math.min(x2, boundingBox.x2), math.min(y2, boundingBox.y2), bitmap, Color.BLACK, fill = false)
        case Circle(x, y, r) => drawCircle(bitmap, Color.BLACK, x, y, r, boundingBox, fill = false)
        case TextAt(x, y, s) => bitmap.writeText(s, x, y)
        case BoundingBox(x1, y1, x2, y2) => drawRectangle(x1, y1, x2, y2, bitmap, Color.BLACK, fill = false)
        case _ => return
      }
      drawImg(tl, boundingBox, bitmap)
  }

  /**
    * The method assumes the first element of the syntax tree is a BoundingBox. It draws all figures instantly, based on the passed <br >
    * command, and returns the BufferedImage.
    *
    * @param commands String
    * @return BufferedImage
    */
  def drawSyntaxTree(commands: String, width: Int, height: Int): BufferedImage = {
    val bitMapping = new RgbBitmap(width, height)
    bitMapping.image.createGraphics()
    bitMapping.fill(Color.WHITE)
    val syntaxTree = generateAbstractSyntaxTree(commands)
    if (isSyntaxTreeValid(syntaxTree)) {
      drawImg(syntaxTree, syntaxTree.head.asInstanceOf[BoundingBox], bitMapping)
    }
    bitMapping.image
  }
}
