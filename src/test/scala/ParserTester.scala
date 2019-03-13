import Parser._
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class ParserTester extends FunSuite with BeforeAndAfter{
  val LineCommand = "(LinE(2 3) (4 5))"
  val CircleCommand = "(Circle(1 2) 2)"
  val RectangleCommand = "(Rectangle(1 2) (3 4))"
  val TextAtCommand = "(Text-At(1 2) 50%)"
  val DrawCommand = "(Draw(black (Circle(2 3) 1) (Circle(1 2) 3)))"
  val boundingBoxCommand = "(Bounding-Box(2 1) (2 3))"
  val fillCommand = "(Fill(black (Circle(2 2) 1)))"
  val errorCommand = "Fill(black, black)"


  test("LineParser") {
    assert(ParseFromString(LineCommand) == Line(2,3,4,5))
    assert(ParseFromString(LineCommand.toLowerCase()) == Line(2,3,4,5))
    assert(ParseFromString(LineCommand.toUpperCase()) == Line(2,3,4,5))
  }

  test("CircleParser") {
    assert(ParseFromString(CircleCommand) == Circle(1,2,2))
    assert(ParseFromString(CircleCommand.toLowerCase()) == Circle(1,2,2))
    assert(ParseFromString(CircleCommand.toUpperCase()) == Circle(1,2,2))
  }

  test("RectangleParser") {
    assert(ParseFromString(RectangleCommand) == Rectangle(1,2,3,4))
    assert(ParseFromString(RectangleCommand.toLowerCase()) == Rectangle(1,2,3,4))
    assert(ParseFromString(RectangleCommand.toUpperCase) == Rectangle(1,2,3,4))
  }

  test("TextAtParser") {
    assert(ParseFromString(TextAtCommand) == TextAt(1,2,"50%"))
    assert(ParseFromString(TextAtCommand.toLowerCase()) == TextAt(1,2,"50%"))
    assert(ParseFromString(TextAtCommand.toUpperCase()) == TextAt(1,2,"50%"))
  }

  test("BoundingBoxParser") {
    assert(ParseFromString(boundingBoxCommand) == BoundingBox(2,1,2,3))
    assert(ParseFromString(boundingBoxCommand.toLowerCase()) == BoundingBox(2,1,2,3))
    assert(ParseFromString(boundingBoxCommand.toUpperCase()) == BoundingBox(2,1,2,3))
  }

  test("FillParser") {
    assert(ParseFromString(fillCommand) == Fill("black", Circle(2,2,1)))
    assert(ParseFromString(fillCommand.toLowerCase()) == Fill("black", Circle(2,2,1)))
    assert(ParseFromString(fillCommand.toUpperCase()) == Fill("black".toUpperCase, Circle(2,2,1)))
  }

  test("DrawParser") {
    assert(ParseFromString(DrawCommand) == Draw("black", List(Circle(2,3,1), Circle(1,2,3))))
    assert(ParseFromString(DrawCommand.toLowerCase()) == Draw("black", List(Circle(2,3,1), Circle(1,2,3))))
    assert(ParseFromString(DrawCommand.toUpperCase) == Draw("black".toUpperCase, List(Circle(2,3,1), Circle(1,2,3))))
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
