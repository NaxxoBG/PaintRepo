package engine

import scala.util.matching.Regex
import java.util.Objects
import java.util.function.Predicate

package object engineValues {

  sealed abstract class Figure
  case class Line(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure
  case class Circle(x: Int, y: Int, r: Int) extends Figure
  case class TextAt(x: Int, y: Int, t: String) extends Figure
  case class Rectangle(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure
  case class Draw(c: String, list: List[Figure]) extends Figure
  case class BoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) extends Figure
  case class Fill(c: String, figure: Figure) extends Figure
  case class Nil() extends Figure
  case class Error(cmd: String) extends Figure

  val LineRegex: Regex = "\\((?i)Line\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
  val CircleRegex: Regex = "\\((?i)Circle\\((\\d+) (\\d+)\\) (\\d+)\\)".r
  val RectangleRegex: Regex = "\\((?i)Rectangle\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
  val TextAtRegex: Regex = "\\((?i)Text-At\\((\\d+) (\\d+)\\) (.*)\\)".r
  val DrawRegex: Regex = "\\((?i)Draw\\(([a-zA-Z]*) (.*)\\)\\)".r
  val BoundingBoxRegex: Regex = "\\((?i)Bounding-Box\\((\\d+) (\\d+)\\) \\((\\d+) (\\d+)\\)\\)".r
  val FillRegex: Regex = "\\((?i)Fill\\(([a-zA-Z]*) (.*)\\)\\)".r
  val validator: Regex = "(?i)(\\([a-z]+(?:\\-[a-z]+)?\\(\\d+ \\d+\\)(?: (?:\\d|[a-z ]+|\\(\\d+ \\d+\\)))?\\))".r

  object AsInt {
    def unapply(s: String): Option[Int] = try {
      Some(s.toInt)
    } catch {
      case _: NumberFormatException => None
    }
  }

  val isStringNotNull: Predicate[String] = s => !Objects.isNull(s)
  val isStringNotEmpty: Predicate[String] = s => !s.isEmpty
}
