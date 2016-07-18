package clustering;

/**
 * Created by ales on 27/06/16.
 */
import java.util.ArrayList;

public class Cluster {

    public ArrayList points;
    public Point centroid;
    public int id;

    //Creates a new Cluster
    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList();
        this.centroid = null;
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
