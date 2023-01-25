run: ThreeBody.class
	java ThreeBody

ThreeBody.class: ThreeBody.java Screen.class Matrix.class EdgeMatrix.class PolygonMatrix.class GfxVector.class Body.class
	javac ThreeBody.java

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

Body.class: Body.java
	javac Body.java

clean:
	rm *.class