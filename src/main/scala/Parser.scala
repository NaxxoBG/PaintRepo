object Parser {

  sealed abstract class Figure;
  case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
  case class Circle(x: Int, y: Int, r: Int) extends Figure;
  case class TextAt(x: Int, y: Int, t: String) extends Figure;
  case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
  case class Draw(c: String, list: List[Figure]) extends Figure;
  case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure;
  case class Fill(c: String, figure: Figure) extends Figure;
  case class Error() extends Figure;

  val LineRegex = "\\((?i)Line\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
  val CircleRegex = "\\((?i)Circle\\((\\d+) (\\d+)\\) (\\d+)\\)".r
  val RectangleRegex = "\\((?i)Rectangle\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
  val TextAtRegex = "\\((?i)Text-At\\((\\d+) (\\d+)\\) (.*)\\)".r
  val DrawRegex = "\\((?i)Draw\\(([a-zA-Z]*) (.*)\\)\\)".r
  val BoundingBoxRegex = "\\((?i)Bounding-Box\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
  val FillRegex = "\\((?i)Fill\\(([a-zA-Z]*) (.*)\\)\\)".r


  object AsInt {
    def unapply(s: String) = try {
      Some(s.toInt)
    } catch {
      case e: NumberFormatException => None
    }
  }


  def ParseFromString(command: String): Figure = command match {
    case LineRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Line(x1, y1, x2, y2)
    case CircleRegex(AsInt(x), AsInt(y), AsInt(r)) => Circle(x, y, r)
    case RectangleRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Rectangle(x1, y1, x2, y2)
    case BoundingBoxRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => BoundingBox(x1, y1, x2, y2)
    case TextAtRegex(AsInt(x), AsInt(y), t) => TextAt(x, y, t)
    case FillRegex(c, figure) => Fill(c, ParseFromString(figure))
    case DrawRegex(c, t) => Draw(c, t.split(" \\(" ).map(cmd => ParseFromString(cmd)).toList)
    case _ => throw new NoSuchMethodException // could not parse command
  }


  //val draw = ParseFromString(DrawCommand)
  //val error = ParseFromString(errorCommand)


  def generateAbstractSyntaxTree(commands: String): List[Figure] = {
    commands.split("\\n").map(cmd => ParseFromString(cmd)).toList
  }




  // A valid syntax tree should only have one Bounding-box and not more
  // A valid syntax is also one with no parsing errors
  def IsSyntaxTreeValid(array: List[Figure]): Boolean =
    array.collect { case fig: BoundingBox => fig }.length == 1 &&
      array.collect { case fig: Error => fig }.length == 0




  def DrawSyntaxTree(commands: String): Unit = {
    val syntaxTree = generateAbstractSyntaxTree(commands);
    //if(IsSyntaxTreeValid(syntaxTree))
    //Draw(syntaxTree)
  }
}