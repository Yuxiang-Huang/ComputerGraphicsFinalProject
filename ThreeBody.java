import java.util.*;
import java.io.*;
import java.awt.*;

import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;

import java.net.URL;

public class ThreeBody{
  public static void main(String[] args) throws Exception {
    Screen s = new Screen();

    EdgeMatrix edges0 = new EdgeMatrix();
    EdgeMatrix edges1 = new EdgeMatrix();
    EdgeMatrix edges2 = new EdgeMatrix();

    int total = 30;

    Body b0 = new Body(5, 0);
    Body b1 = new Body(10, 1);
    Body b2 = new Body(20, 2);
    //
    // bodyInfo(b0, 0);
    // bodyInfo(b1, 1);
    // bodyInfo(b2, 2);

    b0.x = 236.2173599678471;
    b0.y = 460.8096972475058;
    b0.z = 217.92555614015996;
    b0.dx = 0.6037109603635997;
    b0.dy = -2.8126471896414196;
    b0.dz = -3.0;

    b1.x = 84.11901463829508;
    b1.y = 433.46372244853836;
    b1.z = 148.4697114931152;
    b1.dx = 4.8783093348376525;
    b1.dy = 4.137241669213653;
    b1.dz = 2.0;

    b2.x = 192.67978650721966;
    b2.y = 243.0797436644906;
    b2.z = 169.3261327077595;
    b2.dx = -4.340593638961874;
    b2.dy = -2.8598430774659955;
    b2.dz = 1.0;

    BufferedImage firstImage = s.getimg();

    ImageOutputStream output =
      new FileImageOutputStream(new File("ThreeBodySystem.gif"));

    GifSequenceWriter writer =
      new GifSequenceWriter(output, firstImage.getType(), 40, false);

    for (int i = 0; i < total; i ++){
        System.out.println(i);
        //change velocity
        b0.attract(b1);
        b0.attract(b2);
        b1.attract(b2);

        //apply
        helper(edges0, b0);
        helper(edges1, b1);
        helper(edges2, b2);

        //comment this out to see the process
        apply(edges0, edges1, edges2, b0, b1, b2, s);

        // write out the first image to our sequence...
        writer.writeToSequence(s.getimg());
    }

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

  public static void helper(EdgeMatrix edges, Body b0){
    edges.addEdge(b0.x, b0.y, 0, b0.x + b0.dx, b0.y + b0.dy, 0);
    b0.x += b0.dx;
    b0.y += b0.dy;
    b0.z += b0.dz;
  }

  public static void apply(EdgeMatrix edges0, EdgeMatrix edges1, EdgeMatrix edges2,
  Body b0, Body b1, Body b2, Screen s){
    PolygonMatrix c0 = new PolygonMatrix();
    PolygonMatrix c1 = new PolygonMatrix();
    PolygonMatrix c2 = new PolygonMatrix();

    c0.addSphere(b0.x, b0.y, b0.z, Math.sqrt(b0.m) * b0.z / 25, 20);
    c1.addSphere(b1.x, b1.y, b1.z, Math.sqrt(b1.m) * b1.z / 25, 20);
    c2.addSphere(b2.x, b2.y, b2.z, Math.sqrt(b2.m) * b2.z / 25, 20);

    s.clearScreen();
    Color c = Color.BLUE;
    //edges0.drawEdges(s, c);
    c0.drawPolygons(s);
    c = Color.RED;
    //edges1.drawEdges(s, c);
    c1.drawPolygons(s);
    c = Color.GREEN;
    //edges2.drawEdges(s, c);
    c2.drawPolygons(s);
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
