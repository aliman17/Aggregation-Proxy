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
    protected static double distance(Point p1, Point p2) {
        return Math.abs(p1.getX() - p2.getX());
    }

    public String toString() {
        return ""+x;
    }
}