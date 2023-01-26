import java.util.*;
import java.io.*;
import java.awt.*;

public class Polygon {

  public double DEFAULT_R_AMBIENT = 0.2;
  public double DEFAULT_R_SPECULAR = 0.5;
  public double DEFAULT_R_DIFFUSE = 0.5;
  public double SPECULAR_EXP = 4;

  private double[] p0;
  private double[] p1;
  private double[] p2;

  private GfxVector normal;
  private HashMap<String, GfxVector> vertexNormals;

  private double[] rAmbient;
  private double[] rDiffuse;
  private double[] rSpecular;

  private GfxVector view;
  private Color amb;
  private ArrayList<GfxVector> lightPos;
  private Color lightColor;

  private Color c;

  public Polygon(double[] pt0, double[] pt1, double[] pt2, Color co) {
    p0 = Arrays.copyOf(pt0, 3);
    p1 = Arrays.copyOf(pt1, 3);
    p2 = Arrays.copyOf(pt2, 3);
    calculateNormal();
    c = co;

    rAmbient = new double[]{DEFAULT_R_AMBIENT, DEFAULT_R_AMBIENT, DEFAULT_R_AMBIENT};
    rDiffuse = new double[]{DEFAULT_R_DIFFUSE, DEFAULT_R_DIFFUSE, DEFAULT_R_DIFFUSE};
    rSpecular = new double[]{DEFAULT_R_SPECULAR, DEFAULT_R_SPECULAR, DEFAULT_R_SPECULAR};
  }//constructor

  public Polygon(double[] pt0, double[] pt1, double[] pt2) {
    p0 = Arrays.copyOf(pt0, 3);
    p1 = Arrays.copyOf(pt1, 3);
    p2 = Arrays.copyOf(pt2, 3);
    calculateNormal();

    rAmbient = new double[]{DEFAULT_R_AMBIENT, DEFAULT_R_AMBIENT, DEFAULT_R_AMBIENT};
    rDiffuse = new double[]{DEFAULT_R_DIFFUSE, DEFAULT_R_DIFFUSE, DEFAULT_R_DIFFUSE};
    rSpecular = new double[]{DEFAULT_R_SPECULAR, DEFAULT_R_SPECULAR, DEFAULT_R_SPECULAR};
  }//constructor w/ no color

  public Polygon(double[] pt0, double[] pt1, double[] pt2, Color co, HashMap<String, GfxVector> vNs, GfxVector v, Color a, 
  ArrayList<GfxVector> lp, Color lc) {
    p0 = Arrays.copyOf(pt0, 3);
    p1 = Arrays.copyOf(pt1, 3);
    p2 = Arrays.copyOf(pt2, 3);
    calculateNormal();
    c = co;

    vertexNormals = vNs;
    //System.out.println("| INIT | " + vertexNormals);

    rAmbient = new double[]{DEFAULT_R_AMBIENT, DEFAULT_R_AMBIENT, DEFAULT_R_AMBIENT};
    rDiffuse = new double[]{DEFAULT_R_DIFFUSE, DEFAULT_R_DIFFUSE, DEFAULT_R_DIFFUSE};
    rSpecular = new double[]{DEFAULT_R_SPECULAR, DEFAULT_R_SPECULAR, DEFAULT_R_SPECULAR};

    view = v;
    amb = a;

    lightPos = lp;

    lightColor = lc;
  }//constructor w/ normals

  public GfxVector getNormal() {
    return normal;
  }//getNormal

  private void calculateNormal() {
    GfxVector A = new GfxVector(p0, p1);
    GfxVector B = new GfxVector(p0, p2);

    normal = A.crossProduct(B);
  }//calculateNormal


  public void setReflection(double[] ar, double[] dr, double[] sr) {
    rAmbient = Arrays.copyOf(ar, 3);
    rDiffuse = Arrays.copyOf(dr, 3);
    rSpecular = Arrays.copyOf(sr, 3);
  }//setReflection

  public Color calculateLighting(GfxVector pos, GfxVector normalV) {
    int[] ambient, diffuse, specular, color;

    color = new int[3];
    ambient = calculateAmbient(amb);
    for (int i = 0; i < lightPos.size(); i ++){
      GfxVector lpTemp = new GfxVector(lightPos.get(i));
      lpTemp.subtract(pos);
      diffuse = calculateDiffuse(lpTemp, lightColor, normalV);
      specular = calculateSpecular(lpTemp, lightColor, view, normalV);
      // System.out.println(Arrays.toString(diffuse));
      // System.out.println(Arrays.toString(specular));
      // System.out.println(Arrays.toString(color));
      // System.out.println();
      for (int j = 0; j < color.length; j ++){
        color[j] += Math.max(diffuse[j], 0);
        color[j] += Math.max(specular[j], 0);
      }
    }

    for (int i = 0; i < color.length; i ++){
      color[i] += ambient[i];
      color[i] = Math.max(0, color[i]);
      color[i] = Math.min(255, color[i]);
    }

    return new Color((int)color[0], (int)color[1], (int)color[2]);
  }//calculteLighting

  private int[] calculateAmbient(Color amb) {
    int red, green, blue;

    red = (int)(rAmbient[0] * amb.getRed());
    green = (int)(rAmbient[1] * amb.getGreen());
    blue = (int)(rAmbient[2] * amb.getBlue());
    return new int[] {red, green, blue};
  }//calculateAmbient

  private int[] calculateDiffuse(GfxVector lightPos, Color lightColor, GfxVector normalV) {

    int red, green, blue;
    double dot = normalV.dotProduct(lightPos, true);
    dot = dot > 0 ? dot : 0;

    red = (int)(rDiffuse[0] * lightColor.getRed() * dot);
    green = (int)(rDiffuse[1] * lightColor.getGreen() * dot);
    blue = (int)(rDiffuse[2] * lightColor.getBlue() * dot);

    return new int[] {red, green, blue};
  }//calculateDiffuse

  private int[] calculateSpecular(GfxVector lightPos, Color lightColor, GfxVector view, GfxVector normalV) {

    int red, green, blue;
    GfxVector n = new GfxVector(normalV);
    double result;

    result = 2 * n.dotProduct(lightPos, true);

    n.scalarMultiplty(result);
    n.subtract(lightPos.getNormalized());

    result = n.dotProduct(view, true);
    result = result > 0 ? result : 0;
    result = Math.pow(result, SPECULAR_EXP);

    red = (int)(rSpecular[0] * lightColor.getRed() * result);
    green = (int)(rSpecular[1] * lightColor.getGreen() * result);
    blue = (int)(rSpecular[2] * lightColor.getBlue() * result);

    return new int[] {red, green, blue};
  }//calculateSpecular

  public void scanlineConvertOld(Screen s) {
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
  }
}//class Polygon
