package periodic;

import android.util.Log;

import java.util.ArrayList;

import clusteringByWindow.Cluster;
import clusteringByWindow.Clustering;
import clusteringByWindow.Point;
import ethz.ch.client.Utils;
import nervousnet.Nervousnet;

/**
 * Created by ales on 27/07/16.
 */
public class PeriodicExecutionHandler {

    static PeriodicExecution periodicExecution;
    ArrayList<Point> points;
    Clustering clustering;
    Nervousnet nervousnet;

    public PeriodicExecutionHandler() {}

    public PeriodicExecutionHandler(Nervousnet nervousnet){
        /*this.points = points;
        this.clustering = clustering;*/
        this.nervousnet = nervousnet;
    }

    public void start() {

        if (periodicExecution == null){
            Log.d("BUTTON", "Nervousnet button pressed and is under execution ...");
            points = Utils.initPoints();
            clustering = Utils.computeClusters(points);
            periodicExecution = new PeriodicExecution(points, clustering, nervousnet);
            periodicExecution.start();
        }
        else {
            //buttonNervousnet.setText("Stopping ...");
            periodicExecution.stopExecution();
            try {
                periodicExecution.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            periodicExecution = null;
        }
            //buttonNervousnet.setText("Get nervousnet data");
    }

    public void stop(){
        if (periodicExecution != null){
            periodicExecution.stopExecution();
            try {
                periodicExecution.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            periodicExecution = null;
        }
    }
}
