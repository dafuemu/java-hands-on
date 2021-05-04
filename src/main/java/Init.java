import data.ColorPoint;
import data.Point;

import java.awt.*;

public class Init {

    public static void main(String... args){

        Point p = new Point(1, 2);
        ColorPoint cp = new ColorPoint(1, 2, Color.RED);
        boolean first = p.equals(cp);
        boolean second = cp.equals(p);
        System.out.println("first is: " + first);
        System.out.println("second is: " + second);
    }
}
