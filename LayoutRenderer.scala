import java.awt.{Graphics, Graphics2D, Color}
import java.awt.image.BufferedImage
import java.awt.geom.AffineTransform

class LayoutRenderer(val baseLayout: Layout, windowWidth : Int, windowHeight : Int) {

  val tileWidth  = 20
  val tileHeight = 20

  def maxWidth  = baseLayout.maxY * tileWidth
  def maxHeight = baseLayout.maxX * tileHeight

  val offsetWidth  = (windowWidth  - maxWidth)  / 2
  val offsetHeight = (windowHeight - maxHeight) / 2

  val offset = new AffineTransform
  offset.translate(offsetWidth, offsetHeight)

  val terrainBuffer = renderTerrain

  def renderMovable(canvas : Graphics2D, coords : (Int, Int), color : Color) = {
    val x = coords._2
    val y = coords._1
    canvas.setColor(color.darker)
    canvas.fillRoundRect(x  * tileWidth + 2, y * tileHeight + 2, tileWidth - 2, tileHeight - 2, 25, 25)
    canvas.setColor(color)
    canvas.fillRoundRect(x  * tileWidth + 1, y * tileHeight + 1, tileWidth - 3, tileHeight - 3, 25, 25)
  }

  def renderPassable(canvas : Graphics2D, coords : (Int, Int), color : Color) = {
    val x = coords._2
    val y = coords._1
    canvas.setColor(Color.BLACK)
    canvas.fillRect(x  * tileWidth, y * tileHeight, tileWidth, tileHeight)
    canvas.setColor(color)
    canvas.fillRect(x  * tileWidth + 2, y * tileHeight + 2, tileWidth - 2, tileHeight - 2)
  }

  def renderGoal(canvas : Graphics2D, coords : (Int, Int)) = 
    renderPassable(canvas, coords, new Color(249, 204, 202))

  def renderWall(canvas : Graphics2D, coords : (Int, Int)) = {
    val x = coords._2
    val y = coords._1
    canvas.setColor(new Color(8, 146, 208))
    canvas.fillRect(x  * tileWidth, y * tileHeight, tileWidth, tileHeight)
  }

  def renderFloor(canvas : Graphics2D, coords : (Int, Int)) =
    renderPassable(canvas, coords, new Color(125, 249, 255))

  def renderCrate(canvas : Graphics2D, coords : (Int, Int)) =
    renderMovable(canvas, coords, new Color(202, 31, 123))

  def renderDude(canvas : Graphics2D, coords : (Int, Int)) =
    renderMovable(canvas, coords, new Color(255, 255, 0))

  def renderTerrain : BufferedImage = {

    val buffer    : BufferedImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB)
    val graphics  : Graphics2D = buffer.createGraphics().asInstanceOf[Graphics2D]

    for (tile <- baseLayout.terrain) {
      (tile._2 match {
	    case Floor () => renderFloor _
	    case Goal  () => renderGoal  _
	    case _        => renderWall  _
	  }).apply(graphics, tile._1)
	}
	return buffer
  }

  def renderScene(currentLayout : Layout) : BufferedImage = {

    val buffer   : BufferedImage = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB)
    val graphics : Graphics2D = buffer.createGraphics().asInstanceOf[Graphics2D]
        
    graphics.setBackground(Color.BLACK)
    graphics.clearRect(0, 0, maxWidth, maxHeight)

    graphics.setTransform(offset)
    graphics.drawImage(terrainBuffer, 0, 0, null)
       
    for (tile <- currentLayout.cratesPos) {
      renderCrate(graphics, tile)
    }

    renderDude(graphics, currentLayout.dudePos)

    return buffer
  }
}
