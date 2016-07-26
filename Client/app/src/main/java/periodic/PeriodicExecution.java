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
        id ++;
        while (this.isRunning) {
            // pause
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // get latest data
            double latestValue = (double) nervousnet.getSimulatedValue();

            // classify
            double[] coordinates = {latestValue, 1}; // we add 1 just for the y dimension
            Point newPoint = new Point(coordinates);
            //points.add(newPoint);
            int clusterNum = clustering.classify(newPoint);
            Log.d("NEW_THREAD", "Goes to cluster " + clusterNum + " running:"+isRunning);

            // check if window has to be moved
        }
        Log.d("NEW_THREAD", "FINISHED");
    }

    public void stopExecution(){
        this.isRunning = false;
    }
}
