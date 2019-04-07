object BresenhamLineAlgorithm {

  sealed abstract class Point;
  case class Coord(x: Int, y: Int) extends Point

  sealed abstract class PointList;
  case class PointListNil() extends PointList;
  case class PointListCons(crd: Coord, tl: PointList) extends PointList;

  def DeltaCalc(val1: Int, val2: Int): Int = {
    val dval = val2 - val1
    dval
  }

  def FindDErr(dx: Int, dy: Int): Double = {
    if(dx != 0)
      math.abs(dy / dx).toDouble
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

  def LineRc(x0: Int, y0: Int, x1: Int, y1: Int, err: Double, list: PointList): PointList = //list match
  {
    val dx = DeltaCalc(x0, x1)
    val dy = DeltaCalc(y0, y1)
    val resultList= concat(list, PointListCons(Coord(x0, y0), PointListNil()))
    if (dx == 0 && dy == 0) {
      return resultList
    } //Done
    if (dx == 0 || dy == 0) {
      LineRc(x0 + Sign(dx), y0 + Sign(dy), x1, y1, err, resultList)
    } //is vertical or horizontal

    val derr = FindDErr(dx, dy)
    val ploterr = err + derr

    if (ploterr >= 0.5)
      LineRc(x0 + Sign(dx), y0 + Sign(dy), x1, y1, ploterr - 1.0, resultList)
    else
      LineRc(x0 + Sign(dx), y0, x1, y1, ploterr, resultList)
  }

  def LineWrapper(x0: Int, y0: Int, x1: Int, y1: Int): PointList = {
    LineRc(x0, y0, x1, y1, 0, PointListNil())
  }

  def midpoint(x0: Int, y0: Int, radius: Int, xRest: Int, list: PointList): PointList = {
    var x = xRest
    var y = radius
    var resultList = list
    if (x < y) {
      var f = 1 - radius
      var ddF_x = 1
      var ddF_y = -2 * radius
      resultList = PointListCons(Coord(x0, y0 + radius), resultList)
      resultList = PointListCons(Coord(x0, y0 - radius), resultList)
      resultList = PointListCons(Coord(x0 + radius, y0), resultList)
      resultList = PointListCons(Coord(x0 - radius, y0), resultList)
      if (f >= 0) {
        y -= 1
        ddF_y += 2
        f += ddF_y
      }
      x += 1
      ddF_x += 2
      f += ddF_x
      resultList = PointListCons(Coord(x0 + x, y0 + y), resultList)
      resultList = PointListCons(Coord(x0 - x, y0 + y), resultList)
      resultList = PointListCons(Coord(x0 + x, y0 - y), resultList)
      resultList = PointListCons(Coord(x0 - x, y0 - y), resultList)
      resultList = PointListCons(Coord(x0 + y, y0 + x), resultList)
      resultList = PointListCons(Coord(x0 - y, y0 + x), resultList)
      resultList = PointListCons(Coord(x0 + y, y0 - x), resultList)
      resultList = PointListCons(Coord(x0 - y, y0 - x), resultList)
      midpoint(x0, y0, radius, x, resultList)
    }
    return resultList
  }

  def midpointWrapper(x0: Int, y0: Int, radius: Int): PointList = {
    midpoint(x0, y0, radius, 0, PointListNil())
  }

  /*def DrawLine(list: IntList,x0:Int,y0:Int,x1:Int,y1:Int)={
    LineRc(list,x0,y0,x1,y1,0.0,y0)
  }*/

}
