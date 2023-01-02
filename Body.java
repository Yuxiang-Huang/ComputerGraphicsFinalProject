public class Body{
    public double x;
    public double y;
    public double z;
    public double dx;
    public double dy;
    public double dz;
    int m;
    double G = 0.5;
  
    int initialV = 5;
  
    // public Body (int mass){
    //   x = Math.random() * 300 + 100;
    //   y = Math.random() * 300 + 100;
    //   z = Math.random() * 300 + 100;
    //   dx = Math.random() * initialV * 2 - initialV;
    //   dy = Math.random() * initialV * 2 - initialV;
    //   //dz = Math.random() * initialV * 2 - initialV;
    //   m = mass;
    // }
  
    public Body (int mass, int type){
      m = mass;
      dx = Math.random() * initialV * 2 - initialV;
      dy = Math.random() * initialV * 2 - initialV;
      //dz = Math.random() * initialV * 2 - initialV;
  
      double radius = Math.random() * 50 + 100;
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
  
      //System.out.println(angle);
  
      x = Math.cos(angle) * radius + 250;
      y = Math.sin(angle) * radius + 250;
      z = Math.random() * 150 + 100;
    }
  
    // public Body (int mass, int type){
    //   m = mass;
    //
    //   if (type == 0){
    //     x = Math.random() * 150 + 50;
    //     y = Math.random() * 150 + 50;
    //
    //     dx = Math.random() * initialV + initialV;
    //     dy = Math.random() * initialV + initialV;
    //   }
    //
    //   else if (type == 1){
    //     x = Math.random() * 150 + 300;
    //     y = Math.random() * 150 + 50;
    //
    //     dx = - (Math.random() * initialV + initialV);
    //     dy = Math.random() * initialV + initialV;
    //   }
    //
    //   else if (type == 2){
    //     x = Math.random() * 150 + 50;
    //     y = Math.random() * 150 + 300;
    //
    //     dx = Math.random() * initialV + initialV;
    //     dy = - (Math.random() * initialV + initialV);
    //   }
    //
    //   else if (type == 3){
    //     x = Math.random() * 150 + 300;
    //     y = Math.random() * 150 + 300;
    //
    //     dx = - (Math.random() * initialV + initialV);
    //     dy = - (Math.random() * initialV + initialV);
    //   }
    // }
  
    public double dist(Body other){
        return Math.sqrt(Math.abs(x - other.x) * Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(y - other.y));
      }
  
    public void attract (Body other){
      double d = dist(other);
      other.dx += (x - other.x) / d / d * G * m * other.m;
      other.dy += (y - other.y) / d / d * G * m * other.m;
      other.dz += (z - other.z) / d / d * G * m * other.m;
  
      dx += (other.x - x) / d / d * G * m * other.m;
      dy += (other.y - y) / d / d * G * m * other.m;
      dz += (other.z - z) / d / d * G * m * other.m;
  
      // System.out.println();
      // System.out.println(m);
      // System.out.println(dx);
      // System.out.println(dy);
    }
  }