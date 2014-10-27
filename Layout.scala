class Layout(val dudePos   : (Int, Int),
             val cratesPos : Set[(Int, Int)],
             val terrain      : Map[(Int, Int), Terrain]) {


  def maxX = terrain.keys.map(_._1).max + 1
  def maxY = terrain.keys.map(_._2).max + 1

  // predicate for winning the game

  def isSolved = (cratesPos map {terrain(_)}) == Set(Goal())

  // predicates describing properties of Terrains

  def isPassable(coords: (Int, Int)) =
    terrain.get(coords) match {
      case None    => false
      case Some(n) => n.isPassable && !hasCrate(coords)
    }

  def hasCrate(coords : (Int, Int)) = cratesPos contains coords

  // methods that return a new layout, representing a move

  def moveCrate(coords : (Int, Int), direction : ((Int, Int)) => (Int, Int)) : Option[Layout] = {
    if(!hasCrate(coords))
      return None

    val newCoords = direction(coords)

    if(isPassable(newCoords))
      return Some(new Layout(dudePos, cratesPos - coords + newCoords, terrain))
    else
      return None
  }

  def moveDude(direction : ((Int, Int)) => (Int, Int)) : Option[Layout] = {

    val newCoords = direction(dudePos)

    if(isPassable(newCoords))
      Some(new Layout(newCoords, cratesPos, terrain))
    else
      moveCrate(newCoords, direction) match {
        case Some(layout) => layout.moveDude(direction)
        case _ => None
      }
  }
}

object Layout {
  def up(coords: (Int, Int))     = (coords._1 - 1, coords._2)
  def down(coords : (Int, Int))  = (coords._1 + 1, coords._2)
  def left(coords : (Int, Int))  = (coords._1, coords._2 - 1)
  def right(coords : (Int, Int)) = (coords._1, coords._2 + 1)
}
