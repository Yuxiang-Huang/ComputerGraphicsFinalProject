import java.awt.*;
import java.util.*;

public class Earth extends Planet{
    Moon moon;
    double sunlight;

    public Earth(String name, double size, double dist, double revTime, double selfRotateTime, int[][] rgb, double theta,
  ArrayList<Color> planet2D, Moon moon){
        super(name, size, dist, revTime, selfRotateTime, rgb, theta, planet2D);
        this.moon = moon;
    }

    @Override
    public void update(int limit){
        if (limit > (x + Screen.XRES / 2 - size)){ //release moon
            if (moon.orbit){
                moon.orbit = false;
                //calculate tangential velocity
                double speed = moon.dist * 2 * Math.PI / revTime * 4;
                moon.dx = speed * Math.sin(moon.theta);
                moon.dy = -speed * Math.cos(moon.theta);
            }
        }

        if (limit > (x + Screen.XRES / 2 + size)){
          displaySize -= 1.5; 
        } else{
          theta -= 2 * Math.PI / revTime;
          selfRotate -= 2 * Math.PI / selfRotateTime;
          x = Math.cos(theta) * dist;
          y = Math.sin(theta) * dist;
        }
      }

    @Override
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
        drawSnowFlakesEarth(s, csystems, 5, limit);

        csystems.pop();

        //for moon
        csystems.push(csystems.peek().copy());

        //translate
        tmp = new Matrix(Matrix.TRANSLATE, x, y, 0);
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        moon.update(limit, x);
        if (moon.displaySize > 0){
            moon.display(s, csystems, LastSunset.steps, limit, x);
        }
        moon.display2D(s, csystems, limit, x);

        csystems.pop();
    }

    public void drawSnowFlakesEarth(Screen s, Stack<Matrix> csystems, int length, int limit){        
        int times = 25;
        for (int i = 0; i < times; i ++){
            EdgeMatrix edges = new EdgeMatrix();
            double phi = Math.PI * 2 * i / times;
            //translate to the center of this snowflake
            double currLen = length + size * LastSunset.scale2D;
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

            edges.drawEdgesCircularLimit(s, new Color (255, 255, 255), x, y, 
            Math.min(limit - (x + Screen.XRES/2), currLen + length) - sunlight, csystems.peek(), checkMatrix);
            
            csystems.pop();
        }
    }
}
