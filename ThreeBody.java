import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.plaf.PanelUI;
import javax.imageio.stream.*;
import java.net.URL;

public class ThreeBody{
  static double t = 0.001;
  static double mass = 100 * 10E11 * 1;
  static double zfactor = 250;
  public static void main(String[] args) throws Exception {
    Screen s = new Screen();

    int total = 30;

    Body b0 = new Body(mass, 0);
    Body b1 = new Body(mass, 1);
    Body b2 = new Body(mass, 2);
    Body planet = new Body(10E7, 3);

    b2.dx = - b0.dx - b1.dx;
    b2.dy = - b0.dy - b1.dy;
    b2.dz = - b0.dz - b1.dz;

    // b0.x = 650.9224932340734;
    // b0.y = 400.0;
    // b0.z = 21.92500589062638;
    // b0.dx = -1.649980442509496;
    // b0.dy = 0.0;
    // b0.dz = -1.900331476510374;
    
    // b1.x = 219.1638875292836;
    // b1.y = 400.0;
    // b1.z = 40.56616283958891;
    // b1.dx = -0.43048894335373933;
    // b1.dy = 0.0;
    // b1.dz = -1.4533533331695945;
    
    // b2.x = 442.20107693737987;
    // b2.y = 400.0;
    // b2.z = -34.64289784385234;
    // b2.dx = 2.0804693858632355;
    // b2.dy = -0.0;
    // b2.dz = 3.3536848096799687;
    
    // planet.x = 294.22733907724853;
    // planet.y = 285.94987853874306;
    // planet.z = 33.33674596998483;
    // planet.dx = -2.082795403749045;
    // planet.dy = 0.7184876891947818;
    // planet.dz = -1.9433570943231206;

    //int factor = 300;

    // b0.x = -0.6 * factor + Screen.XRES/2;
    // b0.y = -0.8 * factor + Screen.YRES/2;;

    // b1.x = 0.3 * factor + Screen.XRES/2;;
    // b1.y = 0.6 * factor + Screen.YRES/2;;

    // b2.x = -0.3 * factor + Screen.XRES/2;;
    // b2.y = 0.5 * factor + Screen.YRES/2;;

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

        draw(b0, s);
        draw(b1, s);
        draw(b2, s);
        draw(planet, s);

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

  public static void draw(Body b, Screen s){
    double factor = 12.5;

    if (b.m == mass){
      factor = 20;
    }

    PolygonMatrix polys = new PolygonMatrix();
    polys.addSphere(b.x, b.y, b.z, factor * (b.z + zfactor) / zfactor, 20);
    polys.mult(new Matrix(Matrix.ROTATE, Math.PI/6, 'X'));
    polys.drawPolygons(s);
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
