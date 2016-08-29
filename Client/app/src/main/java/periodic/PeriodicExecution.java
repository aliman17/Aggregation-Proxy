package periodic;

import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

import clustering.Cluster;
import clustering.Clustering;
import data.DataSourceHelper;
import data.iDataSource;
import sensor.VirtualSensorPoint;
import state.PossibleStatePoint;

/**
 * Created by ales on 26/07/16.
 */
public class PeriodicExecution extends Thread {

    ArrayList<VirtualSensorPoint> points;
    Clustering clustering;
    iDataSource dataSource;

    state.State state;

    public boolean isRunning = false;

    private static long lastClusteringTimestamp;    // epoch in milliseconds
    private static final long clusteringPeriodInMillisec = 5000;  // in milliseconds

    private static int sleepTime = 100;

    public static int id = 0;

    public PeriodicExecution(state.State state, ArrayList<VirtualSensorPoint> points, Clustering clustering, iDataSource dataSource){
        this.points = points;
        this.clustering = clustering;
        this.dataSource = dataSource;
        this.state = state;             // we'll be updating state.possibleStates periodically
        isRunning = true;
    }

    @Override
    public void run() {

        Log.d("PERIODICITY", "Start periodic execution ...");
        this.isRunning = true;

        points.clear();
        try {
            points.addAll( DataSourceHelper.getInitData( dataSource ) );
        } catch (RemoteException e) {
            e.printStackTrace();
            // TODO: Instead of terminting here, we can probably still proceed
            return;
        }
        Log.d("PERIODIC-INIT-SIZE", "" + points.size());

        clustering.compute(points);

        ArrayList<VirtualSensorPoint> newPoints = new ArrayList<>();

        long currentTimestamp;

        while (this.isRunning) {
            // pause
            try {
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // get latest data
            VirtualSensorPoint newPoint = null;
            try {
                newPoint = DataSourceHelper.getNextVirtualSensorPoint( dataSource );
            } catch (RemoteException e) {
                e.printStackTrace();
                continue;
            }
            newPoints.add(newPoint);
            Cluster cluster = clustering.classify(newPoint);
            Log.d("NEW_THREAD", "Goes to cluster " + cluster.getCoordinates() + " running:"+isRunning);

            // Re-cluster if there has passed enough time
            currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - lastClusteringTimestamp > clusteringPeriodInMillisec) {

                // remove old points
                VirtualSensorPoint tmp = null;
                while (!points.isEmpty()){
                    tmp = (VirtualSensorPoint) points.get(0);
                    if (tmp.getTimestamp() < lastClusteringTimestamp)
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

                for(Cluster c : clustering.getClusters()) {
                    PossibleStatePoint psp = new PossibleStatePoint();
                    // We store copy of the cluster coordinates, so that any change to the
                    // cluster doesn't effect automatically also the possible state point
                    // It's important to have control over the possible states and how they
                    // are created or changed. So we want to avoid indirect intervention.
                    psp.setCopy(c.getCoordinates());
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
