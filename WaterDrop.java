import java.util.*;

public class WaterDrop {
    public double x, y, z;
    double theta = 0;

    public WaterDrop(){
        x = Screen.XRES/2;
        y = Screen.YRES;
        z = 0;
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
        tmp = new Matrix(Matrix.SCALE, 10, 10, 10);
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());

        //draw
        PolygonMatrix polys = new PolygonMatrix();
        polys.addCurve(0, 0, 0, 25, -25, 0, 0, 25, 0, Matrix.HERMITE, 20);
        polys.mult(csystems.peek());
        polys.drawPolygons(s);
    }
}
