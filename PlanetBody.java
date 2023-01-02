public class PlanetBody{
    public double x;
    public double y;
    public double z;
    public double dx;
    public double dy;
    public double dz;
    double m;
    double G = 1;
  
    public PlanetBody (double mass){
      m = mass;
    }
  
    public double dist(Body other){
      return Math.sqrt(Math.abs(x - other.x) * Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(y - other.y));
    }
  
    public void attract (Body other){
        double ax = 0;
        if (x - other.x != 0){
            ax = (x - other.x) / Math.abs(x - other.x) / Math.abs(x - other.x) / Math.abs(x - other.x) * G * other.m;
        }

        double ay = 0;
        if (y - other.y != 0){
            ay = (y - other.y) / Math.abs(y - other.y) / Math.abs(y - other.y) / Math.abs(y - other.y) * G * other.m;
        }

        // System.out.println("ax: " + ax);
        // System.out.println("ay: " + ay);
    
        dx -= ax;
        dy -= ay;
        // dz += (other.z - z) / d / d * G * m * other.m;
    }
  }