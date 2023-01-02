import java.util.*;
import java.io.*;
import java.awt.*;

import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;

import java.net.URL;

public class Orbit {
    public static void main(String[] args) throws IOException{
        Screen s = new Screen();
        
        //start gif
        BufferedImage firstImage = s.getimg();
        ImageOutputStream output =
        new FileImageOutputStream(new File("Orbit.gif"));
        GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 40, false);

        //setup
        Body center = new Body(1, 0);
        center.x = Screen.XRES/2;
        center.y = Screen.YRES/2;
        center.dx = 0;
        center.dy = 0;

        PlanetBody planet = new PlanetBody(0.01);
        planet.x = Screen.XRES/2;
        planet.y = Screen.YRES/2 + 100;
        planet.dx = 0.1;
        planet.dy = 0;

        //start 
        int total = 30;
        for (int i = 0; i < total; i ++){
            System.out.println(i);
            s.clearScreen();

            planet.attract(center);
            planet.x += planet.dx;
            planet.y += planet.dy;

            // System.out.println(planet.x);
            // System.out.println(planet.y);

            PolygonMatrix polys = new PolygonMatrix();
            polys.addSphere(center.x, center.y, 0,20, 10);
            polys.addSphere(planet.x, planet.y, 0, 10, 10);
            polys.drawPolygons(s);

            // write out the first image to our sequence...
            writer.writeToSequence(s.getimg());
        }

        URL url = ThreeBody.class.getResource("Orbit.gif");
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
}
