package clustering;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ales on 20/07/16.
 * This class represents one point with its own coordinates. It keeps reference to
 * the original object and cluster number.
 */
public class Point {
    protected double[] coordinates;
    protected Cluster cluster;
    protected Object reference;

    public Point(double[] coordinates){
        this.coordinates = coordinates;
    }

    public Point(double[] coordinates, Object ref){
        this.coordinates = coordinates;
        this.reference = ref;
    }

    public int getDimensions(){
        return coordinates.length;
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public Cluster getCluster(){
        return cluster;
    }

    public Object getReference(){
        return this.reference;
    }

    public void setReference(Object ref){
        this.reference = ref;
    }

    public void setCluster(Cluster c){
        this.cluster = c;
    }

}
