package clustering;

import android.support.v4.content.res.TypedArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ales on 28/06/16.
 */
public interface Clustering {
    public void compute(List<Point> point);
    public List<Cluster> getClusters();
}
