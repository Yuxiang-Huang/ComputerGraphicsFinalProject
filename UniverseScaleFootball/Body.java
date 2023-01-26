import java.util.*;
import java.awt.*;

public class Body{
    public double x;
    public double y;
    public double z;
    public double dx;
    public double dy;
    public double dz;

    double m;
    double G = 6.67E-11;
    int type;
    int initialV = 3;

    //display
    public boolean useTexture;
    double theta;
    double zfactor = 500;
    int[][] texture;
    double[] ambient, diffuse, specular;

    public Body (double mass, int type, double[] ambient, double[] diffuse, double[] specular, int[][] texture, boolean useTexture){
      m = mass;
      this.type = type;
      this.texture = texture;
      this.ambient = ambient;
      this.diffuse = diffuse;
      this.specular = specular;
      this.useTexture = useTexture;
      double radius = Math.random() * 50 + 150;
      double angle = 0;
  
      if (type == 0){
        angle += 0;
        angle += Math.random() * Math.PI * 1 / 3;
      }
  
      else if (type == 1){
        angle += Math.PI * 2 / 3;
        angle += Math.random() * Math.PI * 1 / 3;
      }
  
      else if (type == 2){
        angle += Math.PI * 4 / 3;
        angle += Math.random() * Math.PI * 1 / 3;
      } 
      
      else{
        angle = Math.random() * Math.PI * 2;
      }
  
      //System.out.println(angle);
  
      x = Math.cos(angle) * radius + Screen.XRES/2;
      y = Math.sin(angle) * radius + Screen.YRES/2;
      z = Math.random() * 500 - 250;

      if (x > Screen.XRES/2){
        dx = Math.random() * initialV - 1.25 * initialV;
      } else{
        dx = Math.random() * initialV - 0.75 * initialV;
      }
      if (y > Screen.YRES/2){
        dy = Math.random() * initialV - 1.25 * initialV;
      } else{
        dy = Math.random() * initialV - 0.75 * initialV;
      }
      if (z > 0){
        dz = Math.random() * initialV - 1.25 * initialV;
      } else{
        dz = Math.random() * initialV - 0.75 * initialV;
      }

      if (type != 3){
        y = Screen.YRES / 2 + 50;
        dy = 0;
      }
    }
  
    public double dist(Body other){
        return Math.sqrt(Math.abs(x - other.x) * Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(y - other.y)
        + Math.abs(z - other.z) * Math.abs(z - other.z));
      }
  
    public void attract (Body other, double t){
        double d = dist(other);
        double a = G * other.m / d / d;

        //shell theorem
        if (d < 15){
          a = G * other.m * d / 15 / 15 / 15;
        }

        double theta = Math.atan2(other.y - y, other.x - x);
        double phi = Math.acos((other.z - z) / d);

        double ax = a * Math.cos(theta) * Math.sin(phi);
        double ay = a * Math.sin(theta) * Math.sin(phi);
        double az = a * Math.cos(phi);

        //System.out.println("x: " + x + ", y: " + y);

        x += dx * t + 0.5 * ax * t * t;
        y += dy * t + 0.5 * ay * t * t;
        z += dz * t + 0.5 * az * t * t;
    
        dx += ax * t;
        dy += ay * t;
        dz += az * t;
    }

    public void display(Screen s, GfxVector view, Color amb, ArrayList<GfxVector> lightPos, Color lightColor, int steps){
        theta += Math.PI * 2 / 100;

        double factor = 20;
        if (type == 3){
          factor = 10;
        }
  
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.ROTATE, Math.PI/6, 'X');
        tmp.mult(transform);
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.TRANSLATE, x, y, z);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, theta, 'Y');
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        if (type != 3){
          tmp = new Matrix(Matrix.ROTATE, Math.PI * 2 / 3 * type, 'Z');
          tmp.mult(csystems.peek());
          csystems.push(tmp.copy());
        }

        tmp = new Matrix(Matrix.ROTATE, Math.PI / 2, 'Z');
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        PolygonMatrix polys = new PolygonMatrix();
        polys.addSphere(0, 0, 0, factor * (z + zfactor) / zfactor, steps);
        polys.mult(csystems.peek());
        if (useTexture){
          polys.drawPolygons(s, view, texture, steps);
        } else{
          polys.drawPolygons(s, view, amb, lightPos, lightColor, ambient, diffuse, specular, texture, steps);
        }
    }
  }