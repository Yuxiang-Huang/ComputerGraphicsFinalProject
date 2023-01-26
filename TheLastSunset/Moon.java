import java.awt.*;
import java.util.*;

public class Moon extends Planet{
    boolean orbit = true;

    double dx;
    double dy;

    public Moon(String name, double size, double dist, double revTime, double selfRotateTime, int[][] rgb, double theta,
  ArrayList<Color> planet2D){
        super(name, size, dist, revTime, selfRotateTime, rgb, theta, planet2D);
    }

    public void display(Screen s, Stack<Matrix> csystems, int steps, int limit, double EarthX){
        //new world
        csystems.push(csystems.peek());

        //translate to moon
        Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, 0);
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //self rotate
        tmp = new Matrix(Matrix.ROTATE, selfRotate, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //fix y
        tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //draw
        int diminish = Math.max(0, (int) ((limit - (x + Screen.XRES/2 - size + EarthX)) / 3 / size * steps));

        PolygonMatrix polys = new PolygonMatrix();
        polys.addSphere(0, 0, 0, displaySize, steps, 
        Math.min(steps, diminish));
        polys.mult(csystems.peek());

        polys.drawPolygons(s, view, rgb, Math.min(steps, steps - diminish));
        csystems.pop();
    }

    public void display2D(Screen s, Stack<Matrix> csystems, int limit, double EarthX){
        Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, 0);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());
    
        for (int i = planet2D.size() - 1; i >= 0; i --){
          if (i + 1 < limit - (x + Screen.XRES/2 + EarthX)){ //circular limit
            EdgeMatrix edges = new EdgeMatrix();
            edges.addFilledCircle(0, 0, 0, i + 1);
            edges.mult(csystems.peek());
            edges.drawEdges(s, planet2D.get(i));
          }
        }
        csystems.pop();
      }

    public void update(int limit, double EarthX){
        if (limit > (x + Screen.XRES / 2 + size + EarthX)){
            displaySize -= 1.5; 
        } else{
            theta -= 2 * Math.PI / revTime;
            selfRotate -= 2 * Math.PI / selfRotateTime;
            if (orbit){
                x = Math.cos(theta) * dist;
                y = Math.sin(theta) * dist;
            } else{                
                x += dx;
                y += dy;
            }
        }
    }
}
