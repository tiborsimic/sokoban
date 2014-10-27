import scala.xml.{XML, Elem, Node}

class Level(node : Node) {
  
  val layout = LayoutReader.constructLayout((node \ "L").map(_.text).toList)
  val title  = (node \\ "@Id").text

}
