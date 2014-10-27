object LayoutReader {

   val Terrainset = Map(
    '#' -> Wall(),
    ' ' -> Floor(),
    '$' -> Floor(),
    '@' -> Floor(),
    '.' -> Goal(),
    '+' -> Goal(),
    '*' -> Goal())

    val nullLayout = new Layout(null, Set[(Int, Int)](), Map[(Int, Int), Terrain]())

    def isDude(c : Char) = List('@', '+') contains c
    def isCrate(c : Char) = List('$', '*') contains c

    def expandLayout(elem : ((Int, Int), Char), plan : Layout) : Layout = {

    val coords = elem._1
    val Terrain   = elem._2

    val newPlan   = plan.terrain + (coords -> Terrainset(Terrain))
    val newDude   = if(isDude(Terrain))  coords else plan.dudePos
    val newCrates = if(isCrate(Terrain)) plan.cratesPos + coords else plan.cratesPos

    new Layout(newDude, newCrates, newPlan)

  }

   def constructLayout(blueprint : Seq[String]) = {
    val layout = (for(row <- 0 until blueprint.length;
                      col <- 0 until blueprint(row).length)
                        yield((row, col) -> blueprint(row)(col))).foldRight(nullLayout)(expandLayout)
    cull(layout)
  }

  def cull(layout : Layout) = {
     val accessible = visit(layout.terrain, List[(Int, Int)](), List(layout.dudePos) )
     val newTerrain = (layout.terrain) filter {
       (tile) =>
         !(tile._2.isPassable) ||
         accessible.contains(tile._1)
     }
     new Layout(layout.dudePos, layout.cratesPos, newTerrain)
  }   
     
  def visit(terrain : Map[(Int, Int),Terrain], visited : List[(Int, Int)], schedule : List[(Int, Int)]) : List[(Int, Int)] = {
    if(schedule.isEmpty)
      return visited

    val current = schedule.head
    val directions = List(Layout.up _, Layout.down _, Layout.left _, Layout.right _)
    val newCoords = (directions map {_(current)}) filter {(coords) => terrain(coords).isPassable && !(visited contains coords)}
    visit(terrain, current :: visited, newCoords ::: schedule.tail)
  }

}
