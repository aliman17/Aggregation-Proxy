package clustering;

/**
 * Created by ales on 27/06/16.
 */
import java.util.ArrayList;

public class Cluster {

    public int id;              // id of the cluster
    public Point centroid;      // centroid of the cluster, based on the points
    public ArrayList points;    // point of the cluster

    public Cluster(int id) {
        this.id = id;
        this.centroid = null;
        this.points = new ArrayList();
    }

    public ArrayList getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void setPoints(ArrayList points) {
        this.points = points;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public int getId() {
        return id;
    }

    public void clear() {
        points.clear();
    }
}
