import java.util.*;
import java.awt.*;

public class WaterDrop {
    public double x, y, z;
    int size = 30;
    double theta = 0;
    public boolean acc = true;
    public boolean intro = true;
    public boolean end = false;

    double direction = 0;
    double rotateSpeed = Math.PI * 2 / 100;

    //for expanding
    double expand = 0;
    double expandX = 0;
    double expandY = 0;
    double expandAngle = 0;

    //special movement
    boolean lock0 = true;
    boolean lock1 = true;

    double[] ambient, diffuse, specular;

    public WaterDrop(double[] ambient, double[] diffuse, double[] specular){
        x = Screen.XRES/2;
        y = Screen.YRES;
        z = 0;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public void update(ArrayList<SpaceShip> ships, ArrayList<SpaceShip> explode){
        int speed = 20;
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
            direction += Math.PI/2;
        }
    }

    public void end(){
        int speed = 25;
        while (speed > 0){
            //direction
            double targetx = Screen.XRES/2;
            double targety = Screen.YRES/2;
            direction = Math.atan2(targety - y, targetx - x);
            double distance = Math.sqrt((x - targetx) * (x - targetx) + (y - targety) * (y - targety));
            //move toward it
            if (distance < speed){
                x = targetx;
                y = targety;
                speed = 0;
            } else{
                //not enough speed
                x = speed * Math.cos(direction) + x;
                y = speed * Math.sin(direction) + y;
                speed = 0;
            }
            direction += Math.PI/2;
        }
    }

    public static double dist (SpaceShip ship, WaterDrop sfp){
        return Math.sqrt((sfp.x - ship.x) * (sfp.x - ship.x) + (sfp.y - ship.y) * (sfp.y - ship.y));
    }

    public void display(Screen s, GfxVector view, Color amb, ArrayList<GfxVector> lightPos, Color lightColor){
        //translate to the center of the ship
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, z);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        if (end){ //at viewer
            tmp = new Matrix(Matrix.ROTATE, -Math.PI/2, 'X');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());

            //self rotate
            theta += rotateSpeed;
            tmp = new Matrix(Matrix.ROTATE, theta, 'Y');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());
        } else{
            //direction
            tmp = new Matrix(Matrix.ROTATE, direction, 'Z');
            tmp.mult(csystems.peek());
            csystems.pop();
            csystems.push(tmp.copy());

            //self rotate
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
        }

        //draw
        PolygonMatrix polys = new PolygonMatrix();
        polys.addCurve(0, 0, 0, size, -size, 0, 0, size, 0, Matrix.HERMITE, 20);
        polys.mult(csystems.peek());
        polys.drawPolygons(s, view, amb, lightPos, lightColor, ambient, diffuse, specular);

        //accelearation animation
        if (acc){
            if (expand == 0){
                expandX = x;
                expandY = y;
                expandAngle = direction + Math.PI/2;
            }
            if (end){
                endAnimateAcc(s, view, amb, lightPos, lightColor);
            } else{
                animateAcc(s, view, amb, lightPos, lightColor);
            }
            if (expand >= 2){
                expand = 0;
            } else{
                expand += 0.4;
            }
        }
    }

    void endAnimateAcc(Screen s, GfxVector view, Color amb, ArrayList<GfxVector> lightPos, Color lightColor){
        direction += Math.PI/2;

        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp;

        //translate to the center
        tmp = new Matrix(Matrix.TRANSLATE, expandX, expandY, z);
        tmp.mult(transform);
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'X');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //dilates
        tmp = new Matrix(Matrix.SCALE, size / 10, size / 10, size / 10);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());
        tmp = new Matrix(Matrix.SCALE, expand, expand, expand);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        //draw
        PolygonMatrix polys = new PolygonMatrix();
        polys.addTorus(0, 0, z, 1, 5, 20);
        polys.mult(csystems.peek());
        polys.drawPolygons(s, view, amb, lightPos, lightColor, ambient, diffuse, specular);
    }

    void animateAcc(Screen s, GfxVector view, Color amb, ArrayList<GfxVector> lightPos, Color lightColor){
        direction += Math.PI/2;

        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp;

        //translate to the center of the halo
        if (intro){
            tmp = new Matrix(Matrix.TRANSLATE, expandX + size * 5 * Math.cos(expandAngle), 
            expandY + size * 5 * Math.sin(expandAngle), z);
            tmp.mult(transform);
            csystems.push(tmp.copy());
        } else{
            tmp = new Matrix(Matrix.TRANSLATE, expandX + size * Math.cos(expandAngle), 
            expandY + size * Math.sin(expandAngle), z);
            tmp.mult(transform);
            csystems.push(tmp.copy());
        }

        //direction
        tmp = new Matrix(Matrix.ROTATE, expandAngle - Math.PI/2, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //dilates
        if (intro){
            tmp = new Matrix(Matrix.SCALE, 5, 1, 5);
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
        polys.drawPolygons(s, view, amb, lightPos, lightColor, ambient, diffuse, specular);

        //lines
        csystems = new Stack<Matrix>();

        //translate to the tail
        if (intro){
            tmp = new Matrix(Matrix.TRANSLATE, x + size * 5 * Math.cos(direction), 
            y + size * 5 * Math.sin(direction), z);
            tmp.mult(transform);
            csystems.push(tmp.copy());
        } else{
            tmp = new Matrix(Matrix.TRANSLATE, x + size * Math.cos(direction), 
            y + size * Math.sin(direction), z);
            tmp.mult(transform);
            csystems.push(tmp.copy());
        }

        //dilates
        if (intro){
            tmp = new Matrix(Matrix.SCALE, 1, 5, 1);
            tmp.mult(csystems.peek());
            csystems.push(tmp.copy());
        }

        //draw
        double b = 255;
        double lastX = 0;
        double lastY = 0;
        for (int i = 0; i < 50; i ++){
            EdgeMatrix edges = new EdgeMatrix();
            edges.addEdge(lastX, lastY, 0, Math.cos(direction) + lastX, Math.sin(direction) + lastY, 0);
            if (intro){
                edges.addEdge(lastX-1, lastY, 0, lastX - 1, Math.sin(direction) + lastY, 0);
                edges.addEdge(lastX+1, lastY, 0, lastX + 1, Math.sin(direction) + lastY, 0);
            }
            lastX += Math.cos(direction);
            lastY += Math.sin(direction);

            edges.mult(csystems.peek());
            if (i == 0){
                edges.drawEdges(s, new Color(255, 255, 255));
            } else{
                edges.drawEdges(s, new Color(0, 0, (int) b));
                b -= 255.0 / 50;
            }
        }

        direction -= Math.PI/2;
    }
}
