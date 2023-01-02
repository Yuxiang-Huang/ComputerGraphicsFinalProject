import java.util.*;
import java.awt.*;

public class EdgeMatrix extends Matrix {
  //make sure steps is divisible by 4
  public void addStar(double x0, double y0, double z0, double z1,
  double r, int steps){
    Matrix points = generateStar(x0, y0, z0, z1, r, steps);

    for (double[] arr : points.m){
      addEdge(arr[0], arr[1], arr[2], arr[0]+1, arr[1], arr[2]);
    }

    System.out.println(points.m.size());
  }

  private Matrix generateStar(double x, double y, double z0, double z1,
  double r0, int steps ) {
    Matrix points = new Matrix();
    double z, r;

    z = Math.min(z0, z1);
    double dz = Math.abs(z1 - z0) / steps;

    r = 0;
    double dr = r0 / steps;

    //for y value of points on line
    for (double h = 0; h < steps; h ++) {
      //draw a circle
      EdgeMatrix edges = new EdgeMatrix();
      edges.addMyCurve(r, 0, 0, r, -r, 0, 0, r, steps/4, EdgeMatrix.HERMITE);
      edges.addMyCurve(-r, 0, 0, r, r, 0, 0, r, steps/4, EdgeMatrix.HERMITE);
      edges.addMyCurve(r, 0, 0, -r, -r, 0, 0, -r, steps/4, EdgeMatrix.HERMITE);
      edges.addMyCurve(-r, 0, 0, -r, r, 0, 0, -r, steps/4, EdgeMatrix.HERMITE);
      edges.mult(new Matrix(Matrix.TRANSLATE, x, y, z));
      for (int i = 0; i < edges.m.size(); i += 2){
        double[] arr = edges.m.get(i);
        points.addColumn(arr[0], arr[1], arr[2]);
      }
      z += dz;
      if (h < steps/2){
        r += dr;
      } else{
        r -= dr;
      }
    }
    return points;
  }

  public void addCircle(double cx, double cy, double cz,
                        double r, double step) {
    double x0, y0, x1, y1, t;

    x0 = r + cx;
    y0 = cy;
    for (t=step; t <= 1.00001; t+= step) {

      x1 = r * Math.cos(2 * Math.PI * t) + cx;
      y1 = r * Math.sin(2 * Math.PI * t) + cy;

      addEdge(x0, y0, cz, x1, y1, cz);
      x0 = x1;
      y0 = y1;
    }
  }//addCircle

  public void addFilledCircle(double cx, double cy, double cz,
  double r) {
    for(int y = (int) (cy - r); y <= (int) (cy + r); y++){
      for(int x = (int) (cx - r); x <=(int) (cx + r); x++){
        if((x - cx)*(x - cx)+(y - cy)*(y - cy) <= r*r){
          addEdge(x, y, cz, x, y, cz);
        }
      }
    }
  }

  public void addCurve( double x0, double y0,
                         double x1, double y1,
                         double x2, double y2,
                         double x3, double y3,
                         double step, int curveType ) {

    double t, x, y;
    Matrix xcoefs = new Matrix(curveType, x0, x1, x2, x3);
    Matrix ycoefs = new Matrix(curveType, y0, y1, y2, y3);

    double[] xm = xcoefs.get(0);
    double[] ym = ycoefs.get(0);

    for (t=step; t <= 1.000001; t+= step) {

      x = xm[0]*t*t*t + xm[1]*t*t+ xm[2]*t + xm[3];
      y = ym[0]*t*t*t + ym[1]*t*t+ ym[2]*t + ym[3];
      addEdge(x0, y0, 0, x, y, 0);
      x0 = x;
      y0 = y;
    }
  }//addCurve

  public void addMyCurve( double x0, double y0,
                         double x1, double y1,
                         double x2, double y2,
                         double x3, double y3,
                         double step, int curveType ) {

    double t, x, y;
    Matrix xcoefs = new Matrix(curveType, x0, x1, x2, x3);
    Matrix ycoefs = new Matrix(curveType, y0, y1, y2, y3);

    double[] xm = xcoefs.get(0);
    double[] ym = ycoefs.get(0);

    
    for (int i = 1; i <= step; i ++){
      t = 1.0 / step * i;
      x = xm[0]*t*t*t + xm[1]*t*t+ xm[2]*t + xm[3];
      y = ym[0]*t*t*t + ym[1]*t*t+ ym[2]*t + ym[3];
      addEdge(x0, y0, 0, x, y, 0);
      x0 = x;
      y0 = y;
    }
  }//addCurve

  public void addEdge(double x0, double y0, double z0,
                      double x1, double y1, double z1) {
    double[] col0 = {x0, y0, z0, 1};
    double[] col1 = {x1, y1, z1, 1};
    m.add(col0);
    m.add(col1);
  }//addColumn

  public void drawEdges(Screen s, Color c) {
    if ( m.size() < 2) {
      System.out.println("Need at least 2 edges to draw a line");
      return;
    }//not enough points

    for(int point=0; point<m.size()-1; point+=2) {
      double[] p0 = m.get(point);
      double[] p1 = m.get(point+1);
      s.drawLine((int)p0[0], (int)p0[1], (int)p1[0], (int)p1[1], p0[2], c);
    }//draw lines
  }//drawEdges

  public void drawEdges(Screen s, Color c, int xLowerLimit, int xUpperLimit) {
    if ( m.size() < 2) {
      System.out.println("Need at least 2 edges to draw a line");
      return;
    }//not enough points

    for(int point=0; point<m.size()-1; point+=2) {
      double[] p0 = m.get(point);
      double[] p1 = m.get(point+1);
      s.drawLine((int)p0[0], (int)p0[1], (int)p1[0], (int)p1[1], p0[2], c, xLowerLimit, xUpperLimit);
    }//draw lines
  }//drawEdges with limit
}//class EdgeMatrix
