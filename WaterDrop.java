import java.util.*;

public class WaterDrop {
    public double x, y, z;
    double theta = 0;
    public boolean acc = false;
    public boolean intro = true;

    double direction = 0;
    double rotateSpeed = Math.PI * 2 / 100;

    double expand = 0;
    double expandX = 0;
    double expandY = 0;

    //special movement
    boolean lock0 = true;
    boolean lock1 = true;

    public WaterDrop(){
        x = Screen.XRES/2;
        y = Screen.YRES;
        z = 0;
    }

    public void update(ArrayList<SpaceShip> ships, ArrayList<SpaceShip> explode){
        int speed = 25;
        while (speed > 0){
            //no more targets
            if (ships.size() == 0){
                break;
            } 
            //special movements
            if (ships.size() == 15 && lock0){
                //end of first col
                direction = - Math.PI/2;
                double distance = y;
                if (distance < speed){
                    y = 0;
                    speed -= distance;
                    lock0 = false;
                } else{
                    x = speed * Math.cos(direction) + x;
                    y = speed * Math.sin(direction) + y;
                    speed = 0;
                }
            }
            else if (ships.size() == 10 && lock1){
                //end of second col
                direction = Math.PI/2;
                double distance = Screen.YRES - y;
                if (distance < speed){
                    y = Screen.YRES;
                    speed -= distance;
                    lock1 = false;
                } else{
                    x = speed * Math.cos(direction) + x;
                    y = speed * Math.sin(direction) + y;
                    speed = 0;
                }
            } else{
                SpaceShip target = ships.get(0);
                double distance = dist(target, this);
                //find closest ship
                for (SpaceShip ship : ships){
                    if (dist(ship, this) < distance){
                        distance = dist(ship, this);
                        target = ship;
                    }
                    //ship responses
                    if (ships.size() <= 5){
                        ship.escape(this);
                    } else if (ships.size() <= 10){
                        ship.random();
                    } else if (ships.size() > 15){
                        ship.forward();
                    }
                }

                //direction
                direction = Math.atan2(target.y - y, target.x - x);

                //move toward it
                if (distance < speed){
                    //destroy
                    x = target.x;
                    y = target.y;
                    explode.add(target);
                    ships.remove(target);
                    speed -= distance;
                } else{
                    //not enough speed
                    x = speed * Math.cos(direction) + x;
                    y = speed * Math.sin(direction) + y;
                    speed = 0;
                }
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
        if (intro){
            tmp = new Matrix(Matrix.ROTATE, direction, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());
        } else{
            tmp = new Matrix(Matrix.ROTATE, direction + Math.PI/2, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());
        }

        //rotate
        theta += rotateSpeed;
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
        polys.mult(csystems.peek());
        polys.drawPolygons(s);

        //accelearation animation
        if (acc){
            if (expand == 0){
                expandX = x;
                expandY = y;
            }
            animateAcc(s);
            if (expand >= 2){
                expand = 0;
            } else{
                expand += 0.4;
            }
        }
    }

    void animateAcc(Screen s){
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp;

        //translate to the center of the halo
        if (intro){
            tmp = new Matrix(Matrix.TRANSLATE, expandX + 35 * 5 * Math.cos(direction + Math.PI/2), 
            expandY + 35 * 5 * Math.sin(direction + Math.PI/2), z);
            tmp.mult(transform);
            csystems.push(tmp.copy());
        } else{
            tmp = new Matrix(Matrix.TRANSLATE, expandX+ 35 * Math.cos(direction), 
            expandY + 35 * Math.sin(direction), z);
            tmp.mult(transform);
            csystems.push(tmp.copy());
        }

        //direction
        if (intro){
            tmp = new Matrix(Matrix.ROTATE, direction, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());
        } else{
            tmp = new Matrix(Matrix.ROTATE, direction + Math.PI/2, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());
        }

        //dilates
        if (intro){
            tmp = new Matrix(Matrix.SCALE, 5, 5, 5);
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());
        }
        tmp = new Matrix(Matrix.SCALE, expand, 1, 1);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        //draw
        PolygonMatrix polys = new PolygonMatrix();
        polys.addTorus(0, 0, z, 1, 5, 20);
        polys.mult(csystems.peek());
        polys.drawPolygons(s);
    }
}
