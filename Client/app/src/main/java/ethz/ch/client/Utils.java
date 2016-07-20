package ethz.ch.client;

import java.util.ArrayList;
import java.util.Random;

import clusteringByWindow.Point;

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
            double[] coord = {rand.nextDouble()* 10, rand.nextDouble()* 10};
            points.add(new Point(coord));
        }

        for (int i = 0; i < 10; i++){
            double[] coord = {rand.nextDouble()* 15 + 10, rand.nextDouble()* 20};
            points.add(new Point(coord));
        }

        for (int i = 0; i < 10; i++){
            double[] coord = {rand.nextDouble()* 20, rand.nextDouble()* 30 + 20};
            points.add(new Point(coord));
        }
        return points;
    }
}
