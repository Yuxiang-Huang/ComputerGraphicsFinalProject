import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class ImageReader{
    public static void main(String[] args) throws IOException{
        int steps = 200;

        File file = new File("earth.jpg");
        BufferedImage image = ImageIO.read(file);

        int[][] rgb = new int[steps][steps];
        // int[][] rgb = new int[image.getHeight()][image.getWidth()/2];

        for (int i = 0; i < steps; i ++) {
            for (int j = 0; j < steps; j ++) {
                rgb[j][i] = image.getRGB(i * image.getWidth() / steps, j * image.getHeight() / steps);
            }
        }

        System.out.println(rgb.length);
        System.out.println(rgb[0].length);
        
        // for (int i = 0; i < rgb.length; i ++) {
        //     for (int j = 0; j < rgb[i].length; j ++) {
        //         rgb[i][j] = image.getRGB(j/2, i);
        //     }
        // }

        // for (int[] arr : rgb) {
        //     System.out.println(Arrays.toString(arr)); 
        // }

        Screen s = new Screen();

        BufferedImage firstImage = s.getimg();
    ImageOutputStream output =
      new FileImageOutputStream(new File("SolarSystemResize.gif"));
    GifSequenceWriter writer =
      new GifSequenceWriter(output, firstImage.getType(), 100, false);

          //set up the world at the center
          Matrix transform = new Matrix();
          transform.ident();
          Stack<Matrix> csystems = new Stack<Matrix>();
          Matrix tmp = new Matrix(Matrix.TRANSLATE, 250, 250, 250);
          tmp.mult(transform);
          csystems.push(tmp.copy());

        for (int i = 0; i < 25; i++) {
            s.clearScreen();

            System.out.println(i);

            tmp = new Matrix(Matrix.ROTATE, i * 2 * Math.PI / 25, 'Y');
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());

            tmp = new Matrix(Matrix.ROTATE, Math.PI / 2, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());

            PolygonMatrix poly = new PolygonMatrix();
            
            poly.addSphere(0, 0, 0, 100, steps);
            poly.mult(csystems.peek());
            poly.drawPolygons(s, rgb, steps);
            writer.writeToSequence(s.getimg());

            csystems.pop();
        }

        //display gif
        URL url = ImageReader.class.getResource("SolarSystemResize.gif");
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