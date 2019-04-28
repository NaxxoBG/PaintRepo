package engine

import engineValues._

object Parser {

  def parseFromString(command: String): Figure = command match {
    case LineRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Line(x1, y1, x2, y2)
    case CircleRegex(AsInt(x), AsInt(y), AsInt(r)) => Circle(x, y, r)
    case RectangleRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => Rectangle(x1, y1, x2, y2)
    case BoundingBoxRegex(AsInt(x1), AsInt(y1), AsInt(x2), AsInt(y2)) => BoundingBox(x1, y1, x2, y2)
    case TextAtRegex(AsInt(x), AsInt(y), t) => TextAt(x, y, t)
    case FillRegex(c, figure) => Fill(c, parseFromString(figure))
    case DrawRegex(c, t) => Draw(c, validator.findAllIn(t).map(cmd => parseFromString(cmd)).toList)
    case r => Error(r) // could not parse command
  }

  def generateAbstractSyntaxTree(commands: String): List[Figure] = {
    commands.split("\\r?\\n").map(cmd => parseFromString(cmd)).toList
  }

  /** The bounding box should be the first element of the list <br >
    * A valid syntax tree should only have one Bounding-box and not more <br >
    * A valid syntax is also one with no parsing errors
    * @param array <code>List[Figure]</code>
    * @return Boolean
    */
  def isSyntaxTreeValid(array: List[Figure]): Boolean =
    array.head.isInstanceOf[BoundingBox] &&
    array.collect { case fig: BoundingBox => fig }.length == 1 &&
      array.collect { case fig: Error => fig }.isEmpty
}
