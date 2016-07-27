package periodic;

import android.util.Log;

import java.util.ArrayList;

import clusteringByWindow.Clustering;
import clusteringByWindow.Point;
import nervousnet.Nervousnet;

/**
 * Created by ales on 26/07/16.
 */
public class PeriodicExecution extends Thread {

    ArrayList<Point> points;
    Clustering clustering;
    Nervousnet nervousnet;
    private boolean isRunning;

    public static int id = 0;

    public PeriodicExecution(ArrayList<Point> points, Clustering clustering, Nervousnet nervousnet){
        this.points = points;
        this.clustering = clustering;
        this.nervousnet = nervousnet;
        isRunning = true;
    }

    @Override
    public void run() {

        Log.d("PERIODICITY", "Start periodic execution ...");
        id ++;
        int initRecomputeIterations = 10;
        int recomputeIterations = initRecomputeIterations;
        ArrayList<Point> newPoints = new ArrayList<>();

        while (this.isRunning) {
            // pause
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // get latest data
            double latestValue = (double) nervousnet.getSimulatedValue();

            // classify
            double[] coordinates = {latestValue, 1}; //TODO: get real data
            Point newPoint = new Point(coordinates);
            newPoints.add(newPoint);
            int clusterNum = clustering.classify(newPoint);
            Log.d("NEW_THREAD", "Goes to cluster " + clusterNum + " running:"+isRunning);

            // check if window has to be moved
            recomputeIterations --;
            if (recomputeIterations <= 0) {
                recomputeIterations = initRecomputeIterations; // reset back to init
                // update point list
                points.addAll(newPoints);   // add new points to the original list
                newPoints.clear();          // reset temporary storage for new ones
                // delete old points
                for (int i = 0; i < initRecomputeIterations; i++) points.remove(0);
                // recompute clusters
                clustering.compute(points);
            }
        }
        Log.d("PERIODICITY", "Finished periodic execution!");
    }

    public void stopExecution(){
        Log.d("PERIODICITY", "Accepted stop instruction ...");
        this.isRunning = false;
    }
}
