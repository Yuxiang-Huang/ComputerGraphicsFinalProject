run: SolarSystem2D.class
	java SolarSystem2D

SolarSystem2D.class: SolarSystem2D.java Screen.class Matrix.class EdgeMatrix.class PolygonMatrix.class GfxVector.class Planet.class
	javac SolarSystem2D.java

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

Planet.class: Planet.java
	javac Planet.java

clean:
	rm *.class