package clustering;

/**
 * Created by ales on 27/06/16.
 *
 * Algorithm for clustering, implements class Clustering.
 */
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans implements Clustering {

    private int numOfClusters;
    private int numOfDimensions;
    private ArrayList<Cluster> clusters;

    public KMeans(int numOfDimensions, int numOfClusters) {
        this.numOfDimensions = numOfDimensions;
        this.numOfClusters = numOfClusters;
    }

    //The process to calculate the K Means, with iterating method.
    @Override
    public ArrayList<Cluster> compute(ArrayList<? extends Point> points) {

        Log.d("KMEANS", "Start computing clusters ...");

        boolean finish = false;

        initClusters(points);

        if (points.size() <= 0) finish = true;

        // Add in new data, one at a time, recalculating centroids with each new one.
        while(!finish) {
            //Clear cluster state
            clearClusters(clusters);

            List<Cluster> lastCentroids = getCentroids(clusters);

            //Assign points to the closer cluster
            boolean trainingPhase = true;
            assignCluster(points, clusters);

            //Calculate new centroids
            calculateCentroids(clusters);

            List<Cluster> currentCentroids = getCentroids(clusters);

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for(int i = 0; i < lastCentroids.size(); i++)
                distance += Util.distance(lastCentroids.get(i).getCentroid(),
                        currentCentroids.get(i).getCentroid());

            if(distance == 0) {
                finish = true;
            }
            if(Double.isNaN(distance))
                finish = true;
            Log.d("KMEAN", "Cluster diff ... "+distance);
        }

        for(Cluster c : clusters)
            Log.d("KMEANS-final-centroids", Arrays.toString(c.getCentroid()) + "");

        Log.d("KMEANS", "Computing clusters finished!");
        return clusters;
    }

    @Override
    public Cluster classify(Point point) {
        double min = Double.MAX_VALUE;
        Cluster cluster = null;

        for(int i = 0; i < this.numOfClusters; i++) {
            Cluster c = clusters.get(i);
            double distance = Util.distance( point.coordinates, c.getCentroid() );
            if(distance < min){
                min = distance;
                cluster = c;
            }
        }
        return cluster;
    }

    @Override
    public Cluster classify(double[] point) {
        double min = Double.MAX_VALUE;
        Cluster cluster = null;

        for(int i = 0; i < this.numOfClusters; i++) {
            Cluster c = clusters.get(i);
            double distance = Util.distance(c.getCentroid(), point);
            if(distance < min){
                min = distance;
                cluster = c;
            }
        }
        return cluster;
    }

    @Override
    public ArrayList<Cluster> getClusters() {
        return this.clusters;
    }


    private void initClusters(ArrayList<? extends Point> points){

        clusters = new ArrayList<>();
        int sizeOfPoints = points.size();
        Random rand = new Random();
        Log.d("INIT-CLUSTER", "Size " + sizeOfPoints);
        // Choose random centroids
        for (int i = 0; i < this.numOfClusters; i++){
            // Get one of the points as an initial cluster
            Integer newRandomInt = rand.nextInt(sizeOfPoints);
            Point point = points.get(newRandomInt);
            double[] coordinates = point.getCoordinates();
            // Just add a little bit of disturbation into the first coordinate
            // so that clusters don't overlap in case of selecting the same point
            // several times
            for( int j = 0; j < coordinates.length; j++ )
                coordinates[j] += coordinates[j] * (1 + rand.nextDouble());
            Cluster c = new Cluster(coordinates);
            clusters.add(c);
            Log.d("KMEANS-init-centroids", Arrays.toString(c.getCentroid()) + "");
        }
    }

    private void clearClusters(ArrayList<Cluster> clusters) {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private List<Cluster> getCentroids(ArrayList<Cluster> clusters) {
        ArrayList centroids = new ArrayList();
        for(Cluster cluster : clusters) centroids.add(cluster);
        return centroids;
    }

    private void assignCluster(List<? extends Point> points, ArrayList<Cluster> clusters) {
        for(Point point : points) {
            Cluster cluster = classify(point);
            point.setCluster(cluster);
            cluster.addPoint(point);
        }
    }

    private void calculateCentroids(ArrayList<Cluster> clusters) {
        for(Cluster cluster : clusters) {

            ArrayList<Point> points = cluster.getPoints();
            int n_points = points.size();
            double[] sum = new double[this.numOfDimensions];  // initialized to 0;

            for(Point point : points) {
                double[] coordinates = point.getCoordinates();
                for (int dim = 0; dim < this.numOfDimensions; dim++){
                    sum[dim] += coordinates[dim];
                }
            }

            // Average it
            for (int dim = 0; dim < this.numOfDimensions; dim++)
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