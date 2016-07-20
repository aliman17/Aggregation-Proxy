package ethz.ch.client;

import java.util.ArrayList;
import java.util.Random;

import clustering.Point;

/**
 * Created by ales on 20/07/16.
 */
public class Utils {

    public static ArrayList<Point> randomPoints(){
        // Test data
        ArrayList<Point> points = new ArrayList<>();

        // Create some random values
        Random rand = new Random();
        for (int i = 0; i < 10; i++){
            points.add(new Point(rand.nextDouble()* 10));
        }

        for (int i = 0; i < 10; i++){
            points.add(new Point(rand.nextDouble()* 10 + 15));
        }

        for (int i = 0; i < 10; i++){
            points.add(new Point(rand.nextDouble()* 10 + 20));
        }
        return points;
    }
}
