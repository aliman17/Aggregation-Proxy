package clustering;

import java.util.ArrayList;

/**
 * Created by ales on 20/07/16.
 *
 * This is a class for a cluster, which contains the point (Point) of its location and
 * other details. Additionally, it includes array of all points under its supervision -
 * the points, belonging to this cluster/centroid.
 */
public class Cluster extends Point {

    private int id;
    private ArrayList<Point> points;

    public Cluster(int id, double[] coordinates){
        super(coordinates);
        this.id = id;
        this.points = new ArrayList<Point>();
    }

    public int getId(){
        return id;
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public void setCentroid(double[] centroid){
        this.coordinates = centroid;
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
