package ethz.ch.client;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import clustering.Cluster;
import clustering.Clustering;
import clustering.KMeans;
import clustering.Point;
import sensor.SensorPoint;
import plot.GraphPlot;

/**
 * Created by ales on 20/07/16.
 */
public class Utils {

    public static ArrayList<Point> randomPoints(){
        // Test data
        ArrayList<Point> points = new ArrayList<>();

        // Create some random values
        Random rand = new Random();
        for (int i = 0; i < 10; i++){
            double[] coord = {rand.nextDouble()* 10, rand.nextDouble()* 10};
            points.add(new Point(coord));
        }

        for (int i = 0; i < 10; i++){
            double[] coord = {rand.nextDouble()* 15 + 10, rand.nextDouble()* 20};
            points.add(new Point(coord));
        }

        for (int i = 0; i < 10; i++){
            double[] coord = {rand.nextDouble()* 20, rand.nextDouble()* 30 + 20};
            points.add(new Point(coord));
        }
        return points;
    }

    public static ArrayList<Point> initPoints(){
        Log.d("BUTTON", "Initialize points ...");
        ArrayList<Point> points = new ArrayList<>();
        //for(Double d : data){
        //double[] coordinates = {d.doubleValue(), 1};
        for(int i = 0; i < 30; i++)   { ;
            Random rand = new Random();
            double[] coordinates = {rand.nextDouble(), i+1};

            points.add(new Point(coordinates));
        }
        return points;
    }

    public static Clustering computeClusters(ArrayList<Point> points){
        Log.d("BUTTON", "Compute clusters ...");
        // Cluster initially
        int dim = 2;
        int nOfClusters = 5;
        Clustering clustering = new KMeans(dim, nOfClusters);
        clustering.compute(points);
        return clustering;
    }

    protected void plot(GraphPlot graph, ArrayList<Point> points, ArrayList<Cluster> clusters) {
        Log.d("PLOTTING", "Start plotting ...");
        try {
            graph.plot(points, clusters);
        } catch (Exception e) {

        }
        Log.d("PLOTTING", "Plotting completed!");

        //this.nervousnetText.setText(WriteJSON.serialize("possibleStates", clusters));
        //this.sendResponse.setText("Click 'SEND' to update sever ...");

    }

    public static ArrayList<Point> initialClusteringPoints(){
        ArrayList<Point> points = new ArrayList<>();
        //for(Double d : data){
        //double[] coordinates = {d.doubleValue(), 1};
        for(int i = 0; i < 10; i++)   { ;
            Random rand = new Random();
            double[] coordinates = {rand.nextDouble() * 600, 1};
            SensorPoint nsp = new SensorPoint(0, System.currentTimeMillis(), coordinates);
            points.add(new Point(coordinates, nsp));
        }
        return points;
    }

}
