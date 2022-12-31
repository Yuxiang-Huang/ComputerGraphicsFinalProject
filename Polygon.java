import java.util.*;
import java.io.*;
import java.awt.*;

public class Polygon {

  private double[] p0;
  private double[] p1;
  private double[] p2;

  private double[] normal;
  private Color c;

  public Polygon(double[] pt0, double[] pt1, double[] pt2, Color co) {
    p0 = Arrays.copyOf(pt0, 3);
    p1 = Arrays.copyOf(pt1, 3);
    p2 = Arrays.copyOf(pt2, 3);
    normal = new double[3];
    calculateNormal();
    c = co;
  }//constructor

  public double[] getNormal() {
    return normal;
  }//getNormal

  private void calculateNormal() {
    double[] A = new double[3];
    double[] B = new double[3];

    A[0] = p1[0] - p0[0];
    A[1] = p1[1] - p0[1];
    A[2] = p1[2] - p0[2];

    B[0] = p2[0] - p0[0];
    B[1] = p2[1] - p0[1];
    B[2] = p2[2] - p0[2];

    normal[0] = A[1] * B[2] - A[2] * B[1];
    normal[1] = A[2] * B[0] - A[0] * B[2];
    normal[2] = A[0] * B[1] - A[1] * B[0];
  }//calculateNormal


  /*======== void scanline_convert() ==========
  Inputs: Screen s

  Returns:

  Fills in polygon i by drawing consecutive horizontal (or vertical) lines.

  Color should be set differently for each polygon.
  ====================*/
  public void scanlineConvert(Screen s) {
    ArrayList<double[]> points = new ArrayList<double[]>();
    points.add(p0);
    points.add(p1);
    points.add(p2);
    Collections.sort(points, new Comparator<double[]>(){
      public int compare(double[] p0, double[] p1){
        if (p0[1] > p1[1]){
          return 1;
        } else{
          return -1;
        }
      }
    });

    // for (double[] p : points){
    //   System.out.println(Arrays.toString(p));
    // }
    // System.out.println();

    int y = (int) points.get(0)[1];
    double x0 = points.get(0)[0];
    double dx0 = (points.get(2)[0] - points.get(0)[0]) / ((int)points.get(2)[1] - (int)points.get(0)[1]);
    // if (points.get(2)[1] - points.get(0)[1] < 1){
    //   dx0 = points.get(2)[0] - points.get(0)[0];
    // }
    double z0 = points.get(0)[2];
    double dz0 = (points.get(2)[2] - points.get(0)[2]) / ((int)points.get(2)[1] - (int)points.get(0)[1]);

    double x1 = points.get(0)[0];
    double dx1 = (points.get(1)[0] - points.get(0)[0]) / ((int)points.get(1)[1] - (int)points.get(0)[1]);
    // if (points.get(1)[1] - points.get(0)[1] < 1){
    //   dx1 = points.get(1)[0] - points.get(0)[0];
    // }
    double z1 = points.get(0)[2];
    double dz1 = (points.get(1)[2] - points.get(0)[2]) / ((int)points.get(1)[1] - (int)points.get(0)[1]);

    // System.out.println(dx0);
    // System.out.println(dx1);

    while (y < (int) points.get(1)[1]){
      s.drawScanline((int)x0, z0, (int)x1, z1, y, c);
      x0 += dx0;
      x1 += dx1;
      z0 += dz0;
      z1 += dz1;
      y ++;
    }

    y = (int) points.get(1)[1];
    x1 = points.get(1)[0];
    dx1 = (points.get(2)[0] - points.get(1)[0]) / ((int)points.get(2)[1] - (int)points.get(1)[1]);

    z1 = points.get(1)[2];
    dz1 = (points.get(2)[2] - points.get(1)[2]) / ((int)points.get(2)[1] - (int)points.get(1)[1]);

    if (Math.abs(dx1) > Math.abs(points.get(2)[0] - points.get(1)[0])){
      dx1 = points.get(2)[0] - points.get(1)[0];
      dz1 = points.get(2)[2] - points.get(1)[2];
    }

    while (y < (int) points.get(2)[1]){
      s.drawScanline((int)x0, z0, (int)x1, z1, y, c);
      x0 += dx0;
      x1 += dx1;
      z0 += dz0;
      z1 += dz1;
      y ++;
    }
  }//scanlineConvert

  public void scanlineConvert(Screen s, int xLowerLimit, int xUpperLimit) {
    ArrayList<double[]> points = new ArrayList<double[]>();
    points.add(p0);
    points.add(p1);
    points.add(p2);
    Collections.sort(points, new Comparator<double[]>(){
      public int compare(double[] p0, double[] p1){
        if (p0[1] > p1[1]){
          return 1;
        } else{
          return -1;
        }
      }
    });

    // for (double[] p : points){
    //   System.out.println(Arrays.toString(p));
    // }
    // System.out.println();

    int y = (int) points.get(0)[1];
    double x0 = points.get(0)[0];
    double dx0 = (points.get(2)[0] - points.get(0)[0]) / ((int)points.get(2)[1] - (int)points.get(0)[1]);
    // if (points.get(2)[1] - points.get(0)[1] < 1){
    //   dx0 = points.get(2)[0] - points.get(0)[0];
    // }
    double z0 = points.get(0)[2];
    double dz0 = (points.get(2)[2] - points.get(0)[2]) / ((int)points.get(2)[1] - (int)points.get(0)[1]);

    double x1 = points.get(0)[0];
    double dx1 = (points.get(1)[0] - points.get(0)[0]) / ((int)points.get(1)[1] - (int)points.get(0)[1]);
    // if (points.get(1)[1] - points.get(0)[1] < 1){
    //   dx1 = points.get(1)[0] - points.get(0)[0];
    // }
    double z1 = points.get(0)[2];
    double dz1 = (points.get(1)[2] - points.get(0)[2]) / ((int)points.get(1)[1] - (int)points.get(0)[1]);

    // System.out.println(dx0);
    // System.out.println(dx1);

    while (y < (int) points.get(1)[1]){
      s.drawScanline((int)x0, z0, (int)x1, z1, y, c, xLowerLimit, xUpperLimit);
      x0 += dx0;
      x1 += dx1;
      z0 += dz0;
      z1 += dz1;
      y ++;
    }

    y = (int) points.get(1)[1];
    x1 = points.get(1)[0];
    dx1 = (points.get(2)[0] - points.get(1)[0]) / ((int)points.get(2)[1] - (int)points.get(1)[1]);

    z1 = points.get(1)[2];
    dz1 = (points.get(2)[2] - points.get(1)[2]) / ((int)points.get(2)[1] - (int)points.get(1)[1]);

    if (Math.abs(dx1) > Math.abs(points.get(2)[0] - points.get(1)[0])){
      dx1 = points.get(2)[0] - points.get(1)[0];
      dz1 = points.get(2)[2] - points.get(1)[2];
    }

    while (y < (int) points.get(2)[1]){
      s.drawScanline((int)x0, z0, (int)x1, z1, y, c, xLowerLimit, xUpperLimit);
      x0 += dx0;
      x1 += dx1;
      z0 += dz0;
      z1 += dz1;
      y ++;
    }
  }//scanlineConvert with limit
}//class Polygon
