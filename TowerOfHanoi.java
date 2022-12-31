import java.util.*;

public class TowerOfHanoi {
    static int level = 5;
    static ArrayList<Integer> first;
    static ArrayList<Integer> second;
    static ArrayList<Integer> third;

    static int poleRadius = 20;
    static int ringRadius = 20;
    static int floorHeight = 100;
    public static void main(String[] args) {
        first = new ArrayList<Integer>();
        for (int i = level - 1; i >= 0; i--) {
            first.add(i + 1);
        }

        second = new ArrayList<Integer>();
        third = new ArrayList<Integer>();

        Screen s = new Screen();

        draw(s, first, second, third);

       // solve(first, third, second, level);
    }

    public static void solve(ArrayList<Integer> from, ArrayList<Integer> to, ArrayList<Integer> tmp, int level){
        if (level == 1){
            to.add(from.remove(from.size() - 1));
            System.out.println(first);
            System.out.println(second);
            System.out.println(third);
            System.out.println();
        } else{
            solve(from, tmp, to, level - 1);
            to.add(from.remove(from.size() - 1));
            System.out.println(first);
            System.out.println(second);
            System.out.println(third);
            System.out.println();
            solve(tmp, to, from, level - 1);
        }
    }

    public static void draw (Screen s, ArrayList<Integer> first, ArrayList<Integer> second, ArrayList<Integer> third){
        PolygonMatrix polys = new PolygonMatrix();
        for (int i = 0; i < first.size(); i ++){
            int num = first.size() - i;
            polys.addTorus(Screen.XRES / 4, (ringRadius * 10 + 10) * i / level + floorHeight, 
            0, ringRadius, poleRadius * num, 20);
        }
        polys.drawPolygons(s);
        s.display();
    }
}
