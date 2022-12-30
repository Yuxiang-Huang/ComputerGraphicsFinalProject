import java.awt.*;

public interface WaterDrop {
    public static void main(String[] args) {
        Screen s = new Screen();
        EdgeMatrix edges = new EdgeMatrix();
        edges.addCurve(200, 200, 200, 490, -200, 0, 200, 430, 0.01, Matrix.HERMITE);
        edges.drawEdges(s, new Color(0, 0, 255));
        s.display();
    }
}
