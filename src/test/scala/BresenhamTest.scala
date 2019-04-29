
import au.controller.BresenhamLineAlgorithm._
import au.controller.BresenhamLineAlgorithm
import org.scalatest.FunSuite



class BresenhamTest extends FunSuite{

  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block    // call-by-name
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ns")
    result
  }



  test("MidpointTestRadius1"){
    val expectedPointList = PointListCons(Coord(1,0), PointListCons(Coord(0,1), PointListCons(Coord(-1,0), PointListCons(Coord(0,-1), PointListNil()))))
    val pointList = BresenhamLineAlgorithm.midpointWrapper(0,0, 1)
    printPointList(pointList)
  }

  test("MidpointTestRadius3"){
    val pointList = BresenhamLineAlgorithm.midpointWrapper(110,110, 50)
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

  test("LineTestNot45degrees")
  {
    val expectedPointList = PointListCons(Coord(20,10), PointListCons(Coord(21,11), PointListCons(Coord(22,12), PointListCons(Coord(23,12), PointListCons(Coord(24,13),
      PointListCons(Coord(25, 14), PointListCons(Coord(26,15), PointListCons(Coord(27,16), PointListCons(Coord(28, 16),
        PointListCons(Coord(29,17), PointListCons(Coord(30, 18), PointListNil())))))))))))
    val pointList = BresenhamLineAlgorithm.LineWrapper(20,10, 30,18)
    printPointList(pointList)
    assert(expectedPointList == pointList)
  }

  test("RectangleCubeSide2NotFilled"){
    val expectedPointList = PointListCons(Coord(2,1), PointListCons(Coord(1,2), PointListCons(Coord(2, 2), PointListCons(Coord(1,0), PointListCons(Coord(2,0),
      PointListCons(Coord(0, 0), PointListCons(Coord(0,1), PointListCons(Coord(0,2), PointListNil()))))))))
    val pointList = BresenhamLineAlgorithm.getPointRectangle(0,0,2,2, fill = false)
    printPointList(pointList)
    assert(pointList == expectedPointList)
  }

  test("RectangleTimeTest1"){
    val pointList = time{BresenhamLineAlgorithm.getPointRectangle(0,0,200,200, fill = false)}
    val pointList2 = time{BresenhamLineAlgorithm.getPointRectangle(0,0,200,200, fill = true)}
  }

  test("RectangleTimeTest2"){
    val pointList = time{BresenhamLineAlgorithm.getPointRectangle(0,0,50,50, fill = false)}
  }

  test("RectangleTimeTest3"){
    val pointList = time{BresenhamLineAlgorithm.getPointRectangle(0,0,10,10, fill = false)}
  }

  test("RectangleCubeSide2Filled"){
    val expectedPointList = PointListCons(Coord(2,0), PointListCons(Coord(2,1), PointListCons(Coord(2, 2), PointListCons(Coord(1,0), PointListCons(Coord(1,1),
      PointListCons(Coord(1, 2), PointListCons(Coord(0,0), PointListCons(Coord(0,1), PointListCons(Coord(0,2), PointListNil())))))))))
    val pointList = BresenhamLineAlgorithm.getPointRectangle(0,0,2,2, fill = true)
    printPointList(pointList)
    assert(pointList == expectedPointList)
  }
}
