import java.awt.*;

public interface WaterDrop {
    public static void main(String[] args) {
        Screen s = new Screen();
        EdgeMatrix edges = new EdgeMatrix();
        edges.addCurve(200, 200, 200, 490, -200, 0, 200, 430, 0.01, Matrix.HERMITE);
        //edges.addCurve(150, 150, 350, 150, -100, -100, 100, 150, 0.01, Matrix.HERMITE);
        edges.drawEdges(s, new Color(0, 0, 255));
        // s.drawLine(150, 0, 150, 500, new Color(0, 0, 255));
        // s.drawLine(350, 0, 350, 500, new Color(0, 0, 255));
        // s.drawLine(0, 150, 500, 150, new Color(0, 0, 255));
        s.display();
    }
}
