import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class SolarSystem{
  public static void main(String[] args) throws Exception {
        Screen s = new Screen();

        //variables
        int steps = 200;
        int dist = 300;
        int radius = 25;
        double year = 60;
        int day = 30;

        //theta
        double sunTheta = 0;
        double mercuryTheta = -Math.PI / 4;
        double venusTheta = Math.PI * 2 / 3;
        double earthTheta = -Math.PI / 3;
        double marsTheta = Math.PI / 3;
        double moonTheta = Math.PI * 3 / 4;

        int[][] background = createRGBMap("background.jpg", Screen.XRES);

        //planets
        ArrayList<Planet> planets = new ArrayList<Planet>();
        //size, dist, revTime, selfRotateTime
        Planet Sun = new Planet(radius * 2, 0, 100, 50, new Color (255, 255, 0), createRGBMap("sun.jpg", steps), sunTheta);
        planets.add(Sun);

        Planet Mercury = new Planet(radius * 0.383, dist * 0.39, year * 0.25, day * 3, new Color (0, 0, 255), createRGBMap("mercury.jpg", steps), mercuryTheta);
        planets.add(Mercury);

        Planet Venus = new Planet(radius * 0.949, dist * 0.72, year * 0.6, day * 2, new Color (150, 75, 0), createRGBMap("venus.jpg", steps), venusTheta);
        planets.add(Venus);

        Planet Earth = new Planet(radius, dist, year, day, new Color (0, 255, 0), createRGBMap("earth.jpg", steps), earthTheta);
        planets.add(Earth);

        Planet Mars = new Planet(radius * 0.532, dist * 1.52, year * 2, day * 1.03, new Color (255, 0, 0), createRGBMap("mars.jpg", steps), marsTheta);
        planets.add(Mars);

        Planet Moon = new Planet(radius * 0.25, Earth.size + 15, year / 3, year / 3, new Color (192, 192, 192), createRGBMap("moon.jpg", steps), moonTheta);

        // //start gif
        // BufferedImage firstImage = s.getimg();
        // ImageOutputStream output =
        // new FileImageOutputStream(new File("SolarSystemResize.gif"));
        // GifSequenceWriter writer =
        // new GifSequenceWriter(output, firstImage.getType(), 100, false);

        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, 400, 400, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        //for (double i = 0; i < year * 6; i ++){
            //clear
            s.clearScreen();

            drawBackground(s, background);

            //System.out.println(csystems.peek());

            //update
            for (int j = 0; j < planets.size(); j ++){
                Planet p = planets.get(j);
                p.update();
            }

            //rotate
            tmp = new Matrix(Matrix.ROTATE, -Math.PI/4, 'X');
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());

            //csystems.push(csystems.peek().copy());

            for (int j = 0; j < planets.size(); j ++){
                Planet p = planets.get(j);

                //draw the orbit in the rotated world
                EdgeMatrix edges = new EdgeMatrix();
                edges.addCircle(0, 0, 0, p.radius, 0.01);
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

                polys.drawPolygons(s, p.rgb, steps);

                //Earth Moon
                if (j == 3){
                    Moon.update();

                    //reverse the y change
                    tmp = new Matrix(Matrix.ROTATE, -Math.PI/2, 'Y');
                    tmp.mult(csystems.peek());
                    csystems.pop();
                    csystems.push(tmp.copy());

                    //orbit
                    edges = new EdgeMatrix();
                    edges.addCircle(0, 0, 0, Moon.radius, 0.01);
                    edges.mult(csystems.peek());
                    edges.drawEdges(s, Moon.c);

                    //new world
                    csystems.push(csystems.peek());

                    //translate to moon
                    tmp = new Matrix(Matrix.TRANSLATE, Moon.x, Moon.y, 0);
                    tmp.mult(csystems.peek());
                    csystems.pop();
                    csystems.push(tmp.copy());

                    //self rotate
                    tmp = new Matrix(Matrix.ROTATE, Moon.selfRotate, 'Z');
                    tmp.mult(csystems.peek());
                    csystems.pop();
                    csystems.push(tmp.copy());

                    //fix y
                    tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
                    tmp.mult(csystems.peek());
                    csystems.pop();
                    csystems.push(tmp.copy());

                    //draw
                    polys = new PolygonMatrix();
                    polys.addSphere(0, 0, 0, Moon.size * csystems.peek().get(3)[2] / 250, steps);
                    polys.mult(csystems.peek());

                    polys.drawPolygons(s, Moon.rgb, steps);

                    csystems.pop();
                }
                csystems.pop();
            }   
            //finish this frame
            //writer.writeToSequence(s.getimg());
            csystems.pop();
        //}

        s.display();

        // //display animation
        // URL url = SolarSystem.class.getResource("SolarSystemResize.gif");
        // Icon icon = new ImageIcon(url);
        // JLabel label = new JLabel(icon);
        // JFrame f = new JFrame("Animation");
        // f.getContentPane().add(label);
        // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.pack();
        // f.setLocationRelativeTo(null);
        // f.setVisible(true);
        // writer.close();
        // output.close();
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

    public static void drawBackground(Screen s, int[][] background){
        for (int i = 0; i < Screen.XRES; i ++){
            for (int j = 0; j < Screen.XRES; j ++){
                s.plot(new Color (background[j][i]), i, j, -1.0/0);
            }
        }
    }
}