import java.util.*;

public class WaterDrop {
    public double x, y, z;
    double theta = 0;
    public boolean acc = false;
    public boolean intro = true;

    double direction = 0;

    public WaterDrop(){
        x = Screen.XRES/2;
        y = Screen.YRES;
        z = 0;
    }

    public void update(ArrayList<SpaceShip> ships){
        if (ships.size() == 0){
            //move toward spectator
        } else{
            SpaceShip target = ships.get(0);
            double distance = dist(target, this);
            //find closest ship
            for (SpaceShip ship : ships){
                if (dist(ship, this) < distance){
                    distance = dist(ship, this);
                    target = ship;
                }
                if (ships.size() <= 10){
                    ship.escape(this);
                }
            }

            //direction
            direction = Math.atan2(target.y - y, target.x - x);

            //move toward it
            int speed = 25;
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
                target.destroy(ships);
            }
        }
    }

    public static double dist (SpaceShip ship, WaterDrop sfp){
        return Math.sqrt((sfp.x - ship.x) * (sfp.x - ship.x) + (sfp.y - ship.y) * (sfp.y - ship.y));
    }

    public void display(Screen s){
        //translate to the center of the ship
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, z);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        //direction
        tmp = new Matrix(Matrix.ROTATE, direction + Math.PI/2, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //rotate
        theta += Math.PI * 2 / 100;
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
