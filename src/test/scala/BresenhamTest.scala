import BresenhamLineAlgorithm.{Coord, PointList, PointListCons, PointListNil}
import org.scalatest.FunSuite


class BresenhamTest extends FunSuite{


  def printPointList(list: PointList):Unit= list match {
    case PointListNil() => return
    case PointListCons(Coord(x,y), tl) => (printf("Point x:%d, y:%d \n", x, y), printPointList(tl))
  }

  test("MidpointTestRadius1"){
    val expectedPointList = PointListCons(Coord(1,0), PointListCons(Coord(0,1), PointListCons(Coord(-1,0), PointListCons(Coord(0,-1), PointListNil()))))
    val pointList = BresenhamLineAlgorithm.midpointWrapper(0,0, 1)
    printPointList(pointList)
  }

  test("MidpointTestRadius3"){
    val pointList = BresenhamLineAlgorithm.midpointWrapper(0,0, 3)
    printPointList(pointList)
  }

  test("LineTestHorizontalLine")
  {
    val expectedPointList = PointListCons(Coord(0,0), PointListCons(Coord(1,0), PointListCons(Coord(2,0), PointListCons(Coord(3,0), PointListNil()))))
    val pointList = BresenhamLineAlgorithm.LineWrapper(0,0, 3,0)
    print(pointList)
    assert(expectedPointList == pointList)

  }

  test("LineTestVerticalLine")
  {
    val expectedPointList = PointListCons(Coord(0,0), PointListCons(Coord(0,1), PointListCons(Coord(0,2), PointListCons(Coord(0,3), PointListNil()))))
    val pointList = BresenhamLineAlgorithm.LineWrapper(0,0, 0,3)
    print(pointList)
    assert(expectedPointList == pointList)
  }

  test("LineTest45degrees")
  {
    val expectedPointList = PointListCons(Coord(0,0), PointListCons(Coord(1,1), PointListCons(Coord(2,2), PointListCons(Coord(3,3), PointListNil()))))
    val pointList = BresenhamLineAlgorithm.LineWrapper(0,0, 3,3)
    print(pointList)
    assert(expectedPointList == pointList)
  }


}
