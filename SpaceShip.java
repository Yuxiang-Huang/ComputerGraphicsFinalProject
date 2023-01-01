import java.util.*;

public class SpaceShip {
    public double x, y, z;
    public double xtheta = -Math.PI / 6;
    public double ztheta = Math.PI / 2;
    int size = 30;

    public SpaceShip(int i, int j){
        x = i;
        y = j;
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
        tmp = new Matrix(Matrix.ROTATE, xtheta, 'X');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, Math.PI/2, 'Y');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, ztheta, 'Z');
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
}
