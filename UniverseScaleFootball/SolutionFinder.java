import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class SolutionFinder{
  static double t = 0.001;
  static double mass = 100 * 10E11 * 1;
  static int steps = 20;
  static double zfactor = 250;
  public static void main(String[] args) throws Exception {
    //setup
    int total = 100;

    boolean flag = true;
    while (flag){
        Body b0 = new Body(mass, 0);
        Body b1 = new Body(mass, 1);
        Body b2 = new Body(mass, 2);
        Body planet = new Body(10E7, 3);
    b0.x = 225.1431734039289;
    b0.y = 299.9988886652912;
    b0.z = -47.36201078719667;
    b0.dx = -5.640876201752784;
    b0.dy = -6.80533427844432E-5;
    b0.dz = 2.663066152775723;
    
    b1.x = 193.19437295028638;
    b1.y = 299.9990102068979;
    b1.z = 180.86703651903773;
    b1.dx = 1.3301875840013504;
    b1.dy = -1.6025757633717695E-5;
    b1.dz = -2.050833320470318;
    
    b2.x = 302.4201074362905;
    b2.y = 300.0001634661233;
    b2.z = -13.216748105878244;
    b2.dx = 4.317940046984553;
    b2.dy = 8.856920723551432E-5;
    b2.dz = -0.613921440948297;
        // bodyInfo(b0, 0);
        // bodyInfo(b1, 1);
        // bodyInfo(b2, 2);
        bodyInfo(planet);

        for (int i = 0; i < total; i ++){

            for (int j = 0; j < 1 / t; j ++){
                 //change velocity
                b0.attract(b1, t);
                b0.attract(b2, t);
                b0.attract(planet, t);
        
                b1.attract(b0, t);
                b1.attract(b2, t);
                b1.attract(planet, t);
        
                b2.attract(b0, t);
                b2.attract(b1, t);
                b2.attract(planet, t);
        
                planet.attract(b0, t);
                planet.attract(b1, t);
                planet.attract(b2, t);
            }

            if (outOfBound(planet)){
                System.out.println(i);
                break;
            }

            if (i == total - 1){
                flag = false;
            }
        } 
    }
  }

  public static boolean outOfBound(Body b){
    Matrix curr = new Matrix();
    curr.addColumn(b.x, b.y, b.z);
    //System.out.println(curr);
    curr.mult(new Matrix(Matrix.ROTATE, Math.PI/6, 'X'));
    //System.out.println(curr);
    if (curr.m.get(0)[0] < 0 || curr.m.get(0)[0] > Screen.XRES || curr.m.get(0)[1] < 0 || curr.m.get(0)[1] > Screen.YRES
    || curr.m.get(0)[0] < -250 || curr.m.get(0)[0] > 500)
        return true;
    return false;
  }

  public static void bodyInfo(Body b, int i){
    System.out.println("b" + i + ".x = " + b.x + ";");
    System.out.println("b" + i + ".y = " + b.y + ";");
    System.out.println("b" + i + ".z = " + b.z + ";");
    System.out.println("b" + i + ".dx = " + b.dx + ";");
    System.out.println("b" + i + ".dy = " + b.dy + ";");
    System.out.println("b" + i + ".dz = " + b.dz + ";");
    System.out.println();
  }

  public static void bodyInfo(Body b){
    System.out.println("planet" + ".x = " + b.x + ";");
    System.out.println("planet" + ".y = " + b.y + ";");
    System.out.println("planet" + ".z = " + b.z + ";");
    System.out.println("planet" + ".dx = " + b.dx + ";");
    System.out.println("planet" + ".dy = " + b.dy + ";");
    System.out.println("planet" + ".dz = " + b.dz + ";");
    System.out.println();
  }
}

