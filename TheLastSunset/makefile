run: LastSunset.class
	java LastSunset

LastSunset.class: LastSunset.java Screen.class Matrix.class EdgeMatrix.class PolygonMatrix.class GfxVector.class Planet.class Venus.class Earth.class Moon.class
	javac LastSunset.java

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

Venus.class: Venus.java
	javac Venus.java
	
Earth.class: Earth.java
	javac Earth.java

Moon.class: Moon.java
	javac Moon.java

clean:
	rm *.class