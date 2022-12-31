import java.awt.*;

public class Planet{
  double size;
  double dist;
  double revTime;
  double theta;
  double x;
  double y;
  Color c;
  int[][] rgb;

  double selfRotate = 0;
  double selfRotateTime;

  public Planet(double size, double dist, double revTime, double selfRotateTime, Color c, int[][] rgb, double theta){
    this.size = size;
    this.dist = dist;
    this.revTime = revTime;
    this.selfRotateTime = selfRotateTime;
    this.c = c;
    this.theta = theta;
    x = Math.cos(theta) * dist;
    y = Math.sin(theta) * dist;
    this.rgb = rgb;
  }

  public void update(){
    theta += 2 * Math.PI / revTime;
    selfRotate += 2 * Math.PI / selfRotateTime;
    x = Math.cos(theta) * dist;
    y = Math.sin(theta) * dist;
  }
}