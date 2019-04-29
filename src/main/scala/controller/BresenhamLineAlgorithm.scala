package au.controller

import scala.annotation.tailrec

object BresenhamLineAlgorithm {

  sealed abstract class Point
  case class Coord(x: Int, y: Int) extends Point

  sealed abstract class PointList
  case class PointListNil() extends PointList
  case class PointListCons(crd: Coord, tl: PointList) extends PointList

  def printPointList(list: PointList):Unit= list match {
    case PointListNil() =>
    case PointListCons(Coord(x,y), tl) => (printf("Point x:%d, y:%d \n", x, y), printPointList(tl))
  }

  def DeltaCalc(val1: Int, val2: Int): Int = {
    val2 - val1
  }

  def FindDErr(dx: Int, dy: Int): Double = {
    if (dx != 0)
      math.abs(dy.toDouble / dx.toDouble)
    else
      dy
  }

  def Sign(num: Int): Int = {
    if (num > 0)
      1
    else if (num < 0)
      -1
    else
      0
  }

  def concat(list1: PointList, list2: PointList): PointList = list1 match {
    case PointListNil() => list2
    case PointListCons(hd, tl) => PointListCons(hd, concat(tl, list2))
  }


  def fillRectangle(x1: Int, y1: Int, x2: Int, y2: Int): PointList = {
    @tailrec
    def fillRectangleTailRec(x1: Int, y1: Int, x2: Int, y2: Int, pointList: PointList): PointList = {
      if (x1 > x2) return pointList
      fillRectangleTailRec(x1 + 1, y1, x2, y2, concat(LineWrapper(x1, y1, x1, y2), pointList))
    }

    fillRectangleTailRec(x1, y1, x2, y2, PointListNil())
  }

  def getPointRectangle(x1: Int, y1: Int, x2: Int, y2: Int, fill: Boolean): PointList = {
    if (fill) {
      fillRectangle(x1, y1, x2, y2)
    } else {
      var points = LineWrapper(x1, y1, x1, y2)
      points = concat(LineWrapper(x1 + 1, y1, x2, y1), points) //This will avoid duplicate pixels of the corners
      points = concat(LineWrapper(x1 + 1, y2, x2, y2), points)
      concat(LineWrapper(x2, y1 + 1, x2, y2 - 1), points)
    }
  }

  def LineRc(x0: Int, y0: Int, x1: Int, y1: Int, err: Double, list: PointList): PointList = //list match
  {
    val dx = DeltaCalc(x0, x1)
    val dy = DeltaCalc(y0, y1)
    val resultList = concat(list, PointListCons(Coord(x0, y0), PointListNil()))
    if (dx == 0 && dy == 0) {
      return resultList
    } //Done
    if (dx == 0 || dy == 0) {
      return LineRc(x0 + Sign(dx), y0 + Sign(dy), x1, y1, err, resultList)
    } //is vertical or horizontal

    val der = FindDErr(dx, dy)
    val plotErr = err + der

    if (plotErr >= 0.5)
      LineRc(x0 + Sign(dx), y0 + Sign(dy), x1, y1, plotErr - 1.0, resultList)
    else
      LineRc(x0 + Sign(dx), y0, x1, y1, plotErr, resultList)
  }

  def LineWrapper(x0: Int, y0: Int, x1: Int, y1: Int): PointList = {
    LineRc(x0, y0, x1, y1, 0, PointListNil())
  }

  def CircleCoordinatesEvery45deg(x0: Int, y0: Int, x: Int, y: Int, pointList: PointList): PointList = {
    var resultList = PointListCons(Coord(x0 + x, y0 + y), pointList)
    resultList = PointListCons(Coord(x0 - x, y0 + y), resultList)
    resultList = PointListCons(Coord(x0 + x, y0 - y), resultList)
    resultList = PointListCons(Coord(x0 - x, y0 - y), resultList)
    resultList = PointListCons(Coord(x0 + y, y0 + x), resultList)
    resultList = PointListCons(Coord(x0 - y, y0 + x), resultList)
    resultList = PointListCons(Coord(x0 + y, y0 - x), resultList)
    resultList = PointListCons(Coord(x0 - y, y0 - x), resultList)
    resultList
  }

  def GetTheFourCornersOfCircle(x0: Int, y0: Int, radius: Int): PointList = {
    var resultList = PointListCons(Coord(x0, y0 + radius), PointListNil())
    resultList = PointListCons(Coord(x0, y0 - radius), resultList)
    resultList = PointListCons(Coord(x0 + radius, y0), resultList)
    resultList = PointListCons(Coord(x0 - radius, y0), resultList)
    resultList
  }

  def midpointWrapper(x0: Int, y0: Int, radius: Int): PointList = {
    @tailrec
    def midPoint(f: Int, x0: Int, y0: Int, x_inner: Int, y_inner: Int, ddFX: Int, ddFY: Int, pointList: PointList): PointList = {
      if (x_inner < y_inner) {
        var y_change = y_inner
        var f_change = f
        var ddFY_change = ddFY

        if (f_change >= 0) {
          y_change = y_change - 1
          ddFY_change = ddFY_change +  2
          f_change = f_change + ddFY_change
        }
        val x_change = x_inner + 1
        val ddFX_change = ddFX + 2
        f_change += ddFX_change
        midPoint(f_change, x0, y0, x_change, y_change, ddFX_change, ddFY_change, CircleCoordinatesEvery45deg(x0, y0, x_change, y_change, pointList))
      }
      else {
        pointList
      }
    }

    midPoint(1 - radius, x0, y0, 0, radius, 1, -2*radius, GetTheFourCornersOfCircle(x0, y0, radius))
  }

}
