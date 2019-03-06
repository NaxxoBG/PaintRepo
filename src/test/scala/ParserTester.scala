import Parser._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class ParserTester extends FunSuite with BeforeAndAfter{
  val LineCommand = "(LinE(2 3) (4 5))"
  val CircleCommand = "(Circle(1 2) 2)"
  val RectangleCommand = "(Rectangle(1 2) (3 4))"
  val TextAtCommand = "(Text-At(1 2) 50%)"
  val DrawCommand = "(Draw(black, Circle(2,3,1)))"
  val boundingBoxCommand = "(Bounding-Box(2 1) (2 3))"
  val fillCommand = "(Fill(black Circle(2 2) 1))"
  val errorCommand = "Fill(black, black)"


  test("LineParser") {
    assert(ParseFromString(LineCommand) == Line(2,3,4,5))
  }

  test("CircleParser") {
    assert(ParseFromString(CircleCommand) == Circle(1,2,2))
  }

  test("RectangleParser") {
    assert(ParseFromString(RectangleCommand) == Rectangle(1,2,3,4))
  }

  test("TextAtParser") {
    assert(ParseFromString(TextAtCommand) == TextAt(1,2,"50%"))
  }

  test("BoundingBoxParser") {
    assert(ParseFromString(boundingBoxCommand) == BoundingBox(2,1,2,3))
  }

  test("ParseListOfCommand"){
    val builder = StringBuilder.newBuilder
    builder.append(LineCommand)
    builder.append(System.getProperty("line.separator"))
    builder.append(LineCommand)
    builder.append(System.getProperty("line.separator"))
    builder.append(boundingBoxCommand)

    val tree = generateAbstractSyntaxTree(builder.toString())
    assert(tree.length == 3)
    assert(IsSyntaxTreeValid(tree))
  }


}
