package clustering;

import java.util.ArrayList;

/**
 * Created by ales on 20/07/16.
 * This class represents one point with its own coordinates. It keeps reference to
 * the original object and cluster number.
 */
public class Point {
    protected int dimensions;
    protected double[] coordinates;
    protected Cluster cluster;

    public Point(int dim){
        this.dimensions = dim;
        this.coordinates = new double[dim]; // initialized to 0 values
    }

    public Point(double[] coordinates){
        this.dimensions = coordinates.length;
        this.coordinates = coordinates;
    }

    public int getDimensions(){
        return dimensions;
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public Cluster getCluster(){
        return cluster;
    }

    public void setCoordinate(int i, double value){
        if (this.coordinates == null)
            this.coordinates = new double[this.dimensions];
        this.coordinates[i] = value;
    }

    public void setCopyOfCoordinates(double[] coordinates){
        // We copy the array, that changes on passed array do not effect Point
        if (this.coordinates == null){
            this.dimensions = coordinates.length;
            this.coordinates = new double[coordinates.length];
        }
        for (int i = 0; i < dimensions; i++)
            this.coordinates[i] = coordinates[i];
    }

    public void setCluster(Cluster c){
        this.cluster = c;
    }

    public static double distance(int dimensions, Point p1, Point p2){
        // Get data
        double[] coord1 = p1.getCoordinates();
        double[] coord2 = p2.getCoordinates();
        // Sum of squares
        double sum = 0;
        for (int i = 0; i < dimensions; i++){
            double diff = coord1[i] - coord2[i];
            sum += diff * diff;
        }
        sum = Math.sqrt(sum);
        // Return
        return sum;
    }
}
