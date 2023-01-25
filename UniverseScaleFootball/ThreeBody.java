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
  static double t = 0.001;
  static double mass = 100 * 10E11 * 1;
  static int steps = 20;
  static double zfactor = 250;
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
    int total = 75;
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

    b0.x = 396.077482971523;
    b0.y = 300.0;
    b0.z = -30.316126092990714;
    b0.dx = -2.4825196807583527;
    b0.dy = 0.0;
    b0.dz = -0.825248291299385;
    
    b1.x = 95.78495447776288;
    b1.y = 300.0;
    b1.z = -25.52735233494371;
    b1.dx = -0.7392044413325729;
    b1.dy = 0.0;
    b1.dz = -1.1923925190880127;
    
    b2.x = 194.68092622073345;
    b2.y = 300.0;
    b2.z = 42.10864717419048;
    b2.dx = 3.221724122090926;
    b2.dy = -0.0;
    b2.dz = 2.0176408103873977;
    
    planet.x = 380.35916612863673;
    planet.y = 123.75694971719822;
    planet.z = -28.661104344340814;
    planet.dx = -1.2560327884448443;
    planet.dy = -0.8729857244354906;
    planet.dz = -1.836860183658632;

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
