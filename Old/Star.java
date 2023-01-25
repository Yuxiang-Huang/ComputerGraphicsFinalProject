import java.util.*;

public class Star {
    public static void main(String[] args) throws Exception {
        Screen s = new Screen();
    
        //set up the world at the center
        Matrix transform = new Matrix();
        transform.ident();
        Stack<Matrix> csystems = new Stack<Matrix>();
        Matrix tmp = new Matrix(Matrix.TRANSLATE, Screen.XRES/2, Screen.YRES/2, 250);
        tmp.mult(transform);
        csystems.push(tmp.copy());
    
        tmp = new Matrix(Matrix.ROTATE, -Math.PI / 3, 'X');
        tmp.mult(csystems.peek());
        csystems.push(tmp.copy());
    
        PolygonMatrix polys = new PolygonMatrix();
        polys.addStar(0, 0, -100, 100, 100, 40);
        //polys.addSphere(0, 0, 0, 100, 20);
        polys.mult(csystems.peek());
        polys.drawPolygons(s);
    
        s.display();
      }
}
