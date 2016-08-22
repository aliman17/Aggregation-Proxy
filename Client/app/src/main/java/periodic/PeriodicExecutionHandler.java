package periodic;

import android.util.Log;

import java.util.ArrayList;

import clustering.Clustering;
import clustering.Point;
import data.DataSourceHelper;
import data.iDataSource;
import state.State;

/**
 * Created by ales on 27/07/16.
 */
public class PeriodicExecutionHandler implements iPeriodicExecutionHandler {

    public static PeriodicExecution periodicExecution;
    public static ArrayList<Point> points;
    public static Clustering clustering;
    private State state;
    iDataSource dataSource;

    public PeriodicExecutionHandler() {}

    public PeriodicExecutionHandler(State state, Clustering clustering, iDataSource dataSource){
        if (points == null)
            this.points = new ArrayList<>();
        this.clustering = clustering;
        this.dataSource = dataSource;
        this.state = state;
    }

    /**
     * Initializes starting points and starts periodic execution of update and clustering
     */
    public void start() {
        // If periodic execution doesn't exist, create one
        if (periodicExecution == null){
            Log.d("BUTTON", "Nervousnet button pressed and is under execution ...");
            periodicExecution = new PeriodicExecution(state, points, clustering, dataSource);
            periodicExecution.start();
        }
    }

    public void stop(){
        // Stop existing periodic execution
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
