import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.image.*;

import java.awt.*;

import javax.imageio.*;
import javax.swing.*;
import javax.imageio.stream.*;

import java.net.URL;

public class DoomsdayBattle {
    static int steps = 100;
    public static void main(String[] args) throws IOException{
        //lighting
        GfxVector view = new GfxVector(0, 0, 1);
		Color amb = new Color(255, 255, 255);
        ArrayList<GfxVector> lightPos = new ArrayList<>();
		lightPos.add(new GfxVector(250, 400, 150));
		Color lightColor = new Color(255, 255, 255);
        
        double[] ambient = new double[]{0.23125, 0.23125, 0.23125};
        double[] diffuse = new double[]{0.2775, 0.2775, 0.2775};
        double[] specular = new double[]{0.773911, 0.773911, 0.773911};

        int[][] explosion = createTexture("explosion.jpg", steps);

        //csystem
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

        WaterDrop sfp = new WaterDrop(ambient, diffuse, specular);

        //entrance
        int introFrame = 50; //50
        for (int i = 0; i < introFrame; i ++){
            System.out.println(i);
            s.clearScreen();
            sfp.y -= ((Screen.YRES + 150) / 2) / introFrame; //150 is the size of sfp
            sfp.display(s, view, amb, lightPos, lightColor);
            writer.writeToSequence(s.getimg());
        }

        sfp.acc = true;
        sfp.rotateSpeed *= 2;

        introFrame = 7;
        for (int i = 0; i < introFrame; i ++){
            System.out.println(i);
            s.clearScreen();
            sfp.y -= ((Screen.YRES + 150) / 2) / introFrame;
            sfp.display(s, view, amb, lightPos, lightColor);
            writer.writeToSequence(s.getimg());
        }

        //start battle
        sfp.intro = false;
        sfp.x = Screen.XRES; 
        sfp.y = Screen.YRES;

        //set up
        ArrayList<SpaceShip> ships = new ArrayList<>();
        ArrayList<SpaceShip> explode = new ArrayList<>();
        for (int i = 0; i < 4; i ++){
            for (int j = 0; j < 5; j ++){
                ships.add(new SpaceShip(i, j));
            }
        }

        lightPos = new ArrayList<>();
        lightPos.add(new GfxVector(250, 250, 1000));

        //animation battle
        // int battleframe = 60;
        // for (int i = 0; i < battleframe; i ++){
        int i = -1;
        while (ships.size() != 0){
            i ++;
            s.clearScreen();
            System.out.println(i);
            sfp.update(ships, explode);
            sfp.display(s, view, amb, lightPos, lightColor);

            //remove too close ships and update ships
            ArrayList<SpaceShip> tmp = new ArrayList<>();
            for (int j = ships.size() - 1; j >= 0 ; j--){
                SpaceShip ship = ships.get(j);
                //check closeness
                if (tooClose(ship, ships)){
                    tmp.add(ship);
                } else{
                    ship.display(s, view, amb, lightPos, lightColor);
                }
            }
            for (SpaceShip ship : tmp){
                ships.remove(ship);
                explode.add(ship);
            }

            //explosion
            for (int j = explode.size() - 1; j >= 0 ; j--){
                SpaceShip ship = explode.get(j);
                if (ship.expand){
                    ship.display(s, view, amb, lightPos, lightColor);
                }
                ship.explode(s, explode, view, amb, lightPos, lightColor, explosion);
            }
            writer.writeToSequence(s.getimg());
        }

        // while (explode.size() != 0){
        //     System.out.println(explode.size());

        //     s.clearScreen();
        //     //explosion
        //     for (int j = explode.size() - 1; j >= 0 ; j--){
        //         SpaceShip ship = explode.get(j);
        //         if (ship.expand){
        //             ship.display(s, view, amb, lightPos, lightColor);
        //         }
        //         ship.explode(s, explode, view, amb, lightPos, lightColor, explosion);
        //     }
        //     writer.writeToSequence(s.getimg());
        // }

        //move towards viewer
        while (sfp.x != Screen.XRES/2 || sfp.y != Screen.YRES/2){
            s.clearScreen();
            sfp.end();
            sfp.display(s, view, amb, lightPos, lightColor);

            for (int j = explode.size() - 1; j >= 0 ; j--){
                SpaceShip ship = explode.get(j);
                if (ship.expand){
                    ship.display(s, view, amb, lightPos, lightColor);
                }
                ship.explode(s, explode, view, amb, lightPos, lightColor, explosion);
            }

            writer.writeToSequence(s.getimg());
        }

        sfp.end = true;

        while (sfp.size < 150){
            s.clearScreen();
            sfp.size += 2;
            System.out.println(sfp.size);
            sfp.endDisplay(s, view, amb, lightPos, lightColor);
            writer.writeToSequence(s.getimg());
        }

        //display animation
        URL url = DoomsdayBattle.class.getResource("DoomsdayBattle.gif");
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

    public static boolean tooClose(SpaceShip ship, ArrayList<SpaceShip> ships){
        for (SpaceShip other : ships){
            if (!ship.equals(other)){
                if (dist(ship, other) < 30){
                    return true;
                }
            }
        }
        return false;
    }

    public static double dist(SpaceShip ship, SpaceShip other){
        return Math.sqrt((other.x - ship.x) * (other.x - ship.x) + (other.y - ship.y) * (other.y - ship.y)); 
    }

    public static int[][] createTexture(String name, int steps) throws IOException{
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

