import scala.xml.NodeSeq

class Levels(val levels : NodeSeq) {
  
  var index = 0
  val valid = 0 until levels.size

  def moveIndex(direction : Int) =
    if(valid contains (index + direction))
      index += direction
  
  def previous = moveIndex(-1)
  def next     = moveIndex(1)
  
  def level = new Level(levels(index))

}
