package clustering;

import java.util.ArrayList;

/**
 * Created by ales on 28/06/16.
 *
 * Interface specifies the exact structure of any clustering algorithm.
 */
public interface Clustering {
    public ArrayList<Cluster> compute(ArrayList<? extends Point> points);
    public Cluster classify(Point point);
    public ArrayList<Cluster> getClusters();
}
