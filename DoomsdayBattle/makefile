run: DoomsdayBattle.class
	java DoomsdayBattle

DoomsdayBattle.class: DoomsdayBattle.java Screen.class Matrix.class EdgeMatrix.class PolygonMatrix.class GfxVector.class SpaceShip.class
	javac DoomsdayBattle.java

Matrix.class: Matrix.java
	javac Matrix.java

PolygonMatrix.class: PolygonMatrix.java Polygon.class Matrix.java Screen.class GfxVector.class
	javac PolygonMatrix.java

Polygon.class: Polygon.java GfxVector.class
	javac Polygon.java

GfxVector.class: GfxVector.java
	javac GfxVector.java

EdgeMatrix.class: EdgeMatrix.java Matrix.java Screen.class
	javac EdgeMatrix.java

Screen.class: Screen.java
	javac Screen.java

SpaceShip.class: SpaceShip.java
	javac SpaceShip.java

clean:
	rm *.class