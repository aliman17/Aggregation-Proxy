package clustering;

/**
 * Created by ales on 27/06/16.
 */
import java.util.ArrayList;
import java.util.List;

public class KMeans_first_example implements Clustering{

    private int NUM_CLUSTERS = 3;

    private List<Cluster> clusters;

    private static final int MIN_COORDINATE = 0;
    private static final int MAX_COORDINATE = 10;

    public KMeans_first_example() {
        this.clusters = new ArrayList();
        this.init();
        //plotClusters();
    }

    private void init(){
        //Create Clusters
        //Set Random Centroids
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = Point.createRandomPoint(MIN_COORDINATE,MAX_COORDINATE);
            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }
    }

    //The process to calculate the K Means, with iterating method.
    public void compute(List<Point> points) {
        boolean finish = false;
        int iteration = 0;

        // Add in new data, one at a time, recalculating centroids with each new one.
        while(!finish) {
            //Clear cluster state
            clearClusters();

            List<Point> lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster(points);

            //Calculate new centroids.
            calculateCentroids();

            iteration++;

            List<Point> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for(int i = 0; i < lastCentroids.size(); i++)
                distance += Point.distance(lastCentroids.get(i), currentCentroids.get(i));

            if(distance == 0) {
                finish = true;
            }
        }
    }

    private void clearClusters() {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private List<Point> getCentroids() {
        List centroids = new ArrayList(NUM_CLUSTERS);
        for(Cluster cluster : clusters) centroids.add(cluster.getCentroid());
        return centroids;
    }

    private void assignCluster(List<Point> points) {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for(Point point : points) {
            min = max;
            for(int i = 0; i < NUM_CLUSTERS; i++) {
                Cluster c = clusters.get(i);
                distance = Point.distance(point, c.getCentroid());
                if(distance < min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            List<Point> list = cluster.getPoints();
            int n_points = list.size();

            for(Point point : list) {
                sumX += point.getX();
            }

            Point centroid = cluster.getCentroid();
            if(n_points < 0) {
                double newX = sumX / n_points;
                centroid.setX(newX);
            }
        }
    }

    public List<Cluster> getClusters(){
        return this.clusters;
    }
}
