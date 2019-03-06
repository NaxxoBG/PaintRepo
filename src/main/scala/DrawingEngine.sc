
sealed abstract class Figure;
case class Line(x1: Int, y1: Int, x2: Int, y2: Int)  extends Figure;
case class Circle(x: Int, y: Int, r: Int) extends Figure;
case class TextAt(x: Int, y: Int, t: String) extends Figure;
case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
case class Draw(c: String, list: List[Figure]) extends Figure;
case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
case class Fill(c: String, figure: Figure) extends Figure;
case class Error() extends Figure;


val LineRegex = "Line\\((\\d+),(\\d+),(\\d+),(\\d+)\\)".r
val CircleRegex = "Circle\\((\\d+),(\\d+),(\\d+)\\)".r
val RectangleRegex = "Rectangle\\((\\d+),(\\d+),(\\d+),(\\d+)\\)".r
val TextAtRegex = "Text-At\\((\\d+),(\\d+),(.*)\\)".r
//val DrawRegex = "Draw\\(([a-zA-Z]*),(.*+?)\\)".r
val BoundingBoxRegex = "Bounding-Box\\((\\d+),(\\d+),(\\d+),(\\d+)\\)".r
val FillRegex = "Fill\\(([a-zA-Z]*),(.*)\\)".r

val LineCommand = "Line(2,3,4,5)"
val CircleCommand = "Circle(1,2,3)"
val RectangleCommand = "Rectangle(1,2,3,4)"
val TextAtCommand = "Text-At(1,2,50%)"
val DrawCommand = "Draw(black, Circle(2,3,1))"
val boundingBoxCommand = "Bounding-Box(2,1,2,3)"
val fillCommand = "Fill(black,Circle(2,2,1))"
val errorCommand = "Fill(black, black)"


object AsInt {
  def unapply(s: String) = try{ Some(s.toInt) } catch {
    case e: NumberFormatException => None
  }
}

def ParseFromString(command: String): Figure  = command match {
  case LineRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Line(x1, y1, x2, y2)
  case CircleRegex(AsInt(x), AsInt(y), AsInt(r)) => Circle(x, y, r)
  case RectangleRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Rectangle(x1, y1, x2, y2)
  case BoundingBoxRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => BoundingBox(x1, y1, x2, y2)
  case TextAtRegex(AsInt(x), AsInt(y), t) => TextAt(x, y, t)
  case FillRegex(c, figure) => Fill(c, ParseFromString(figure))
  //case DrawRegex(c, t) => Draw(c, ParseFromString(t))
  case _ => Error() // could not parse command
}

//val l = ParseFromString(LineCommand)
//val c = ParseFromString(CircleCommand)
//val r = ParseFromString(RectangleCommand)
//val t = ParseFromString(TextAtCommand)
//val bB = ParseFromString(boundingBoxCommand)
val fill = ParseFromString(fillCommand)
val error = ParseFromString(errorCommand)
