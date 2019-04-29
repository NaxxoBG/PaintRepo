
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import au.controller.Parser._
import au.controller._


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
    assert(parseFromString(LineCommand) == Line(2,3,4,5))
    assert(parseFromString(LineCommand.toLowerCase()) == Line(2,3,4,5))
    assert(parseFromString(LineCommand.toUpperCase()) == Line(2,3,4,5))
  }

  test("CircleParser") {
    assert(parseFromString(CircleCommand) == Circle(1,2,2))
    assert(parseFromString(CircleCommand.toLowerCase()) == Circle(1,2,2))
    assert(parseFromString(CircleCommand.toUpperCase()) == Circle(1,2,2))
  }

  test("RectangleParser") {
    assert(parseFromString(RectangleCommand) == Rectangle(1,2,3,4))
    assert(parseFromString(RectangleCommand.toLowerCase()) == Rectangle(1,2,3,4))
    assert(parseFromString(RectangleCommand.toUpperCase) == Rectangle(1,2,3,4))
  }

  test("TextAtParser") {
    assert(parseFromString(TextAtCommand) == TextAt(1,2,"50%"))
    assert(parseFromString(TextAtCommand.toLowerCase()) == TextAt(1,2,"50%"))
    assert(parseFromString(TextAtCommand.toUpperCase()) == TextAt(1,2,"50%"))
  }

  test("BoundingBoxParser") {
    assert(parseFromString(boundingBoxCommand) == BoundingBox(2,1,2,3))
    assert(parseFromString(boundingBoxCommand.toLowerCase()) == BoundingBox(2,1,2,3))
    assert(parseFromString(boundingBoxCommand.toUpperCase()) == BoundingBox(2,1,2,3))
  }

  test("FillParser") {
    assert(parseFromString(fillCommand) == Fill("black", Circle(2,2,1)))
    assert(parseFromString(fillCommand.toLowerCase()) == Fill("black", Circle(2,2,1)))
    assert(parseFromString(fillCommand.toUpperCase()) == Fill("black".toUpperCase, Circle(2,2,1)))
  }

  test("DrawParser") {
    assert(parseFromString(DrawCommand) == Draw("black", List(Circle(2,3,1), Circle(1,2,3))))
    assert(parseFromString(DrawCommand.toLowerCase()) == Draw("black", List(Circle(2,3,1), Circle(1,2,3))))
    assert(parseFromString(DrawCommand.toUpperCase) == Draw("black".toUpperCase, List(Circle(2,3,1), Circle(1,2,3))))
  }

  test("ParseListOfCommand"){
    val builder = StringBuilder.newBuilder
    builder.append(boundingBoxCommand)
    builder.append(System.getProperty("line.separator"))
    builder.append(LineCommand)
    builder.append(System.getProperty("line.separator"))
    builder.append(LineCommand)
    val tree = generateAbstractSyntaxTree(builder.toString())
    assert(tree.length == 3)
    assert(isSyntaxTreeValid(tree))
  }


}
