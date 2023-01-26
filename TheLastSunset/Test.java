import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class Test {
    static GfxVector view = new GfxVector(0, 0, 1);
    public static void main(String[] args) throws Exception{
        Screen s = new Screen();

        //start gif
        BufferedImage firstImage = s.getimg();
        ImageOutputStream output =
        new FileImageOutputStream(new File("LastSunSet.gif"));
        GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 100, false);

        //variables
        int steps = 20;
        int dist = 300;
        int radius = 25;
        double year = 60;
        int day = 30;

        int[][] background = createRGBMap("background.jpg", Screen.XRES);
        ArrayList<Planet> planets = new ArrayList<Planet>();
        Planet Sun = new Planet(radius * 1.75, 0, 100, 50, new Color (255, 255, 0), createRGBMap("sun.jpg", steps), 0);
        planets.add(Sun);

        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, Screen.XRES/2, Screen.YRES/2, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        for (int i = 200; i < 300; i ++){
            System.out.println(i);

            s.clearScreen();;

            drawBackground(s, background, i);

            //update
            for (int j = 0; j < planets.size(); j ++){
                Planet p = planets.get(j);
                p.update();
            }

            //rotate
            tmp = new Matrix(Matrix.ROTATE, -Math.PI/5, 'X');
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());

            for (int j = 0; j < planets.size(); j ++){
                Planet p = planets.get(j);

                //draw the orbit in the rotated world
                EdgeMatrix edges = new EdgeMatrix();
                edges.addCircle(0, 0, 0, p.dist, 0.01);
                edges.mult(csystems.peek());
                edges.drawEdges(s, p.c);

                csystems.push(csystems.peek().copy());

                //translate
                tmp = new Matrix(Matrix.TRANSLATE, p.x, p.y, 0);
                tmp.mult(csystems.peek());
                csystems.pop();
                csystems.push(tmp.copy());

                //self rotate
                tmp = new Matrix(Matrix.ROTATE, p.selfRotate, 'Z');
                tmp.mult(csystems.peek());
                csystems.pop();
                csystems.push(tmp.copy());

                // //rotate
                // tmp = new Matrix(Matrix.ROTATE, Math.PI/4, 'X');
                // tmp.mult(csystems.peek());
                // csystems.pop();
                // csystems.push(tmp.copy());

                //fix
                tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
                tmp.mult(csystems.peek());
                csystems.pop();
                csystems.push(tmp.copy());

                //draw
                PolygonMatrix polys = new PolygonMatrix();
                polys.addSphere(0, 0, 0, p.size, steps);
                polys.mult(csystems.peek());

                polys.drawPolygons(s, view, p.rgb, steps);
                csystems.pop();
            }
            writer.writeToSequence(s.getimg());
            csystems.pop();
        }

        //display animation
        URL url = Test.class.getResource("LastSunSet.gif");
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

    public static void drawBackground(Screen s, int[][] background, int limit){
        for (int i = limit; i < Screen.XRES; i ++){
            for (int j = 0; j < Screen.YRES; j ++){
                s.plot(new Color (background[j][i]), i, j, -1.0/0);    
            }
        }
    }
}
