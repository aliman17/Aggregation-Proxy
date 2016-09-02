package periodic;

import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import clustering.Point;
import clustering.iCluster;
import clustering.iClustering;
import clustering.iPoint;
import database.iDatabase;
import virtualSensor.ClusterVirtualSensorPoint;
import virtualSensor.DataSourceHelper;
import data.iDataSource;
import virtualSensor.OriginalVirtualSensorPoint;
import virtualSensor.VirtualPoint;
import state.PossibleStatePoint;

/**
 * Created by ales on 26/07/16.
 */
public class PeriodicExecution extends Thread {

    iDatabase database;
    ArrayList<iPoint> points;
    iClustering clustering;
    iDataSource dataSource;

    state.State state;

    public boolean isRunning = false;

    private static long lastClusteringTimestamp;    // epoch in milliseconds
    private static final long clusteringIntervalInMillisec = 5000;  // in milliseconds

    private static int sleepTime = 100;

    public static int id = 0;

    public PeriodicExecution(state.State state, iClustering clustering, iDataSource dataSource,
                             iDatabase database){
        this.state = state;             // we'll be updating state.possibleStates periodically
        this.clustering = clustering;
        this.dataSource = dataSource;
        this.database = database;
        isRunning = false;
    }

    @Override
    public void run() {

        Log.d("PERIODICITY", "Start periodic execution ...");
        this.isRunning = true;

        points = new ArrayList<iPoint>();

        // 1. Get virtual sensor data

        try {
            ArrayList<OriginalVirtualSensorPoint> original = DataSourceHelper.getInitData( dataSource );
            for (OriginalVirtualSensorPoint o : original){
                // Create new point with the reference of virtual point
                points.add(new Point(o.getValues(), new VirtualPoint(null, o)));
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            // TODO: Instead of terminting here, we can probably still proceed
            return;
        }
        Log.d("PERIODIC-INIT-SIZE", "" + points.size());

        // 2. compute clusters
        clustering.compute(points);

        // 3. add new points to database
        for( iPoint p : points ){
            VirtualPoint vp = (VirtualPoint)p.getReference();
            vp.setCluster(new ClusterVirtualSensorPoint(p.getCoordinates()));
            Log.d("TEST", "TEST");
            this.addVPtoDB(vp);
        }

        ArrayList<iPoint> newPoints = new ArrayList<>();

        long currentTimestamp;

        while (this.isRunning) {
            // 1. pause
            try {
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 2. get latest data
            OriginalVirtualSensorPoint newOrigPoint = null;
            try {
                newOrigPoint = DataSourceHelper.getNextVirtualSensorPoint( dataSource );
            } catch (RemoteException e) {
                e.printStackTrace();
                continue;
            }
            iPoint newPoint = new Point(newOrigPoint.getValues(), new VirtualPoint(null, newOrigPoint));

            // 3. compute cluster
            iCluster cluster = clustering.classify(newOrigPoint.getValues());

            // 4. create virtual point
            ClusterVirtualSensorPoint newCVirtualPoint = new ClusterVirtualSensorPoint(cluster.getCentroid());
            ((VirtualPoint)(newPoint.getReference())).setCluster(newCVirtualPoint);
            newPoints.add(newPoint);

            Log.d("NEW_THREAD", "Goes to cluster " + Arrays.toString(cluster.getCentroid()) + " running:"+isRunning);

            // 5. Re-cluster if there has passed enough time
            currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - lastClusteringTimestamp > clusteringIntervalInMillisec) {

                // 6. remove old points
                Log.d("PERIODIC", "Remove old points");
                iPoint tmp = null;
                while (!points.isEmpty()){
                    tmp = points.get(0);
                    if (((VirtualPoint)tmp.getReference()).getOriginal().getTimestamp() < lastClusteringTimestamp) {
                        points.remove(0);
                        database.delete(((VirtualPoint) tmp.getReference()).getOriginal().getTimestamp());
                    }
                    else
                        // All points that are added later, should have better timestamp
                        // even if not, they will be erased eventually during the time
                        break;

                }
                // 7. add new points
                points.addAll(newPoints);       // add new points
                for (iPoint p : newPoints){
                    this.addVPtoDB((VirtualPoint) p.getReference());
                }
                newPoints.clear();              // reset temporary storage for new ones
                clustering.compute(points);     // recompute clusters
                state.clearPossibleStates();    // update possible states

                // 8. Recluster everything
                for(iCluster c : clustering.getClusters()) {
                    PossibleStatePoint psp = new PossibleStatePoint();
                    // We store copy of the cluster coordinates, so that any change to the
                    // cluster doesn't effect automatically also the possible state point
                    // It's important to have control over the possible states and how they
                    // are created or changed. So we want to avoid indirect intervention.
                    psp.setCopy(c.getCentroid());
                    state.addPossibleState(psp);
                }

                // 9. Update database

                lastClusteringTimestamp = currentTimestamp; // Update and shift 'window for clustering'
            }
        }
        Log.d("PERIODICITY", "Finished periodic execution!");
    }

    public void stopExecution(){
        Log.d("PERIODICITY", "Accepted stop instruction ...");
        this.isRunning = false;
    }

    public iClustering getClustering(){
        return this.clustering;
    }


    public void addVPtoDB(VirtualPoint vp){
        ClusterVirtualSensorPoint cluster = vp.getCluster();
        OriginalVirtualSensorPoint original = vp.getOriginal();
        database.add(original.getTimestamp(),
                cluster.getNoise(),
                cluster.getLight(),
                cluster.getBattery(),
                cluster.getAccelerometer()[0],
                cluster.getAccelerometer()[1],
                cluster.getAccelerometer()[2],
                cluster.getGryometer()[0],
                cluster.getGryometer()[1],
                cluster.getGryometer()[2],
                cluster.getProximity(),
                original.getNoise(),
                original.getLight(),
                original.getBattery(),
                original.getAccelerometer()[0],
                original.getAccelerometer()[1],
                original.getAccelerometer()[2],
                original.getGryometer()[0],
                original.getGryometer()[1],
                original.getGryometer()[2],
                original.getProximity()
        );
    }
}
