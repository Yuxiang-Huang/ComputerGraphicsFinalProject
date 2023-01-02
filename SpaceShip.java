import java.util.*;
import java.util.zip.ZipEntry;

public class SpaceShip {
    public double x, y, z;
    public double xtheta = -Math.PI / 6;
    public double ztheta = Math.PI / 2;
    int size = 15;

    //for explosion
    public boolean expand = true;
    double scale = 1;

    public SpaceShip(int i, int j){
        x = Screen.XRES * (2 * i + 3) / 10;
        y = Screen.YRES * (2 * j + 2) / 12;
    }

    public void escape(WaterDrop sfp){
        ztheta -= Math.PI/2;
        int speed = 5;
        double rotateSpeed = Math.PI / 50;
        //rotate toward runaway
        double direction = Math.atan2(y - sfp.y, x - sfp.x);
        if (Math.abs(direction - ztheta) < rotateSpeed){
            ztheta = direction; 
            speed *= 2;
        } else if (Math.abs(direction - ztheta) < Math.PI){
            ztheta += Math.abs(direction - ztheta) / (direction - ztheta) * rotateSpeed;
        } else{
            ztheta -= Math.abs(direction - ztheta) / (direction - ztheta) * rotateSpeed;
        }

        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;

        ztheta += Math.PI/2;
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
        tmp = new Matrix(Matrix.ROTATE, ztheta - Math.PI / 2, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, xtheta, 'X');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, Math.PI / 2, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        //body
        PolygonMatrix polys = new PolygonMatrix();
        polys.addSphere(0, 0, 0, size, 20);
        polys.addTorus(0, 0, 0, size/2, size * 2, 20);

        //draw
        polys.mult(csystems.peek());
        polys.drawPolygons(s);

        //other parts
        polys = new PolygonMatrix();
        polys.addCylinder(0, size, size * 3, 0, size/4, 20);
        polys.addCone(0, size * 2, size * 4.5, 0, size/3, 20);
        //back
        polys.addCylinder(size, -size/3, -size*4, 0, size/3, 20);
        polys.addCylinder(-size, -size/3, -size*4, 0, size/3, 20);
        polys.addCylinder(0, -size/3, -size*3.5, 0, size/3, 20);
        //front
        polys.addCylinder(size/1.5, size, size*3.5, 0, size/4, 20);
        polys.addCylinder(-size/1.5, size, size*3.5, 0, size/4, 20);

        //rotate
        tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'X');
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        //draw
        polys.mult(csystems.peek());
        polys.drawPolygons(s);
        csystems.pop();
    }

    public void explode(Screen s, ArrayList<SpaceShip> explode){
         //translate to the center of the ship
         Matrix transform = new Matrix();
         transform.ident();
         Stack<Matrix> csystems = new Stack<Matrix>();
         Matrix tmp = new Matrix(Matrix.TRANSLATE, x, y, z);
         tmp.mult(transform);
         csystems.push(tmp.copy());

        PolygonMatrix polys = new PolygonMatrix();
        polys.addSphere(0, 0, 0, scale * size * 2, 20);

        //expand or contract
        if (expand){
            scale += 0.5;
            if (scale >= 3){
                expand = false; 
            }
        } else{
            scale -= 0.25;
            if (scale >= 3){
                expand = false; 
            }
            explode.remove(this);
        }
 
         //draw
         polys.mult(csystems.peek());
         polys.drawPolygons(s);
         csystems.pop();
    }
}
