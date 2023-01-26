import java.awt.*;
import java.util.*;

public class Venus extends Planet{

  ArrayList<Double> venusSnowflakes = new ArrayList<>();
  ArrayList<Double> venusRandomAngle = new ArrayList<>();

    public Venus(String name, double size, double dist, double revTime, double selfRotateTime, int[][] rgb, double theta,
  ArrayList<Color> planet2D){
    super(name, size, dist, revTime, selfRotateTime, rgb, theta, planet2D);
    //random snowflakes for Venus
    if (name.equals("Venus")){
      double phi = 0;

      while (phi < Math.PI * 2 - Math.PI / 10){
        venusSnowflakes.add(phi);
        phi += Math.random() * Math.PI / 10 + Math.PI / 10;

        venusRandomAngle.add(Math.random() * 2 * Math.PI);
      }
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
    
    drawSnowFlakesVenus(s, csystems, 10, limit);

    csystems.pop();
  }

  public void drawSnowFlakesVenus(Screen s, Stack<Matrix> csystems, int length, int limit){        
    //translate to the center of the planet
    for (int i = 0; i < venusSnowflakes.size(); i ++){
      double phi = venusSnowflakes.get(i);
      EdgeMatrix edges = new EdgeMatrix();
      //translate to the center of this snowflake
      double currLen = size * LastSunset.scale2D + length;
      Matrix tmp = new Matrix(Matrix.TRANSLATE, currLen * Math.cos(phi), currLen * Math.sin(phi), 0);
      tmp.mult(csystems.peek());
      csystems.push(tmp.copy());
      //rotate
      tmp = new Matrix(Matrix.ROTATE, venusRandomAngle.get(i), 'Z');
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
