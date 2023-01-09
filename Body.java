public class Body{
    public double x;
    public double y;
    public double z;
    public double dx;
    public double dy;
    public double dz;
    double m;
    double G = 6.67E-11;
  
    int initialV = 3;
  
    // public Body (int mass){
    //   x = Math.random() * 300 + 100;
    //   y = Math.random() * 300 + 100;
    //   z = Math.random() * 300 + 100;
    //   dx = Math.random() * initialV * 2 - initialV;
    //   dy = Math.random() * initialV * 2 - initialV;
    //   //dz = Math.random() * initialV * 2 - initialV;
    //   m = mass;
    // }
  
    public Body (double mass, int type){
      m = mass;
      double radius = Math.random() * 50 + 150;
      double angle = 0;
  
      if (type == 0){
        angle += 0;
        angle += Math.random() * Math.PI * 1 / 3;
      }
  
      else if (type == 1){
        angle += Math.PI * 2 / 3;
        angle += Math.random() * Math.PI * 1 / 3;
      }
  
      else if (type == 2){
        angle += Math.PI * 4 / 3;
        angle += Math.random() * Math.PI * 1 / 3;
      } 
      
      else{
        angle = Math.random() * Math.PI * 2;
      }
  
      //System.out.println(angle);
  
      x = Math.cos(angle) * radius + Screen.XRES/2;
      y = Math.sin(angle) * radius + Screen.YRES/2;
      z = Math.random() * 100 + 100;

      dx = Math.random() * initialV;
      if (x > Screen.XRES/2){
        dx *= -1;
      }
      dy = Math.random() * initialV;
      if (y > Screen.YRES/2){
        dy *= -1;
      }
      dz = Math.random() * initialV * 2 - initialV;
    }
  
    public double dist(Body other){
        return Math.sqrt(Math.abs(x - other.x) * Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(y - other.y)
        + Math.abs(z - other.z) * Math.abs(z - other.z));
      }
  
    public void attract (Body other, double t){
        double d = dist(other);
        double a = G * other.m / d / d;

        //shell theorem
        if (d < 15){
          a = G * other.m * d / 15 / 15 / 15;
        }

        double theta = Math.atan2(other.y - y, other.x - x);
        double phi = Math.acos((other.z - z) / d);

        double ax = a * Math.cos(theta) * Math.sin(phi);
        double ay = a * Math.sin(theta) * Math.sin(phi);
        double az = a * Math.cos(phi);

        //System.out.println("x: " + x + ", y: " + y);

        x += dx * t + 0.5 * ax * t * t;
        y += dy * t + 0.5 * ay * t * t;
        y += dz * t + 0.5 * az * t * t;
    
        dx += ax * t;
        dy += ay * t;
        dz += az * t;
    }
  }