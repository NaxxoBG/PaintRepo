package au.controller

import scala.annotation.tailrec

object BresenhamLineAlgorithm {

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
    val x0org = math.min(x0,x1)
    val x1org = math.max(x0,x1)
    val y0org = math.min(y0,y1)
    val y1org = math.max(y0,y1)
    val dx = DeltaCalc(x0org, x1org)
    val dy = DeltaCalc(y0org, y1org)
    val resultList = concat(list, PointListCons(Coord(x0org, y0org), PointListNil()))
    if (dx == 0 && dy == 0) {
      return resultList
    } //Done
    if (dx == 0 || dy == 0) {
      return LineRc(x0org + Sign(dx), y0org + Sign(dy), x1org, y1org, err, resultList)
    } //is vertical or horizontal

    val der = FindDErr(dx, dy)
    val plotErr = err + der

    if (plotErr >= 0.5)
      LineRc(x0org + Sign(dx), y0org + Sign(dy), x1org, y1org, plotErr - 1.0, resultList)
    else
      LineRc(x0org + Sign(dx), y0org, x1org, y1org, plotErr, resultList)
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

  def findLeftPointOfCircleAtY(points: PointList, y0: Int): Int = points match {
    case PointListNil() => 0
    case PointListCons(Coord(x, y), tl) =>
      if (y == y0)
        x
      else
        findLeftPointOfCircleAtY(tl, y0)
  }

  @tailrec
  def fillCircle(y0: Int, x0: Int, y: Int, radius: Int, points: PointList): PointList = {
    if(y == y0 - radius) return points
    val d = math.abs(x0 - findLeftPointOfCircleAtY(points, y))
    fillCircle(y0, x0, y - 1, radius, concat(LineWrapper(x0 - d, y, x0 + d, y), points))
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
