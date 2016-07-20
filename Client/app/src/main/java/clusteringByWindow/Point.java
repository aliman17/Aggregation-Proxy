package clusteringByWindow;

import java.util.ArrayList;

/**
 * Created by ales on 20/07/16.
 */
public class Point {
    private int nOfDimensions;
    private double[] coordinates;
    private int clusterNumber;

    public Point(ArrayList<Double> coordinates){
        this.nOfDimensions = coordinates.size();
        this.coordinates = new double[this.nOfDimensions];
        setCoordinates(coordinates);
        this.clusterNumber = -1;    // at the beginning, cluster number is unknown
                                    // so we initialize to -1
    }

    public Point(double[] coordinates){
        this.nOfDimensions = coordinates.length;
        this.coordinates = new double[this.nOfDimensions];
        setCoordinates(coordinates);
        this.clusterNumber = -1;    // at the beginning, cluster number is unknown
        // so we initialize to -1
    }

    public int getDimensions(){
        return nOfDimensions;
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public int getCluster(){
        return clusterNumber;
    }

    public void setCoordinate(int i, double value){
        this.coordinates[i] = value;
    }

    public void setCoordinates(ArrayList<Double> coordinates){
        for (int i = 0; i < nOfDimensions; i++)
            this.coordinates[i] = coordinates.get(i);
    }

    public void setCoordinates(double[] coordinates){
        // We copy the array, that changes on passed array do not effect Point
        for (int i = 0; i < nOfDimensions; i++)
            this.coordinates[i] = coordinates[i];
    }

    public void setCluster(int clusterNumber){
        this.clusterNumber = clusterNumber;
    }

    public static double distance(Point p1, Point p2){
        // Get data
        int nOfDimensions = p1.getDimensions();
        double[] coord1 = p1.getCoordinates();
        double[] coord2 = p2.getCoordinates();
        // Sum of squares
        double sum = 0;
        for (int i = 0; i < nOfDimensions; i++){
            double diff = coord1[i] - coord2[i];
            sum += diff * diff;
        }
        sum = Math.sqrt(sum);
        // Return
        return sum;
    }
}
