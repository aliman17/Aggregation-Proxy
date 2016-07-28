package periodic;

import android.util.Log;

import java.util.ArrayList;

import clusteringByWindow.Cluster;
import clusteringByWindow.Clustering;
import clusteringByWindow.Point;
import nervousnet.Nervousnet;
import nervousnet.NervousnetSensorPoint;
import state.PossibleStatePoint;

/**
 * Created by ales on 26/07/16.
 */
public class PeriodicExecution extends Thread {

    ArrayList<Point> points;
    Clustering clustering;
    Nervousnet nervousnet;
    state.State state;
    public boolean isRunning = false;
    private static long lastClusteringTimestamp;    // epoch in milliseconds
    private static final long clusteringPeriodInMillisec = 5000;  // in milliseconds

    public static int id = 0;

    public PeriodicExecution(state.State state, ArrayList<Point> points, Clustering clustering, Nervousnet nervousnet){
        this.points = points;
        this.clustering = clustering;
        this.nervousnet = nervousnet;
        this.state = state;             // we'll be updating state.possibleStates periodically
        isRunning = true;
    }

    @Override
    public void run() {

        Log.d("PERIODICITY", "Start periodic execution ...");
        this.isRunning = true;

        id ++;
        int initRecomputeIterations = 50;
        int recomputeIterations = initRecomputeIterations;
        ArrayList<Point> newPoints = new ArrayList<>();

        long currentTimestamp;

        while (this.isRunning) {
            // pause
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // get latest data
            //double latestValue = (double) nervousnet.getSimulatedValue();
            NervousnetSensorPoint latestPoint = nervousnet.getLatestLightValue();

            // classify
            double[] coordinates = latestPoint.getValues(); //TODO: get real data, and timestamp
            Point newPoint = new Point(coordinates, latestPoint);
            newPoints.add(newPoint);
            int clusterNum = clustering.classify(newPoint);
            Log.d("NEW_THREAD", "Goes to cluster " + clusterNum + " running:"+isRunning);

            // Re-cluster if there has passed enough time
            currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - lastClusteringTimestamp > clusteringPeriodInMillisec) {

                // remove old points
                Point tmp = null;
                while (!points.isEmpty()){
                    tmp = points.get(0);
                    NervousnetSensorPoint nsp = (NervousnetSensorPoint) tmp.getOriginalObject();
                    if (nsp.getTimestamp() < lastClusteringTimestamp)
                        points.remove(0);
                    else
                        // All points that are added later, should have better timestamp
                        // even if not, they will be erased eventually during the time
                        break;

                }

                points.addAll(newPoints);       // add new points
                newPoints.clear();              // reset temporary storage for new ones
                clustering.compute(points);     // recompute clusters
                state.clearPossibleStates();    // update possible states

                for(Cluster cluster : clustering.getClusters()) {
                    PossibleStatePoint psp = new PossibleStatePoint();
                    // We store copy of the cluster coordinates, so that any change to the
                    // cluster doesn't effect automatically also the possible state point
                    // It's important to have control over the possible states and how they
                    // are created or changed. So we want to avoid indirect intervention.
                    psp.setCopy(cluster.getCentroid().getCoordinates());
                    state.addPossibleState(psp);
                }

                lastClusteringTimestamp = currentTimestamp; // Update and shift 'window for clustering'
            }
        }
        Log.d("PERIODICITY", "Finished periodic execution!");
    }

    public void stopExecution(){
        Log.d("PERIODICITY", "Accepted stop instruction ...");
        this.isRunning = false;
    }
}
