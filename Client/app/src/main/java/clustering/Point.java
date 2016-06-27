package clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ales on 27/06/16.
 */


public class Point {

    private double x = 0;
    private int cluster_number = 0;

    public Point(double x)
    {
        this.setX(x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX()  {
        return this.x;
    }

    public void setCluster(int n) {
        this.cluster_number = n;
    }

    public int getCluster() {
        return this.cluster_number;
    }

    //Calculates the distance between two points.
    protected static double distance(Point p, Point centroid) {
        return Math.abs(p.getX() - centroid.getX());
    }

    //Creates random point
    protected static Point createRandomPoint(int min, int max) {
        Random r = new Random();
        double x = min + (max - min) * r.nextDouble();
        return new Point(x);
    }

    protected static List createRandomPoints(int min, int max, int number) {
        List points = new ArrayList(number);
        for(int i = 0; i < number; i++) {
            points.add(createRandomPoint(min,max));
        }
        return points;
    }

    public String toString() {
        return ""+x;
    }
}