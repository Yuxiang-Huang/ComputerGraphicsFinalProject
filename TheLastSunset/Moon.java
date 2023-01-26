import java.awt.*;
import java.security.cert.PKIXCertPathValidatorResult;
import java.util.*;

public class Moon extends Planet{
    boolean orbit = true;

    double dx;
    double dy;

    public Moon(String name, double size, double dist, double revTime, double selfRotateTime, int[][] rgb, double theta,
  ArrayList<Color> planet2D){
        super(name, size, dist, revTime, selfRotateTime, rgb, theta, planet2D);
    }

    @Override
    public void update(int limit){
        if (limit > (x + 250 + size)){
            displaySize -= 1.5; 
        } else{
            theta += 2 * Math.PI / revTime;
            selfRotate += 2 * Math.PI / selfRotateTime;
            x = Math.cos(theta) * dist;
            y = Math.sin(theta) * dist;
        }
    }
}
