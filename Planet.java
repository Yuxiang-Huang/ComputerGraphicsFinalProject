import java.awt.*;

public class Planet{
  double size;
  double radius;
  double revTime;
  double theta;
  double x;
  double y;
  Color c;
  int[][] rgb;

  double selfRotate = 0;
  double selfRotateTime;

  public Planet(double size, double radius, double revTime, double selfRotateTime, Color c, int[][] rgb){
    this.size = size;
    this.radius = radius;
    this.revTime = revTime;
    this.selfRotateTime = selfRotateTime;
    this.c = c;
    theta = Math.random() * 2 * Math.PI;
    x = Math.cos(theta) * radius;
    y = Math.sin(theta) * radius;
    this.rgb = rgb;
  }

  public void update(){
    theta += 2 * Math.PI / revTime;
    selfRotate += 2 * Math.PI / selfRotateTime;
    x = Math.cos(theta) * radius;
    y = Math.sin(theta) * radius;
  }
}