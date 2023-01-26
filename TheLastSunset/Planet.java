import java.awt.*;
import java.util.*;

public class Planet{
  static GfxVector view = new GfxVector(0, 0, 1);

  String name;
  double size;
  double dist;
  double revTime;
  double theta;
  double x;
  double y;
  int[][] rgb;
  ArrayList<Color> planet2D;

  double selfRotate = 0;
  double selfRotateTime;

  double displaySize;

  ArrayList<Double> venusSnowflakes = new ArrayList<>();

  public Planet(String name, double size, double dist, double revTime, double selfRotateTime, int[][] rgb, double theta,
  ArrayList<Color> planet2D){
    this.name = name;
    this.size = size;
    this.dist = dist;
    this.revTime = revTime;
    this.selfRotateTime = selfRotateTime;
    this.theta = theta;
    x = Math.cos(theta) * dist;
    y = Math.sin(theta) * dist;
    this.rgb = rgb;
    this.planet2D = planet2D;

    displaySize = size;

    //random snowflakes for Venus
    if (name.equals("Venus")){
      double phi = 0;

      while (phi < Math.PI * 2 - Math.PI / 10){
        venusSnowflakes.add(phi);
        phi += Math.random() * Math.PI / 10 + Math.PI / 10;
      }
    }
  }

  public void update(int limit){
    if (limit > (x + 250 + size)){
      displaySize -= 1.5; 
    } else{
      theta += 2 * Math.PI / revTime;
      selfRotate += 2 * Math.PI / selfRotateTime;
      x = Math.cos(theta) * dist;
      y = Math.sin(theta) * dist;
    }
  }

  public void display(Screen s, Stack<Matrix> csystems, int steps, int limit){
     csystems.push(csystems.peek().copy());

     //translate
     Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, 0);
     tmp.mult(csystems.peek());
     csystems.pop();
     csystems.push(tmp.copy());

     //self rotate
     tmp = new Matrix(Matrix.ROTATE, selfRotate, 'Z');
     tmp.mult(csystems.peek());
     csystems.pop();
     csystems.push(tmp.copy());

     //fix
     tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
     tmp.mult(csystems.peek());
     csystems.pop();
     csystems.push(tmp.copy());

     //draw
     int diminish = Math.max(0, (int) ((limit - (x + Screen.XRES/2 - size)) / 3 / size * steps));

     PolygonMatrix polys = new PolygonMatrix();
     polys.addSphere(0, 0, 0, displaySize, steps, 
     Math.min(steps, diminish));
     polys.mult(csystems.peek());

     polys.drawPolygons(s, view, rgb, Math.min(steps, steps - diminish));
     csystems.pop();
  }

  public void display2D(Screen s, Stack<Matrix> csystems, int limit){
    Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, 0);
    tmp.mult(csystems.peek());
    csystems.push(tmp.copy());

    for (int i = planet2D.size() - 1; i >= 0; i --){
      if (i + 1 < limit - (x + Screen.XRES/2)){ //circular limit
        EdgeMatrix edges = new EdgeMatrix();
        edges.addFilledCircle(0, 0, 0, i + 1);
        edges.mult(csystems.peek());
        edges.drawEdges(s, planet2D.get(i));
      }
    }

    if (name.equals("Venus")){
      drawSnowFlakesVenus(s, csystems, 10, limit);
    }

    csystems.pop();
  }

  public void drawSnowFlakesVenus(Screen s, Stack<Matrix> csystems, int length, int limit){        
    //translate to the center of the planet
    for (double phi : venusSnowflakes){
        EdgeMatrix edges = new EdgeMatrix();
        //translate to the center of this snowflake
        double currLen = size * LastSunset.scale2D + length;
        Matrix tmp = new Matrix(Matrix.TRANSLATE, currLen * Math.cos(phi), currLen * Math.sin(phi), 0);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());
        //rotate
        tmp = new Matrix(Matrix.ROTATE, phi, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //draw 6 edges
        for (int j = 0; j < 6; j ++){
            double alpha = Math.PI * 2 * j / 6;
            edges.addEdge(0, 0, 0, length * Math.cos(alpha), length * Math.sin(alpha), 0);
        }

        //check matrix exclude the rotate by x
        Matrix checkMatrix = new Matrix(Matrix.TRANSLATE, x, y, 0);
        tmp = new Matrix(Matrix.TRANSLATE, currLen * Math.cos(phi), currLen * Math.sin(phi), 0);
        tmp.mult(checkMatrix);
        checkMatrix = tmp.copy();
        //rotate
        tmp = new Matrix(Matrix.ROTATE, phi, 'Z');
        tmp.mult(checkMatrix);
        checkMatrix = tmp.copy();

        edges.drawEdgesCircularLimit(s, new Color (255, 255, 0), x, y, 
        limit - (x + Screen.XRES/2), csystems.peek(), checkMatrix);
        
        csystems.pop();
      }
  }
}