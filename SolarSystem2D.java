import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.text.AttributeSet.ColorAttribute;

public class SolarSystem2D {
    public static void main(String[] args) {
        Screen s = new Screen();

        //variables
        int dist = 300;
        int radius = 25;

        //theta
        double mercuryTheta = -Math.PI / 2;
        double earthTheta = Math.PI / 4;
        double marsTheta = Math.PI * 5 / 4;
        double moonTheta = Math.PI * 3 / 4;

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

        double r, g, b;

        //Venus
        double VenusR = radius * 0.949 * 2;
        double VenusD = dist * 0.72;
        double VenusTheta = Math.PI * 3 / 4;
        ArrayList<Color> VenusColor = new ArrayList<>();
        for (int i = 0; i < VenusR; i ++){
            if (i < VenusR * 0.5){
                double now = i;
                r = 225 + (237 - 225) * now / (VenusR * 0.5);
                g = 195 + (99 - 195) * now / (VenusR * 0.5);
                b = 13 + (27 - 13) * now / (VenusR * 0.5);
                VenusColor.add(new Color ((int)r, (int)g, (int)b));
            } else if (i < VenusR * 0.75){
                double now = i - VenusR * 0.5;
                r = 226 + (179 - 226) * now / (VenusR * (0.75 - 0.5));
                g = 90 + (112 - 93) * now / (VenusR * (0.75 - 0.5));;
                b = 36 - (70 - 36) * now / (VenusR * (0.75 - 0.5));;
                VenusColor.add(new Color ((int)r, (int)g, (int)b));
            } else{
                double now = i - VenusR * 0.75;
                r = 158 + (142 - 158) * now / (VenusR * (1 - 0.75));
                g = 122 + (129 - 122) * now / (VenusR * (1 - 0.75));
                b = 88 + (103 - 88) * now / (VenusR * (1 - 0.75));
                VenusColor.add(new Color ((int)r, (int)g, (int)b));
            }
        }

        draw2DPlanet(s, VenusD, VenusTheta, VenusColor, csystems);

        //Sun
        double SunR = radius * 2 * 2;
        double SunD = 0;
        double SunTheta = 0;
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

        s.display();
    }

    public static void draw2DPlanet(Screen s, double distance, double theta, ArrayList<Color> colors, Stack<Matrix> csystems){
        for (int i = colors.size() - 1; i >= 0; i --){
            s.addFilledCircle(Math.cos(theta) * distance + Screen.XRES / 2, Math.sin(theta) * distance + Screen.YRES / 2, 0,
            i, colors.get(i));
        }
    }
}
