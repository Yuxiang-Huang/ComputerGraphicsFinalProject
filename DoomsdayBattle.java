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
        new GifSequenceWriter(output, firstImage.getType(), 50, false);

        WaterDrop sfp = new WaterDrop();

        //entrance
        int introFrame = 0;
        for (int i = 0; i < introFrame; i ++){
            System.out.println(i);
            s.clearScreen();
            sfp.y -= (Screen.YRES / 2) / introFrame;
            sfp.display(s);
            writer.writeToSequence(s.getimg());

            if (i == 20){
                sfp.acc = true;
            }
        }

        sfp.rotateSpeed *= 2;

        introFrame = 0;
        for (int i = 0; i < introFrame; i ++){
            System.out.println(i);
            s.clearScreen();
            sfp.y -= (Screen.YRES / 2) / introFrame;
            sfp.display(s);
            writer.writeToSequence(s.getimg());
        }

        //start battle
        sfp.intro = false;
        sfp.x = Screen.XRES; 
        sfp.y = Screen.YRES;

        ArrayList<SpaceShip> ships = new ArrayList<>();
        ArrayList<SpaceShip> explode = new ArrayList<>();
        for (int i = 0; i < 4; i ++){
            for (int j = 0; j < 5; j ++){
                ships.add(new SpaceShip(i, j));
            }
        }

        int battleframe = 60;
        for (int i = 0; i < battleframe; i ++){
            s.clearScreen();
            System.out.println(i);
            sfp.update(ships, explode);
            sfp.display(s);
            for (SpaceShip ship : ships){
                ship.display(s);
            }
            for (int j = explode.size() - 1; j >= 0 ; j--){
                SpaceShip ship = explode.get(j);
                if (ship.expand){
                    ship.display(s);
                }
                ship.explode(s, explode);
            }
            writer.writeToSequence(s.getimg());
        }

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

