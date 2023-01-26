import java.awt.*;
import java.util.*;

public class Sun extends Planet{
    Earth earth;

    public Sun(String name, double size, double dist, double revTime, double selfRotateTime, int[][] rgb, double theta,
  ArrayList<Color> planet2D, Earth earth){
        super(name, size, dist, revTime, selfRotateTime, rgb, theta, planet2D);
        this.earth = earth;
    }

    @Override
    public void update(int limit){
        if (limit > (x + Screen.XRES / 2 + size)){
          displaySize -= 1.5; 
          earth.sunlight += 0.5;
        } else{
          theta -= 2 * Math.PI / revTime;
          selfRotate -= 2 * Math.PI / selfRotateTime;
          x = Math.cos(theta) * dist;
          y = Math.sin(theta) * dist;
        }
    }
}
