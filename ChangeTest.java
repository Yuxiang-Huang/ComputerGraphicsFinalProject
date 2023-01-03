import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class ChangeTest {
  public static void main(String[] args) throws Exception {
        Screen s = new Screen();

        //variables
        int steps = 20;
        int dist = 300;
        int radius = 25;
        double year = 60;
        int day = 30;

        //theta
        double earthTheta = Math.PI / 4;

        int[][] background = createRGBMap("background.jpg", Screen.XRES);

        Planet Earth = new Planet(radius, dist, year, day, new Color (0, 255, 0), createRGBMap("earth.jpg", steps), earthTheta);

        //createRGBMap("venus.jpg", steps);

        //start gif
        BufferedImage firstImage = s.getimg();
        ImageOutputStream output =
        new FileImageOutputStream(new File("SolarSystemResize.gif"));
        GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 100, false);

        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, Screen.XRES/2, Screen.YRES/2, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        for (double i = 0; i < day; i ++){
            System.out.println(i);
            
            //clear
            s.clearScreen();

            //System.out.println(csystems.peek());

            //update
            Earth.update();

            //rotate
            tmp = new Matrix(Matrix.ROTATE, -Math.PI/2, 'X');
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());

            // csystems.push(csystems.peek().copy());

            //self rotate
            tmp = new Matrix(Matrix.ROTATE, Earth.selfRotate, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());

            //fix
            tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());

            //draw
            PolygonMatrix polys = new PolygonMatrix();
            polys.addSphere(0, 0, 0, Earth.size, steps);
            polys.mult(csystems.peek());

            polys.drawPolygons(s, Earth.rgb, steps);

            //finish this frame
            writer.writeToSequence(s.getimg());
            csystems.pop();
        }

        //display animation
        URL url = SolarSystem.class.getResource("SolarSystemResize.gif");
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