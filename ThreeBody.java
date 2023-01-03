import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class ThreeBody{
  static double t = 0.001;
  public static void main(String[] args) throws Exception {
    Screen s = new Screen();

    int total = 30;

    Body b0 = new Body(10 * 10E11 * 1, 0);
    Body b1 = new Body(11* 10E11 * 1, 1);
    Body b2 = new Body(12 * 10E11 * 1, 2);
    Body planet = new Body(10E7, 3);

    b2.dx = - b0.dx - b1.dx;
    b2.dy = - b0.dy - b1.dy;

    int factor = 300;

    b0.x = -0.6 * factor + Screen.XRES/2;
    b0.y = -0.8 * factor + Screen.YRES/2;;

    b1.x = 0.3 * factor + Screen.XRES/2;;
    b1.y = 0.6 * factor + Screen.YRES/2;;

    b2.x = -0.3 * factor + Screen.XRES/2;;
    b2.y = 0.5 * factor + Screen.YRES/2;;

    // bodyInfo(b0, 0);
    // bodyInfo(b1, 1);
    // bodyInfo(b2, 2);

    BufferedImage firstImage = s.getimg();

    ImageOutputStream output =
      new FileImageOutputStream(new File("ThreeBodySystem.gif"));
    GifSequenceWriter writer =
      new GifSequenceWriter(output, firstImage.getType(), 40, false);

    for (int i = 0; i < total; i ++){
        System.out.println(i);

        draw(b0, b1, b2, planet, s);

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

  public static void draw(Body b0, Body b1, Body b2, Body planet, Screen s){
    s.clearScreen();

    PolygonMatrix polys = new PolygonMatrix();

    polys.addSphere(b0.x, b0.y, b0.z, 10, 20); //* b0.z / 25
    polys.addSphere(b1.x, b1.y, b1.z, 13, 20); 
    polys.addSphere(b2.x, b2.y, b2.z, 15, 20);
    polys.addSphere(planet.x, planet.y, planet.z, 5, 20);

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
}
