import java.util.*;

public class TowerOfHanoi {
    static int level = 5;
    static ArrayList<Integer> first;
    static ArrayList<Integer> second;
    static ArrayList<Integer> third;

    static int poleRadius = 20;
    static int ringRadius = 20;
    static int floorHeight = 200;
    public static void main(String[] args) {
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

        draw(s, csystems, first, second, third);

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

    public static void draw (Screen s, Stack<Matrix> csystems, ArrayList<Integer> first, ArrayList<Integer> second, ArrayList<Integer> third){
        PolygonMatrix polys = new PolygonMatrix();
        //poles
        polys.addCylinder(Screen.XRES / 4, floorHeight, Screen.YRES - floorHeight, 0, poleRadius - 5, 20);
        polys.addCylinder(Screen.XRES / 2, floorHeight, Screen.YRES - floorHeight, 0, poleRadius - 5, 20);
        polys.addCylinder(Screen.XRES * 3 / 4, floorHeight, Screen.YRES - floorHeight, 0, poleRadius - 5, 20);

        //first rings
        for (int i = 0; i < first.size(); i ++){
            int num = first.size() - i;
            polys.addTorus(Screen.XRES / 4, (ringRadius * 10) * (i + 1) / level + floorHeight, 
            0, ringRadius, poleRadius * num, 20);
        }

        // //rotate
        // Matrix tmp = new Matrix(Matrix.ROTATE, Math.PI/6, 'X');
        // tmp.mult(csystems.peek());
        // csystems.push(tmp.copy());

        //draw
        polys.mult(csystems.peek());
        polys.drawPolygons(s);

        //csystems.pop();

        s.display();
    }
}
