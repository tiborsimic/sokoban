import scala.xml.{XML, Elem, NodeSeq}

object Main {
  def main(args: Array[String]): Unit = {
   
    var data   : Elem  = null
    var levels : NodeSeq = null
    
    try {
      data   = XML.loadFile(args(0))
      levels = data \\ "Level"                      
    } 
    catch {
      case ex : java.lang.IndexOutOfBoundsException => {println("usage: Main <file>") ; return}
      case ex : java.io.FileNotFoundException       => {println("File not found.")    ; return}
    }
       
    val game  = new GameFrame(new Levels(levels))
    game.setVisible(true)

  }
}
