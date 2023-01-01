import java.util.*;

public class WaterDrop {
    public static double x, y, z;
    double theta = 0;
    public boolean acc = false;
    public boolean intro = true;

    public WaterDrop(){
        x = Screen.XRES/2;
        y = Screen.YRES;
        z = 0;
    }

    public void update(ArrayList<SpaceShip> ships){
        SpaceShip target = ships.get(0);
        double distance = dist(target);
        //find closest ship
        for (SpaceShip ship : ships){
            if (dist(ship) < distance){
                distance = dist(ship);
                target = ship;
            }
        }

        //move toward it
        int speed = 10;
        if (Math.abs(x - target.x) <= speed){
            x = target.x;
        } else{
            x = Math.abs(target.x - x) / (target.x - x) * speed + x;
        }

        if (Math.abs(y - target.y) <= speed){
            y = target.y;
        } else{
            y = Math.abs(target.y - y) / (target.y - y) * speed + y;
        }

        //destroy it
        if (x == target.x && y == target.y){
            ships.remove(target);
        }
    }

    public static double dist (SpaceShip ship){
        return Math.sqrt((x - ship.x) * (x - ship.x) + (y - ship.y) * (y - ship.y));
    }

    public void display(Screen s){
        //translate to the center of the ship
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, z);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        //rotate
        tmp = new Matrix(Matrix.ROTATE, theta, 'Y');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //dilate
        if (intro){
            tmp = new Matrix(Matrix.SCALE, 5, 5, 5);
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());
        }

        //draw
        PolygonMatrix polys = new PolygonMatrix();
        polys.addCurve(0, 0, 0, 50, -50, 0, 0, 50, 0, Matrix.HERMITE, 20);
        if (acc){
            polys.addTorus(0, 35, z, 1, 7, 20);
        }
        polys.mult(csystems.peek());
        polys.drawPolygons(s);
    }
}
