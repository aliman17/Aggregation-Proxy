package clustering;

/**
 * Created by ales on 27/06/16.
 */
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class KMeans implements Clustering{

    private int NUM_CLUSTERS = 3;

    public KMeans() {}

    private void initClusters(ArrayList clusters, ArrayList<Point> points){

        // Find min and max
        Point minpoint = points.get(0);
        Point maxpoint = points.get(0);
        for (Point p : points){
            double pValue = p.getX();
            if (pValue < minpoint.getX())
                minpoint = p;
            if (pValue > maxpoint.getX())
                maxpoint = p;
        }

        // Set centroids equidistand along the x-axis in the range of the data points
        double h = (maxpoint.getX() - minpoint.getX()) / NUM_CLUSTERS; // bandwidth
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = new Point(minpoint.getX() + i*h + h/2);
            cluster.setCentroid(centroid);
            clusters.add(cluster);
            Log.d("KMEANS-init-controids", centroid.getX()+"");
        }
    }

    //The process to calculate the K Means, with iterating method.
    @Override
    public ArrayList<Cluster> compute(ArrayList<Point> points) {

        ArrayList<Cluster> clusters = new ArrayList();
        boolean finish = false;

        initClusters(clusters, points);

        // Add in new data, one at a time, recalculating centroids with each new one.
        while(!finish) {
            //Clear cluster state
            clearClusters(clusters);

            List<Point> lastCentroids = getCentroids(clusters);

            //Assign points to the closer cluster
            assignCluster(points, clusters);

            //Calculate new centroids
            calculateCentroids(clusters);

            List<Point> currentCentroids = getCentroids(clusters);

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for(int i = 0; i < lastCentroids.size(); i++)
                distance += Point.distance(lastCentroids.get(i), currentCentroids.get(i));

            if(distance == 0) {
                finish = true;
            }
        }

        for(Cluster c : clusters)
            Log.d("KMEANS-final-centroids", c.getCentroid().getX() + "");
        return clusters;
    }

    private void clearClusters(ArrayList<Cluster> clusters) {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private List<Point> getCentroids(ArrayList<Cluster> clusters) {
        List centroids = new ArrayList();
        for(Cluster cluster : clusters) centroids.add(cluster.getCentroid());
        return centroids;
    }

    private void assignCluster(List<Point> points, ArrayList<Cluster> clusters) {
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

    private void calculateCentroids(ArrayList<Cluster> clusters) {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            List<Point> list = cluster.getPoints();
            int n_points = list.size();

            for(Point point : list) {
                sumX += point.getX();
            }

            Point centroid = cluster.getCentroid();
            if(n_points > 0) {
                double newX = sumX / n_points;
                centroid.setX(newX);
            }
        }
    }
}