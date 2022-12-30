import java.awt.*;
import java.util.*;

public interface WaterDrop {
    public static void main(String[] args) {
        Screen s = new Screen();
        EdgeMatrix edges = new EdgeMatrix();
        // edges.addCurve(200, 200, 200, 490, -200, 0, 200, 430, 0.01, Matrix.HERMITE);
        PolygonMatrix polys = new PolygonMatrix();

        edges.addCurve(200, 200, 200, 450, -175, 0, 100, 400, 0, Matrix.HERMITE, 0.01, 20);

        //polys.drawPolygons(s);
        edges.drawEdges(s, new Color(0, 0, 255));
        
        s.display();
    }
}
