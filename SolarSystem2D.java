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
        int steps = 200;
        int dist = 300;
        int radius = 25;
        double year = 60;
        int day = 30;

        //theta
        double sunTheta = 0;
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

        //Venus
        int r, g, b;

        double VenusR = radius * 0.949 * 2;
        System.out.println(VenusR);
        double VenusD = dist * 0.72;
        double VenusTheta = Math.PI * 3 / 4;
        ArrayList<Color> VenusColor = new ArrayList<>();
        for (int i = 0; i < VenusR; i ++){
            if (i < VenusR * 0.5){
                r = 255;
                g = (int) (194 - i * 5);
                b = 0;
                VenusColor.add(new Color (r, g, b));
            } else if (i < VenusR * 0.75){
                VenusColor.add(new Color (220, 90, 40));
            } else{
                VenusColor.add(new Color (155,135, 105));
            }
        }

        draw2DPlanet(s, VenusD, VenusTheta, VenusColor, csystems);

        s.display();
    }

    public static void draw2DPlanet(Screen s, double distance, double theta, ArrayList<Color> colors, Stack<Matrix> csystems){
        for (int i = colors.size() - 1; i >= 0; i --){
            s.addFilledCircle(Math.cos(theta) * distance + Screen.XRES / 2, Math.sin(theta) * distance + Screen.YRES / 2, 0,
            i, colors.get(i));
        }
    }
}
