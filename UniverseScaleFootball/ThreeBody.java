import java.util.*;
import java.io.*;
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
  public static void main(String[] args) throws Exception {
    //lighting
    GfxVector view = new GfxVector(0, 0, 1);
		Color amb = new Color(50, 50, 50);
    ArrayList<GfxVector> lightPos = new ArrayList<>();
		Color lightColor = new Color(255, 255, 255);
        
    double[] ambient = new double[]{0.1, 0.1, 0.1};
    double[] diffuse = new double[]{0.5, 0.5, 0.5};
    double[] specular = new double[]{0.5, 0.5, 0.5};

    //setup
    int total = 325; //325
    Screen s = new Screen();

    int[][] sunRGB = createRGBMap("sun.jpg", steps);
    int[][] planetRGB = createRGBMap("planet.jpg", steps);
    int[][] frozen = createRGBMap("frozen.png", steps);
    int[][] fire = createRGBMap("fire.jpg", steps);

    Body b0 = new Body(mass, 0, ambient, diffuse, specular, sunRGB);
    Body b1 = new Body(mass, 1, ambient, diffuse, specular, sunRGB);
    Body b2 = new Body(mass, 2, ambient, diffuse, specular, sunRGB);
    Body planet = new Body(10E7, 3, ambient, diffuse, specular, planetRGB);

    ArrayList<Body> bodies = new ArrayList<>(); 
    bodies.add(b0);
    bodies.add(b1);
    bodies.add(b2);
    bodies.add(planet);

    // b2.dx = - b0.dx - b1.dx;
    // b2.dy = - b0.dy - b1.dy;
    // b2.dz = - b0.dz - b1.dz;

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

    planet.x = 383.27541696942706;
    planet.y = 160.26314953065503;
    planet.z = 106.42699031402424;
    planet.dx = -0.955315200154736;
    planet.dy = -0.11435082462058332;
    planet.dz = -1.2884815576633857;

    // bodyInfo(b0, 0);
    // bodyInfo(b1, 1);
    // bodyInfo(b2, 2);
    // bodyInfo(planet);

    BufferedImage firstImage = s.getimg();

    ImageOutputStream output =
      new FileImageOutputStream(new File("ThreeBodySystem.gif"));
    GifSequenceWriter writer =
      new GifSequenceWriter(output, firstImage.getType(), 40, false);

    for (int i = 0; i < total; i ++){
        System.out.println(i);
    
        s.clearScreen();

        if (i == 150){
          bodyInfo(b0, 0);
    bodyInfo(b1, 1);
    bodyInfo(b2, 2);
    bodyInfo(planet);
        }

        // if (i == 190){
        //   planet.texture = frozen;
        // }
        // else if (i == 270){
        //   planet.texture = fire;
        // }

        lightPos = new ArrayList<>();
        lightPos.add(new GfxVector(250, 250, 1000));
		    lightPos.add(new GfxVector(b0.x, b0.y, b0.z));
        lightPos.add(new GfxVector(b1.x, b1.y, b1.z));
        lightPos.add(new GfxVector(b2.x, b2.y, b2.z));

        for (Body b : bodies){
          b.display(s, view, amb, lightPos, lightColor, steps);
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
