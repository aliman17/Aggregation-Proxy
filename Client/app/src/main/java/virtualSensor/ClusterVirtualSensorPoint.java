package virtualSensor;

import clustering.Cluster;

/**
 * Created by ales on 03/08/16.
 */
public class ClusterVirtualSensorPoint extends AVirtualSensorPoint {

    public ClusterVirtualSensorPoint(){};

    public ClusterVirtualSensorPoint(double[] centroid){
        this.setValues(centroid);
    }
}
