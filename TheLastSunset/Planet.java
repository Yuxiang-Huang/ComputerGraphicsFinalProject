import java.awt.*;
import java.util.*;

public class Planet{
  static GfxVector view = new GfxVector(0, 0, 1);

  double size;
  double dist;
  double revTime;
  double theta;
  double x;
  double y;
  Color c;
  int[][] rgb;
  ArrayList<Color> planet2D;

  double selfRotate = 0;
  double selfRotateTime;

  double displaySize;

  public Planet(double size, double dist, double revTime, double selfRotateTime, Color c, int[][] rgb, double theta,
  ArrayList<Color> planet2D){
    this.size = size;
    this.dist = dist;
    this.revTime = revTime;
    this.selfRotateTime = selfRotateTime;
    this.c = c;
    this.theta = theta;
    x = Math.cos(theta) * dist;
    y = Math.sin(theta) * dist;
    this.rgb = rgb;
    this.planet2D = planet2D;

    displaySize = size;
  }

  public void update(int limit){
    if (limit > (x + 250 + size)){
      displaySize -= 1.5 * 10; 
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

    csystems.pop();
  }
}