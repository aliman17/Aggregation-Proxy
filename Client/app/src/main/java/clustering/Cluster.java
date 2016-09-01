package clustering;

import java.util.ArrayList;

/**
 * Created by ales on 20/07/16.
 *
 * This is a class for a cluster, which contains the point (Point) of its location and
 * other details. Additionally, it includes array of all points under its supervision -
 * the points, belonging to this cluster/centroid.
 */
public class Cluster {

    private double[] centroid;
    private ArrayList<Point> points;

    public Cluster(double[] centroid){
        setCentroid(centroid);
        this.points = new ArrayList<Point>();
    }

    public double[] getCentroid(){
        return centroid;
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public void setCentroid(double[] centroid){
        this.centroid = centroid;
    }

    public void setPoints(ArrayList<Point> points){
        this.points = points;
    }

    public void clear() {
        this.points.clear();
    }

    public void addPoint(Point point){
        this.points.add(point);
    }

}
