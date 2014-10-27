sokoban
=======

Scala demo - sokoban

A short demo to demonstrate functional and imperative programming in Scala.

To play, install scala and:

1. Compile all classes:

scalac GameFrame.scala LayoutRenderer.scala
Level.scala Main.scala LayoutReader.scala Layout.scala Levels.scala
Terrain.scala -d <target-dir>

2. Run Main with map collection as the argument

scala Main maps\path-to-map-collection.slc

Levels can be obtained from: http://www.sourcecode.se/sokoban/levels. Only XML versions are supported.

Keys are hardcoded:

cursor keys - movement
X - undo last MEANINGFUL move (i.e. undo last push)
R - reset level
SPACE - next level in the collection
BKSPACE - previous level

Fun features:

- pathfinding to trim unreachable edges of levels off the displayed map 
- undo only remembers meaningful moves
- AWT abuse for illusion of tile volume without graphical assets.

Have fun!
