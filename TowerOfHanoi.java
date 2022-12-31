import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class TowerOfHanoi {
    static int level = 1;
    static ArrayList<Integer> first;
    static ArrayList<Integer> second;
    static ArrayList<Integer> third;

    static int poleRadius = 20;
    static int ringRadius = 20;
    static int floorHeight = 200;
    public static void main(String[] args) throws IOException {
        first = new ArrayList<Integer>();
        for (int i = level - 1; i >= 0; i--) {
            first.add(i + 1);
        }

        second = new ArrayList<Integer>();
        third = new ArrayList<Integer>();

        Screen s = new Screen();
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        csystems.push(transform.copy());

        //start gif
        BufferedImage firstImage = s.getimg();
        ImageOutputStream output =
        new FileImageOutputStream(new File("TowerOfHanoi.gif"));
        GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 500, false);

        draw(s, csystems, writer);
        solve(s, csystems, first, third, second, level, writer);

        //display animation
        URL url = SolarSystem.class.getResource("TowerOfHanoi.gif");
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

    public static void solve(Screen s, Stack<Matrix> csystems, ArrayList<Integer> from, ArrayList<Integer> to, 
    ArrayList<Integer> tmp, int level, GifSequenceWriter writer)throws IOException{
        if (level == 1){
            to.add(from.remove(from.size() - 1));
            draw(s, csystems, writer);
        } else{
            solve(s, csystems, from, tmp, to, level - 1, writer);
            to.add(from.remove(from.size() - 1));
            draw(s, csystems, writer);
            solve(s, csystems, tmp, to, from, level - 1, writer);
        }
    }

    public static void draw (Screen s, Stack<Matrix> csystems, GifSequenceWriter writer) throws IOException{
        s.clearScreen();

        PolygonMatrix polys = new PolygonMatrix();
        //floor
        polys.addBox(0, floorHeight, 0, Screen.XRES, 20, 0);

        //poles
        polys.addCylinder(Screen.XRES / 6, floorHeight, Screen.YRES - floorHeight, 0, poleRadius - 5, 20);
        polys.addCylinder(Screen.XRES / 2, floorHeight, Screen.YRES - floorHeight, 0, poleRadius - 5, 20);
        polys.addCylinder(Screen.XRES * 5 / 6, floorHeight, Screen.YRES - floorHeight, 0, poleRadius - 5, 20);

        //rings
        for (int i = 0; i < first.size(); i ++){
            polys.addTorus(Screen.XRES / 6, (ringRadius * 2) * i + floorHeight + ringRadius, 
            0, ringRadius, poleRadius * first.get(i), 20);
        }

        for (int i = 0; i < second.size(); i ++){
            polys.addTorus(Screen.XRES / 2, (ringRadius * 2) * i + floorHeight + ringRadius, 
            0, ringRadius, poleRadius * second.get(i), 20);
        }

        for (int i = 0; i < third.size(); i ++){
            polys.addTorus(Screen.XRES * 5 / 6, (ringRadius * 2) * i + floorHeight + ringRadius, 
            0, ringRadius, poleRadius * third.get(i), 20);
        }

        // //rotate
        // Matrix tmp = new Matrix(Matrix.ROTATE, Math.PI/10, 'X');
        // tmp.mult(csystems.peek());
        // csystems.push(tmp.copy());

        //draw
        polys.mult(csystems.peek());
        polys.drawPolygons(s);

        //csystems.pop();

        writer.writeToSequence(s.getimg());
    }
}
