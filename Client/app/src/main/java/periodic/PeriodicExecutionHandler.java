package periodic;

import android.util.Log;

import java.util.ArrayList;

import clusteringByWindow.Clustering;
import clusteringByWindow.Point;
import ethz.ch.client.Utils;
import nervousnet.Nervousnet;
import state.State;

/**
 * Created by ales on 27/07/16.
 */
public class PeriodicExecutionHandler {

    public static PeriodicExecution periodicExecution;
    public static ArrayList<Point> points;
    public static Clustering clustering;
    private State state;
    Nervousnet nervousnet;

    public PeriodicExecutionHandler() {}

    public PeriodicExecutionHandler(State state, Clustering clustering, Nervousnet nervousnet){
        if (points == null)
            this.points = new ArrayList<>();
        this.clustering = clustering;
        this.nervousnet = nervousnet;
        this.state = state;
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
            points.addAll( Utils.initialClusteringPoints() ); // We don't want to lose reference
                                                // we want changes to be visible outside
                                                // of the this class too,
                                                // for instance for plotting
            clustering.compute(points);
            periodicExecution = new PeriodicExecution(state, points, clustering, nervousnet);
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
