trait Terrain {def isPassable = true}
case class Wall()  extends Terrain {override def isPassable = false}
case class Floor() extends Terrain
case class Goal()  extends Terrain 
