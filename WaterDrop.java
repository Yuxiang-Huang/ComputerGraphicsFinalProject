import java.awt.*;
import java.util.*;

public interface WaterDrop {
    public static void main(String[] args) {
        Screen s = new Screen();
        //EdgeMatrix edges = new EdgeMatrix();
        // edges.addCurve(200, 200, 200, 490, -200, 0, 200, 430, 0.01, Matrix.HERMITE);

        PolygonMatrix polys = new PolygonMatrix();

        polys.addCurve(200, 200, 200, 490, -200, 0, 200, 430, 0.01, Matrix.HERMITE, 20);
        polys.drawPolygons(s);
        
        s.display();
    }
}
