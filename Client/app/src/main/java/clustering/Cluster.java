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

    private int id;
    private Point centroid;
    private ArrayList<Point> points;

    public Cluster(int id){
        this.id = id;
        this.centroid = null;
        this.points = new ArrayList<Point>();
    }

    public Cluster(int id, double[] centroid){
        this.id = id;
        setCentroid(centroid);
        this.points = new ArrayList<Point>();
    }

    public Cluster(int id, Point point){
        this.id = id;
        this.centroid = point;
        this.points = new ArrayList<Point>();
    }

    public int getId(){
        return id;
    }

    public Point getCentroid(){
        return centroid;
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public void setCentroid(double[] centroid){
        this.centroid = new Point(centroid);
    }

    public void setCentroid(Point centroid){
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
