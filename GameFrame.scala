import java.awt.{Color, Graphics, Graphics2D, Toolkit}
import javax.swing.{JFrame, JOptionPane}
import java.awt.event.{KeyEvent, KeyAdapter}
import scala.collection.mutable.Stack

class GameFrame(levels : Levels) extends JFrame {
 
  val windowWidth = 800
  val windowHeight = 600
  
  var level    = levels.level
  var renderer = new LayoutRenderer(level.layout, windowWidth, windowHeight)

  var currentLayout = level.layout  
  var undoStack     = new Stack[Layout]() 
   
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(windowWidth, windowHeight)
  setResizable(false)
  setFocusable(true)
  setLocationRelativeTo(null)
  setTitle(level.title)
  setFocusable(true)
  addKeyListener(new GameKeyAdapter)

  override def paint(g : Graphics) = {
    def g2d = g.asInstanceOf[Graphics2D]
    g2d.drawImage(renderer.renderScene(currentLayout), 0, 0, null)
    
  }

  def isSolved = currentLayout.isSolved

  def handleMovement(key : Int) = {
    val events = Map(
      KeyEvent.VK_LEFT -> Layout.left _, 
      KeyEvent.VK_RIGHT -> Layout.right _,
      KeyEvent.VK_UP -> Layout.up _,
      KeyEvent.VK_DOWN -> Layout.down _)
    
    val newPlan = currentLayout.moveDude(events(key))    
    if(!(newPlan.isEmpty)) {
      val newLayout = newPlan.get
      if(currentLayout.cratesPos != newLayout.cratesPos)
        undoStack.push(currentLayout)
      currentLayout = newLayout
      repaint()
      if(isSolved) {
        JOptionPane.showMessageDialog(this, "YOU ROCK HARD")
        nextLevel
      }       
    }
  }

  def undo = {
    if(!(undoStack.isEmpty)) {
      currentLayout = undoStack.pop
      repaint()
    } else reset
  }
  
  def reset = {
    undoStack = new Stack[Layout]() 
    level = levels.level  
    currentLayout = level.layout
    renderer = new LayoutRenderer(level.layout, windowWidth, windowHeight)
    setTitle(level.title)
    repaint()
  }
   
  def nextLevel = {
    levels.next
    reset
  }
 
  def previousLevel = {
    levels.previous
    reset    
  }

  class GameKeyAdapter extends KeyAdapter {
    
    override def keyPressed(e : KeyEvent) = {
      val key = e.getKeyCode
      (key match {
        case KeyEvent.VK_LEFT | KeyEvent.VK_RIGHT | KeyEvent.VK_UP | KeyEvent.VK_DOWN => (() => handleMovement(key))
        case KeyEvent.VK_R => reset _
        case KeyEvent.VK_X => undo  _
        case KeyEvent.VK_BACK_SPACE => previousLevel _ 
        case KeyEvent.VK_SPACE => nextLevel _
        case _ => (() => Unit)
      }).apply
    }           
  }
}

