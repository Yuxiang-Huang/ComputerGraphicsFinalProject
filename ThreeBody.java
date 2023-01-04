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
  public static void main(String[] args) throws Exception {
    Screen s = new Screen();

    int total = 60;

    Body b0 = new Body(15 * 10E11 * 1, 0);
    Body b1 = new Body(17.5* 10E11 * 1, 1);
    Body b2 = new Body(20 * 10E11 * 1, 2);
    Body planet = new Body(10E7, 3);

    b2.dx = - b0.dx - b1.dx;
    b2.dy = - b0.dy - b1.dy;
    b2.dz = - b0.dz - b1.dz;

    b0.x = 567.0066478886442;
    b0.y = 505.2412746706353;
    b0.z = 150.0;
    b0.dx = -2.8573114397517454;
    b0.dy = -3.567840443430392;
    b0.dz = -3.547111752616753;
    
    b1.x = 245.57444101829284;
    b1.y = 411.3078019482365;
    b1.z = 150.0;
    b1.dx = 4.77430043796929;
    b1.dy = -2.8682049162909444;
    b1.dz = 2.41437157747324;
    
    b2.x = 398.94815576217;
    b2.y = 204.35944267761758;
    b2.z = 150.0;
    b2.dx = -1.916988998217545;
    b2.dy = 6.436045359721336;
    b2.dz = 1.132740175143513;

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

    polys.addSphere(b0.x, b0.y, b0.z, 15 * b0.z / 150, 20);
    polys.addSphere(b1.x, b1.y, b1.z, 17.5 * b1.z / 150, 20); 
    polys.addSphere(b2.x, b2.y, b2.z, 20 * b2.z / 150 , 20);
    polys.addSphere(planet.x, planet.y, planet.z, 7 * planet.z / 150, 20);

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
