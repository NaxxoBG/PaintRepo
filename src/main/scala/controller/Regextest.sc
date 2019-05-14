import java.awt.Color

import au.controller.{AsInt, BoundingBox, Circle, Draw, Error, Figure, Fill, Line, Rectangle, TextAt}

import scala.util.matching.Regex

def getColorFromString(c: String): Color = {
  try {
    Class.forName("java.awt.Color").getField(c).get(null).asInstanceOf[Color]
  } catch {
    case _: Exception =>
      Color.BLACK // Not defined
  }
}

val LineRegex: Regex = "\\((?i)Line\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
val CircleRegex: Regex = "\\((?i)Circle\\((\\d+) (\\d+)\\) (\\d+)\\)".r
val RectangleRegex: Regex = "\\((?i)Rectangle\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
val TextAtRegex: Regex = "\\((?i)Text-At\\((\\d+) (\\d+)\\) (.*)\\)".r
val DrawRegex: Regex = "\\((?i)Draw\\(([a-zA-Z]*) (.*)\\)\\)".r
val BoundingBoxRegex: Regex = "\\((?i)Bounding-Box\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
val FillRegex: Regex = s"\\((?i)Fill\\(([a-zA-Z]*) (\\((?i)Circle\\(\\d+ \\d+\\) \\d+\\))\\)\\)".r
val validator: Regex = "(?i)(\\([a-z]+(?:\\-[a-z]+)?\\(\\d+ \\d+\\)(?: (?:\\d+|[a-z ]+|\\(\\d+ \\d+\\)))?\\))".r

val entry = "(Fill(yellow (Circle(80 80) 60)))"
val crle = "(Circle(80 80) 60)"
val dr = "(Draw(black (Circle(40 40) 20) (Circle(60 60) 30)))"
val ls = List("asd", "Asdadsa")

def parseFromString(command: String): Figure = command match {
  case LineRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Line(x1, y1, x2, y2)
  case CircleRegex(AsInt(x), AsInt(y), AsInt(r)) => Circle(x, y, r)
  case RectangleRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Rectangle(x1, y1, x2, y2)
  case BoundingBoxRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => BoundingBox(x1, y1, x2, y2)
  case TextAtRegex(AsInt(x), AsInt(y), t) => TextAt(x, y, t)
  case FillRegex(c, figure) => Fill(getColorFromString(c), parseFromString(figure))
  case DrawRegex(c, t) => Draw(getColorFromString(c), validator.findAllIn(t).map(cmd => parseFromString(cmd)).toList)
  case r => Error(r) // could not parse command
}

def generateAbstractSyntaxTree(commands: String): List[Figure] = {
  commands.split("\\r?\\n").map(cmd => parseFromString(cmd)).toList
}

val r = generateAbstractSyntaxTree(dr)

r.foreach(println)