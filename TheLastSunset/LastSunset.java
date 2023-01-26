import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class LastSunset {
    static int radius = 25;;
    public static void main(String[] args) throws Exception{
        Screen s = new Screen();

        //start gif
        BufferedImage firstImage = s.getimg();
        ImageOutputStream output =
        new FileImageOutputStream(new File("LastSunset.gif"));
        GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 100, false);

        //variables
        int steps = 100;
        int dist = 200;
        double year = 6000;
        int day = 30;

        double scale2D = 1.5;

        //theta
        double marsTheta = Math.PI * 5 / 4;

        int[][] background = createRGBMap("background.jpg", Screen.XRES);
        ArrayList<Planet> planets = new ArrayList<Planet>();
        Planet Sun = new Planet(radius * 1.75, 0, 100, 50, new Color (255, 255, 0), 
        createRGBMap("sun.jpg", steps), 0, sun2D(radius * 1.75 * scale2D));
        planets.add(Sun);

        Planet Mars = new Planet(radius * 0.532, dist * 1.52, year * 2, day * 1.03, new Color (255, 0, 0), 
        createRGBMap("mars.jpg", steps), marsTheta, mars2D(radius * 0.532 * scale2D));
        planets.add(Mars);

        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, Screen.XRES/2, Screen.YRES/2, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        for (int i = 0; i < 100; i += 1){
            System.out.println(i);

            s.clearScreen();;

            drawBackground(s, background, i);

            //rotate
            tmp = new Matrix(Matrix.ROTATE, -Math.PI/5, 'X');
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());

            //update and display
            for (int j = 0; j < planets.size(); j ++){
                Planet p = planets.get(j);
                p.update(i);
                if (p.displaySize > 0){
                    p.display(s, csystems, steps, i);
                }
                p.display2D(s, csystems, i);
            }

            writer.writeToSequence(s.getimg());
            csystems.pop();
        }

        //display animation
        URL url = LastSunset.class.getResource("LastSunset.gif");
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

    public static ArrayList<Color> sun2D (double PlanetRadius){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * 0.45){
                double factor = i / (PlanetRadius * 0.45);
                r = 255;
                g = 245 + (228 - 245) * factor;
                b = 200 + (100 - 200) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * 0.77){
                double factor = (i - PlanetRadius * 0.45) / (PlanetRadius * (0.77 - 0.45));;
                r = 255;
                g = 200 + (100 - 299) * factor;
                b = 53;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * 0.77) / (PlanetRadius * (1 - 0.77));
                r = 254 + (232 - 254) * factor;
                g = 215 + (67 - 215) * factor;
                b = 52 + (17 - 52) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }
        return PlanetColor;
    }

    public static ArrayList<Color> mars2D(double PlanetRadius){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        double bound1 = 0.45;
        double bound2 = 0.8;
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * bound1){
                double factor = i / (PlanetRadius * bound1);
                r = 231 + (227 - 231) * factor;
                g = 175 + (169 - 175) * factor;
                b = 13 + (31 - 13) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * bound2){
                double factor = (i - PlanetRadius * bound1) / (PlanetRadius * (bound2 - bound1));;
                r = 213 + (129 - 213) * factor;
                g = 170 + (37 - 170) * factor;
                b = 93 + (4 - 93) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * bound2) / (PlanetRadius * (1 - bound2));
                r = 98 + (128 - 98) * factor;
                g = 37 + (27 - 37) * factor;
                b = 4;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }

        return PlanetColor;
    }
}
