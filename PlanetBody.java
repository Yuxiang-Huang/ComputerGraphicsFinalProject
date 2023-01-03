public class PlanetBody{
    public double x;
    public double y;
    public double z;
    public double dx;
    public double dy;
    public double dz;
    double m;
    double G = 6.67E-11;
    double t = 0.001;
  
    public PlanetBody (double mass){
      m = mass;
    }
  
    public double dist(Body other){
      return Math.sqrt(Math.abs(x - other.x) * Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(y - other.y));
    }
  
    public void attract (Body other){
        for (int i = 0; i < 0.5 / t; i ++){
            double d = dist(other);
            double a = G * other.m / d / d;

            double theta = Math.atan2(other.y - y, other.x - x);
            double ax = a * Math.cos(theta);
            double ay = a * Math.sin(theta);

            //System.out.println("x: " + x + ", y: " + y);

            x += dx * t + 0.5 * ax * t * t;
            y += dy * t + 0.5 * ay * t * t;
        
            dx += ax * t;
            dy += ay * t;
            // dz += (other.z - z) / d / d * G * m * other.m;
        }
    }
  }