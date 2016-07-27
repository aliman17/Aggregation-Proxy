package periodic;

import android.util.Log;

import java.lang.reflect.Array;
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

    public static PeriodicExecution periodicExecution;
    public static ArrayList<Point> points;
    public static Clustering clustering;
    Nervousnet nervousnet;

    public PeriodicExecutionHandler() {}

    public PeriodicExecutionHandler(Nervousnet nervousnet){
        this.points = new ArrayList<>();
        //this.clustering = clustering;
        this.nervousnet = nervousnet;
    }

    public PeriodicExecutionHandler(ArrayList<Point> points, Nervousnet nervousnet){
        this.points = points;
        //this.clustering = clustering;
        this.nervousnet = nervousnet;
    }

    public void start() {

        if (periodicExecution == null){
            Log.d("BUTTON", "Nervousnet button pressed and is under execution ...");
            // TODO: get data from nervousnet. Real data, not simulated one.
            points.clear();
            points.addAll( Utils.initPoints() ); // We don't want to lose reference
                                                // we want changes to be visible outside
                                                // of the this class too,
                                                // for instance for plotting
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
