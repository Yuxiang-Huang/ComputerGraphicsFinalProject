import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.text.AttributeSet.ColorAttribute;

public class SolarSystem2D {
    public static void main(String[] args) throws IOException {
        Screen s = new Screen();

        //variables
        int dist = 300;
        int radius = 25 * 2;

        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, 400, 400, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        //rotate
        tmp = new Matrix(Matrix.ROTATE, -Math.PI/5, 'X');
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        //theta
        double sunTheta = 0;
        double mercuryTheta = -Math.PI * 2 / 5;
        double venusTheta = Math.PI * 3 / 4;
        double earthTheta = Math.PI / 4;
        double marsTheta = Math.PI * 5 / 4;
        double moonTheta = Math.PI * 3 / 4;

        drawVenus(radius * 0.949, dist * 0.72, venusTheta, s, csystems);

        drawSun(radius * 1.75, 0, sunTheta, s, csystems);

        drawMercury(radius * 0.383, dist * 0.39, mercuryTheta, s, csystems);

        drawMars(radius * 0.532, dist * 1.52, marsTheta, s, csystems);
  
        s.display();
    }

    public static void draw2DPlanet(Screen s, double distance, double theta, ArrayList<Color> colors, Stack<Matrix> csystems){
        for (int i = colors.size() - 1; i >= 0; i --){
            EdgeMatrix edges = new EdgeMatrix();
            edges.addFilledCircle(Math.cos(theta) * distance, Math.sin(theta) * distance, 0, i + 1);
            edges.mult(csystems.peek());
            edges.drawEdges(s, colors.get(i));
        }
    }

    public static void drawVenus(double PlanetRadius, double PlanetDist, double PlanetTheta, Screen s, Stack<Matrix> csystems){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * 0.22){
                double factor = i / (PlanetRadius * 0.5);
                r = 225 + (237 - 225) * factor;
                g = 195 + (99 - 195) * factor;
                b = 13 + (27 - 13) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * 0.5){
                double factor = (i - PlanetRadius * 0.22) / (PlanetRadius * (0.5 - 0.22));;
                r = 226 + (179 - 226) * factor;
                g = 90 + (112 - 93) * factor;
                b = 36 - (70 - 36) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * 0.5) / (PlanetRadius * (1 - 0.5));
                r = 158 + (142 - 158) * factor;
                g = 122 + (129 - 122) * factor;
                b = 88 + (103 - 88) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }

        draw2DPlanet(s, PlanetDist, PlanetTheta, PlanetColor, csystems);
    }

    public static void drawSun(double SunR, double SunD, double SunTheta, Screen s, Stack<Matrix> csystems)throws IOException{
        //2D
        double r, g, b;
        ArrayList<Color> SunColor = new ArrayList<>();
        for (int i = 0; i < SunR; i ++){
            if (i < SunR * 0.45){
                double now = i;
                r = 255;
                g = 245 + (228 - 245) * now / (SunR * 0.45);
                b = 200 + (100 - 200) * now / (SunR * 0.45);
                SunColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < SunR * 0.77){
                double now = i - SunR * 0.45;
                r = 255;
                g = 200 + (100 - 299) * now / (SunR * (0.77 - 0.45));;
                b = 53;
                SunColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double now = i - SunR * 0.77;
                r = 254 + (232 - 254) * now / (SunR * (1 - 0.77));
                g = 215 + (67 - 215) * now / (SunR * (1 - 0.77));
                b = 52 + (17 - 52) * now / (SunR * (1 - 0.77));
                SunColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }

        draw2DPlanet(s, SunD, SunTheta, SunColor, csystems);

        //3D
        int steps = 200;

        Planet Sun = new Planet(25 * 1.75, 0, 100, 50, new Color (255, 255, 0), createRGBMap("sun.jpg", steps), 0);
        
        //fix
        Matrix tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        //draw
        PolygonMatrix polys = new PolygonMatrix();
        polys.addSphere(0, 0, 0, Sun.size, steps);
        polys.mult(csystems.peek());
        polys.drawPolygons(s, Sun.rgb, steps);

        csystems.pop();
    }

    public static void drawMercury(double PlanetRadius, double PlanetDist, double PlanetTheta, Screen s, Stack<Matrix> csystems){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * 0.42){
                double factor = i / (PlanetRadius * 0.42);
                r = 225 + (237 - 225) * factor;
                g = 195 + (99 - 195) * factor;
                b = 13 + (27 - 13) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * 0.84){
                double factor = (i - PlanetRadius * 0.42) / (PlanetRadius * (0.84 - 0.42));;
                r = 226 + (179 - 226) * factor;
                g = 90 + (112 - 93) * factor;
                b = 36 - (70 - 36) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * 0.84) / (PlanetRadius * (1 - 0.84));
                r = 158 + (142 - 158) * factor;
                g = 122 + (129 - 122) * factor;
                b = 88 + (103 - 88) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }

        draw2DPlanet(s, PlanetDist, PlanetTheta, PlanetColor, csystems);
    }

    public static void drawMars(double PlanetRadius, double PlanetDist, double PlanetTheta, Screen s, Stack<Matrix> csystems){
        double r, g, b;
        ArrayList<Color> PlanetColor = new ArrayList<>();
        for (int i = 0; i < PlanetRadius; i ++){
            if (i < PlanetRadius * 0.2){
                double factor = i / (PlanetRadius * 0.2);
                r = 225 + (237 - 225) * factor;
                g = 195 + (99 - 195) * factor;
                b = 13 + (27 - 13) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < PlanetRadius * 0.6){
                double factor = (i - PlanetRadius * 0.2) / (PlanetRadius * (0.6 - 0.2));;
                r = 226 + (179 - 226) * factor;
                g = 90 + (112 - 93) * factor;
                b = 36 - (70 - 36) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double factor = (i - PlanetRadius * 0.6) / (PlanetRadius * (1 - 0.6));
                r = 158 + (142 - 158) * factor;
                g = 122 + (129 - 122) * factor;
                b = 88 + (103 - 88) * factor;
                PlanetColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }

        draw2DPlanet(s, PlanetDist, PlanetTheta, PlanetColor, csystems);
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
