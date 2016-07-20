package clusteringByWindow;

import java.util.ArrayList;

/**
 * Created by ales on 28/06/16.
 */
public interface Clustering {
    public ArrayList<Cluster> compute(ArrayList<Point> points);
}
