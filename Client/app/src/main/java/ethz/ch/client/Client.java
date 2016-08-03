package ethz.ch.client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import clusteringByWindow.Cluster;
import clusteringByWindow.KMeans;
import clusteringByWindow.Clustering;
import json.WriteJSON;
import sensor.iSensorSource;
import nervousnet.Nervousnet;
import periodic.PeriodicExecutionHandler;
import plot.GraphPlot;
import state.State;

public class Client extends Activity {

    State state;
    iSensorSource dataSource;
    Clustering clustering;
    GraphPlot graph;
    int numOfClusters = 3;
    int numOfDimensions = 2;
    TextView sendResponse, textNervousnet;
    Button buttonConnect, buttonNervousnet;
    OnClickListener buttonConnectOnClickListener;
    OnClickListener buttonNervousnetOnClickListener;

    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create front-end view
        setContentView(R.layout.activity_client);

        // Store element on the view in arguments
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonNervousnet = (Button)findViewById(R.id.nervousnet);

        sendResponse = (TextView)findViewById(R.id.sendResponse);
        textNervousnet = (TextView)findViewById(R.id.textNervousnet);

        initButtonConnectOnClickListener(this);
        initButtonNervousnet(this);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonNervousnet.setOnClickListener(buttonNervousnetOnClickListener);

        // Get sensors data
        Nervousnet nervousnet = new Nervousnet(this);
        nervousnet.connect();
        dataSource = nervousnet;

        // Clustering
        clustering = new KMeans(1, 3);

        // Initialize state of the client
        state = new State(this);

        // Plot
        GraphView graph_view = (GraphView) findViewById(R.id.graph);
        graph = new GraphPlot(graph_view);

    }

    protected void initButtonConnectOnClickListener(Context context) {

        // Read manifest file to get server's url and port
        final String dstAddress = "server_url";
        final String dstPort = "server_port";

        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final Bundle bundle = ai.metaData;

        // Create button connector
        this.buttonConnectOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            SendStatesButtonHandler myClientTask = new SendStatesButtonHandler(
                    sendResponse,
                    bundle.getString(dstAddress),
                    bundle.getInt(dstPort),
                    state);
            myClientTask.execute();
            Log.d("ACTIVITY-BUTTON", "Connect button successfully completed!");
            }
        };
    }

    protected void initButtonNervousnet(final Context context) {
        // Create button connector
        this.buttonNervousnetOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isRunning == false) {
                    isRunning = true;
                    buttonNervousnet.setText("Stop executing ...");
                    PeriodicExecutionHandler perHandler = new PeriodicExecutionHandler(state, clustering, dataSource);
                    perHandler.start();
                }
                else{
                    isRunning = false;
                    buttonNervousnet.setText("Get nervousnet data");
                    PeriodicExecutionHandler perHandler = new PeriodicExecutionHandler();
                    perHandler.stop();
                    graph.plot(PeriodicExecutionHandler.points, PeriodicExecutionHandler.clustering.getClusters());
                }
            }
        };
    }

    public void testData(){
        // TODO: random points used
        ArrayList points = Utils.randomPoints();

        // Clustering
        Clustering clustering = new KMeans(numOfDimensions, numOfClusters);
        ArrayList<Cluster> clusters = clustering.compute(points);

        // Plot
        graph.plot(points, clusters);

        // Set possible states
        int n = clusters.size();
        double[] dClusters = new double[n*numOfDimensions];
        double[] curCoord = null;
        for (int i = 0; i < n; i++) {
            curCoord = clusters.get(i).getCentroid().getCoordinates();
            for (int dim = 0; dim < numOfDimensions; dim++)
                dClusters[i * numOfDimensions + dim] = curCoord[dim];
        }

        //TODO: state.setPossibleStates(dClusters);

        textNervousnet.setText(WriteJSON.serialize("possibleStates", state.getPossibleStates()));
    }

    public void continuousPlotting(){
        while (true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (PeriodicExecutionHandler.points != null
                    && PeriodicExecutionHandler.clustering != null){
                graph.plot(PeriodicExecutionHandler.points,
                        PeriodicExecutionHandler.clustering.getClusters());
            }
        }
    }
}