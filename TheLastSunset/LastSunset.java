import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI.ScrollListener;
import javax.imageio.stream.*;
import java.net.URL;

public class LastSunset {
    static int radius = 25;;
    static double scale2D = 1.5;
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

        //theta
        double marsTheta = Math.PI * 5 / 4;
        double mercuryTheta = -Math.PI * 2 / 5;
        double venusTheta = Math.PI * 3 / 4;
        double earthTheta = Math.PI / 4;
        double moonTheta = Math.PI;

        int[][] background = createRGBMap("background.jpg", Screen.XRES);
        ArrayList<Planet> planets = new ArrayList<Planet>();
        Planet Sun = new Planet("Sun", radius * 1.6, 0, 100, 50, 
        createRGBMap("sun.jpg", steps), 0, sun2D(radius * 1.6 * scale2D));
        planets.add(Sun);

        Planet Mars = new Planet("Mars", radius * 0.532, dist * 1.52, year * 2, day * 1.03,
        createRGBMap("mars.jpg", steps), marsTheta, mars2D(radius * 0.532 * scale2D));
        planets.add(Mars);

        Planet Mercury = new Planet("Mercuary", radius * 0.383, dist * 0.39, year * 0.25, day * 3, 
        createRGBMap("mercury.jpg", steps), mercuryTheta, mercury2D(radius * 0.383 * scale2D));
        planets.add(Mercury);

        Venus Venus = new Venus("Venus", radius * 0.949, dist * 0.72, year * 0.6, day * 2,
        createRGBMap("venus.jpg", steps), venusTheta, venus2D(radius * 0.949 * scale2D));
        planets.add(Venus);

        Moon Moon = new Moon("Moon", radius * 0.25, radius + 15, year / 3, year / 3,
        createRGBMap("moon.jpg", steps), moonTheta, moon2D(radius * 0.25 * scale2D));

        Earth Earth = new Earth("Earth", radius, dist, year, day, 
        createRGBMap("earth.jpg", steps), earthTheta, Earth2D(radius * scale2D), Moon);
        planets.add(Earth);

        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, Screen.XRES/2, Screen.YRES/2, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        for (int i = 375; i < 450; i ++){
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

    public static ArrayList<Color> moon2D(double PlanetRadius){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        double bound1 = 0.2;
        double bound2 = 0.55;
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * bound1){
                double factor = i / (PlanetRadius * bound1);
                r = 254 + (253 - 254) * factor;
                g = 253 + (243 - 253) * factor;
                b = 217 + (93 - 217) * factor;
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

    public static ArrayList<Color> Earth2D(double PlanetRadius){
        //2D
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        double bound1 = 0.26;
        double bound2 = 0.49;
        double bound3 = 0.8;
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * bound1){
                double factor = i / (PlanetRadius * bound1);
                r = 252 + (243 - 252) * factor;
                g = 236 + (216 - 236) * factor;
                b = 50 + (39 - 50) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * bound2){
                double factor = (i - PlanetRadius * bound1) / (PlanetRadius * (bound2 - bound1));;
                r = 243 + (226 - 243) * factor;
                g = 231 + (161 - 231) * factor;
                b = 137 + (47 - 137) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * bound3){
                double factor = (i - PlanetRadius * bound2) / (PlanetRadius * (bound3 - bound2));;
                r = 213 + (208 - 213) * factor;
                g = 75 + (65 - 75) * factor;
                b = 35 + (24 - 35) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * bound2) / (PlanetRadius * (1 - bound2));
                r = 204 + (153 - 204) * factor;
                g = 137 + (100 - 137) * factor;
                b = 46 + (36 - 46) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }
        return PlanetColor;
    }

    public static ArrayList<Color> venus2D(double PlanetRadius){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        double bound1 = 0.55;
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * bound1){
                double factor = i / (PlanetRadius * bound1);
                r = 255 + (242 - 255) * factor;
                g = 128 + (89 - 128) * factor;
                b = 43 + (57 - 43) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }  else{
                double factor = (i - PlanetRadius * bound1) / (PlanetRadius * (1 - bound1));
                r = 200 + (130 - 200) * factor;
                g = 90 + (120 - 90) * factor;
                b = 70 + (100 - 70) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }
        return PlanetColor;
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

    public static ArrayList<Color> mercury2D(double PlanetRadius){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        double bound1 = 0.37;
        double bound2 = 0.78;
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * bound1){
                double factor = i / (PlanetRadius * bound1);
                r = 225 + (237 - 225) * factor;
                g = 195 + (99 - 195) * factor;
                b = 13 + (27 - 13) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * bound2){
                double factor = (i - PlanetRadius * bound1) / (PlanetRadius * (bound2 - bound1));;
                r = 226 + (179 - 226) * factor;
                g = 90 + (112 - 93) * factor;
                b = 36 - (70 - 36) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * bound2) / (PlanetRadius * (1 - bound2));
                r = 158 + (142 - 158) * factor;
                g = 122 + (129 - 122) * factor;
                b = 88 + (103 - 88) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }
        return PlanetColor;
    }
}
