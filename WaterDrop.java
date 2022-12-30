import java.awt.*;
import java.util.*;

public interface WaterDrop {
    public static void main(String[] args) {
        Screen s = new Screen();
        //EdgeMatrix edges = new EdgeMatrix();
        // edges.addCurve(200, 200, 200, 490, -200, 0, 200, 430, 0.01, Matrix.HERMITE);

        PolygonMatrix polys = new PolygonMatrix();

        polys.addCylinder(250, 100, 450, 0, 100, 20);

        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.ROTATE, Math.PI/4 ,'X');
        tmp.mult(transform);
        csystems.push(tmp.copy());

        polys.mult(csystems.peek());

        polys.drawPolygons(s);
        
        s.display();
    }
}
