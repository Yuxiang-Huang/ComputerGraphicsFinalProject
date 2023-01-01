import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;
import java.net.URL;

public class DoomsdayBattle {
    public static void main(String[] args) throws IOException{
        Screen s = new Screen();
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        csystems.push(transform.copy());

        //start gif
        BufferedImage firstImage = s.getimg();
        ImageOutputStream output =
        new FileImageOutputStream(new File("DoomsdayBattle.gif"));
        GifSequenceWriter writer =
        new GifSequenceWriter(output, firstImage.getType(), 100, false);

        WaterDrop sfp = new WaterDrop();

        int introFrame = 30;
        for (int i = 0; i < introFrame; i ++){
            System.out.println(i);
            s.clearScreen();
            sfp.y -= (Screen.YRES - 20) / introFrame;
            sfp.theta += Math.PI * 2 / introFrame;
            sfp.display(s);
            writer.writeToSequence(s.getimg());
        }

        //s.display();

        // for (int i = 0; i < 5; i ++){
        //     for (int j = 0; j < 5; j ++){
        //         SpaceShip test = new SpaceShip(i, j);
        //         test.display(s);
        //     }
        // }

        //display animation
        URL url = SolarSystem.class.getResource("DoomsdayBattle.gif");
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

