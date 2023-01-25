import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class ThreeBody{
  static double t = 0.01;
  static double mass = 100 * 10E11 * 1;
  static int steps = 20;
  static double zfactor = 500;
  public static void main(String[] args) throws Exception {
    //lighting
    GfxVector view = new GfxVector(0, 0, 1);
		Color amb = new Color(255, 255, 255);
    ArrayList<GfxVector> lightPos = new ArrayList<>();
		lightPos.add(new GfxVector(250, 250, 1000));
		Color lightColor = new Color(255, 255, 255);
        
    double[] ambient = new double[]{0.1, 0.1, 0.1};
    double[] diffuse = new double[]{0.5, 0.5, 0.5};
    double[] specular = new double[]{0.5, 0.5, 0.5};

    //setup
    int total = 200;
    Screen s = new Screen();

    Body b0 = new Body(mass, 0);
    Body b1 = new Body(mass, 1);
    Body b2 = new Body(mass, 2);
    Body planet = new Body(10E7, 3);

    int[][] sunRGB = createRGBMap("sun.jpg", steps);
    int[][] planetRGB = createRGBMap("planet.jpg", steps);

    ArrayList<Body> bodies = new ArrayList<>(); 
    bodies.add(b0);
    bodies.add(b1);
    bodies.add(b2);
    bodies.add(planet);

    b2.dx = - b0.dx - b1.dx;
    b2.dy = - b0.dy - b1.dy;
    b2.dz = - b0.dz - b1.dz;

    b0.x = 438.6451133370989;
b0.y = 300.0;
b0.z = -84.8073573668477;
b0.dx = -1.653180856408052;
b0.dy = 0.0;
b0.dz = 0.7116483437629637;

b1.x = 66.34930289571355;
b1.y = 300.0;
b1.z = 188.46920985833225;
b1.dx = -0.9259356947571256;
b1.dy = 0.0;
b1.dz = -3.1695088762338792;

b2.x = 264.8788330212759;
b2.y = 300.0;
b2.z = 117.27487540472697;
b2.dx = 2.5791165511651775;
b2.dy = -0.0;
b2.dz = 2.4578605324709155;

    bodyInfo(b0, 0);
    bodyInfo(b1, 1);
    bodyInfo(b2, 2);
    bodyInfo(planet);

    BufferedImage firstImage = s.getimg();

    ImageOutputStream output =
      new FileImageOutputStream(new File("ThreeBodySystem.gif"));
    GifSequenceWriter writer =
      new GifSequenceWriter(output, firstImage.getType(), 40, false);

    for (int i = 0; i < total; i ++){
        System.out.println(i);
        s.clearScreen();

        for (Body b : bodies){
          double factor = 12.5;

          if (b.m == mass){
            factor = 20;
          }
      
          PolygonMatrix polys = new PolygonMatrix();
          polys.addSphere(b.x, b.y, b.z, factor * (b.z + zfactor) / zfactor, steps);
          polys.mult(new Matrix(Matrix.ROTATE, Math.PI/6, 'X'));
          if (b.type == 3){
            polys.drawPolygons(s, view, amb, lightPos, lightColor, ambient, diffuse, specular, planetRGB, steps);
          } else{
            polys.drawPolygons(s, view, amb, lightPos, lightColor, ambient, diffuse, specular, sunRGB, steps);
          }
        }

        //change velocity
        for (int j = 0; j < 1 / t; j ++){
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

        // write out the first image to our sequence...
        writer.writeToSequence(s.getimg());
    }

    //animation
    URL url = ThreeBody.class.getResource("ThreeBodySystem.gif");
    Icon icon = new ImageIcon(url);
    JLabel label = new JLabel(icon);
    JFrame f = new JFrame("Animation");
    f.getContentPane().add(label);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
    writer.close();
    output.close();
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

  public static int[][] createRGBMap(String name, int steps) throws IOException{
    File file = new File(name);
    BufferedImage image = ImageIO.read(file);

    int[][] rgb = new int[steps][steps];

    for (int i = 0; i < steps; i ++) {
        for (int j = 0; j < steps; j ++) {
            rgb[j][i] = image.getRGB(i * image.getWidth() / steps, (steps - j - 1) * image.getHeight() / steps);
        }
    }
    return rgb;
  }
}
