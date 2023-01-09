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
  static double mass = 30 * 10E11 * 1;
  static double default_z = 100;
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

//     b0.x = 535.0494281889503;
//     b0.y = 539.2980299751117;
//     b0.z = 156.66297208445667;
//     b0.dx = -1.6453077453770488;
//     b0.dy = -1.4128373082378358;
//     b0.dz = -1.6761233767700676;
    
//     b1.x = 208.75125979780722;
//     b1.y = 441.25842179304925;
//     b1.z = 162.31314436784535;
//     b1.dx = 0.5700816800919801;
//     b1.dy = -2.4354429612028357;
//     b1.dz = -3.232491599869356;
    
//     b2.x = 379.98574865247184;
//     b2.y = 223.01895957490063;
//     b2.z = 178.8864397548361;
//     b2.dx = 1.0752260652850687;
//     b2.dy = 3.8482802694406715;
//     b2.dz = 4.908614976639424;
    
//     planet.x = 225.78920510266408;
// planet.y = 312.00081786696296;
// planet.z = 157.3279293343815;
// planet.dx = -0.4093709017808784;
// planet.dy = -1.1913025538479292;
// planet.dz = -1.9068488528385084;

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

        System.out.println(b0.y);

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
    int factor = 7;

    if (b.m == mass){
      factor = 15;
    }

    PolygonMatrix polys = new PolygonMatrix();
    polys.addSphere(0, 0, 0, factor * b.z / default_z, 20);
    // polys.mult(new Matrix(Matrix.ROTATE, Math.PI/4, 'X'));
    polys.mult(new Matrix(Matrix.TRANSLATE, b.x, b.y, b.z));
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
