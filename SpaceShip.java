import java.util.*;

public class SpaceShip {
    public double x, y, z;
    public double xtheta = Math.PI / 6;
    public double ztheta = 0;
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

        //shapes
        PolygonMatrix polys = new PolygonMatrix();
        polys.addSphere(0, 0, 0, size, 20);
        polys.addTorus(0, 0, 0, size/2, size * 2, 20);
        polys.addCylinder(0, 0, 100, 0, size/2, 20);

        //rotate
        tmp = new Matrix(Matrix.ROTATE, xtheta, 'X');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        tmp = new Matrix(Matrix.ROTATE, ztheta, 'Z');
        tmp.mult(csystems.peek());
        csystems.pop();
        csystems.push(tmp.copy());

        polys.mult(csystems.peek());
        polys.drawPolygons(s);
    }
}
