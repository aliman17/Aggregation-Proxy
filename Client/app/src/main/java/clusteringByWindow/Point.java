package clusteringByWindow;

import java.util.ArrayList;

/**
 * Created by ales on 20/07/16.
 */
public class Point {
    private int numOfDimensions;
    private double[] coordinates;
    private Object referenceToOriginalObj;
    private int clusterNumber;

    public Point(double[] coordinates, Object referenceToOriginalObj){
        this.numOfDimensions = coordinates.length;
        this.coordinates = new double[this.numOfDimensions];
        setCoordinates(coordinates);
        this.referenceToOriginalObj = referenceToOriginalObj;
        this.clusterNumber = -1;    // at the beginning, cluster number is unknown
        // so we initialize to -1
    }

    public Point(double[] coordinates){
        this.numOfDimensions = coordinates.length;
        this.coordinates = new double[this.numOfDimensions];
        setCoordinates(coordinates);
        this.referenceToOriginalObj = null;
        this.clusterNumber = -1;    // at the beginning, cluster number is unknown
        // so we initialize to -1
    }

    public int getDimensions(){
        return numOfDimensions;
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public int getCluster(){
        return clusterNumber;
    }

    public Object getOriginalObject() {
        return this.referenceToOriginalObj;
    }

    public void setCoordinate(int i, double value){
        this.coordinates[i] = value;
    }

    public void setCoordinates(ArrayList<Double> coordinates){
        for (int i = 0; i < numOfDimensions; i++)
            this.coordinates[i] = coordinates.get(i);
    }

    public void setCoordinates(double[] coordinates){
        // We copy the array, that changes on passed array do not effect Point
        for (int i = 0; i < numOfDimensions; i++)
            this.coordinates[i] = coordinates[i];
    }

    public void setCluster(int clusterNumber){
        this.clusterNumber = clusterNumber;
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
