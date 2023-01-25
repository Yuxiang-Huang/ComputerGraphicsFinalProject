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
    int total = 75;

    boolean flag = true;
    while (flag){
        Body b0 = new Body(mass, 0);
        Body b1 = new Body(mass, 1);
        Body b2 = new Body(mass, 2);
        Body planet = new Body(10E7, 3);

        bodyInfo(b0, 0);
        bodyInfo(b1, 1);
        bodyInfo(b2, 2);
        //bodyInfo(planet);

        for (int i = 0; i < total; i ++){
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

            if (outOfBound(b0) || outOfBound(b1) || outOfBound(b2)){
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
    if (b.type == 1){
        bodyInfo(b, 1);
    }
    if (b.type == 1){
        System.out.println(curr);
    }
    curr.mult(new Matrix(Matrix.ROTATE, Math.PI/6, 'X'));
    if (b.type == 1){
        System.out.println(curr);
    }
    if (curr.m.get(0)[0] < 0 || curr.m.get(0)[0] > Screen.XRES || curr.m.get(0)[1] < 0 || curr.m.get(0)[1] > Screen.YRES)
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

