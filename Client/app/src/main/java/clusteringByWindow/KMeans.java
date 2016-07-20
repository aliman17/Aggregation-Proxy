package clusteringByWindow;

/**
 * Created by ales on 27/06/16.
 */
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans implements Clustering {

    private int NUM_CLUSTERS = 3;
    private int nOfDimensions;

    public KMeans(int dim) {
        this.nOfDimensions = dim;
    }

    private ArrayList<Cluster> initClusters(ArrayList<Point> points){

        //double[] minCoord = generateMinCoord(points);
        //double[] maxCoord = generateMaxCoord(points);

        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        int sizeOfPoints = points.size();
        Random rand = new Random();

        // Choose random centroids
        for (int i = 0; i < NUM_CLUSTERS; i++){
            Point centroid = points.get(rand.nextInt(sizeOfPoints));
            Cluster c = new Cluster(i, centroid);
            clusters.add(c);
            Log.d("KMEANS-init-centroids", Arrays.toString(c.getCentroid().getCoordinates()) + "");
        }
        return clusters;
    }


    //The process to calculate the K Means, with iterating method.
    @Override
    public ArrayList<Cluster> compute(ArrayList<Point> points) {

        boolean finish = false;

        ArrayList<Cluster> clusters = initClusters(points);

        if (points.size() <= 0) finish = true;

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
            Log.d("KMEAN", "Working ..."+distance);
        }

        for(Cluster c : clusters)
            Log.d("KMEANS-final-centroids", Arrays.toString(c.getCentroid().getCoordinates()) + "");
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

            ArrayList<Point> points = cluster.getPoints();
            int n_points = points.size();
            double[] sum = new double[this.nOfDimensions];  // initialized to 0;

            for(Point point : points) {
                for (int dim = 0; dim < this.nOfDimensions; dim++){
                    sum[dim] += point.getCoordinates()[dim];
                }
            }

            // Average it
            for (int dim = 0; dim < nOfDimensions; dim++)
                sum[dim] = sum[dim] / n_points;

            // Set cluster to new coordinates
            cluster.setCentroid(sum);
        }
    }



    private double[] generateMinCoord(ArrayList<Point> points){
        int size = points.size();
        int nOfDimensions = points.get(0).getDimensions();
        double[] minCoord = points.get(0).getCoordinates();
        double[] curCoord = null;

        for (int i = 0; i < size; i++){
            curCoord = points.get(i).getCoordinates();
            for (int dim = 0; dim < nOfDimensions; dim++){
                if (curCoord[dim] < minCoord[dim])
                    minCoord[dim] = curCoord[dim];
            }
        }
        return minCoord;
    }

    private double[] generateMaxCoord(ArrayList<Point> points){
        int size = points.size();
        int nOfDimenstions = points.get(0).getDimensions();
        double[] maxCoord = points.get(0).getCoordinates();
        double[] curCoord = null;

        for (int i = 0; i < size; i++){
            curCoord = points.get(i).getCoordinates();
            for (int dim = 0; dim < nOfDimenstions; dim++){
                if (curCoord[dim] > maxCoord[dim])
                    maxCoord[dim] = curCoord[dim];
            }
        }
        return maxCoord;
    }

}