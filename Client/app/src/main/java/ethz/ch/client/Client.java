package ethz.ch.client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import java.util.ArrayList;
import clusteringByWindow.Cluster;
import clusteringByWindow.Clustering;
import clusteringByWindow.KMeans;
import json.WriteJSON;
import nervousnet.Nervousnet;
import plot.GraphPlot;
import state.State;

public class Client extends Activity {

    TextView sendResponse;
    TextView textNervousnet;
    Button buttonConnect, buttonNervousnet;
    GraphView point_graph;
    int dimensions = 2;

    OnClickListener buttonConnectOnClickListener;
    OnClickListener buttonNervousnetOnClickListener;

    State state;

    private Nervousnet nervousnet;

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
        nervousnet = new Nervousnet(this);
        nervousnet.connect();

        // TODO: random points used
        ArrayList points = Utils.randomPoints();

        // Clustering
        Clustering clustering = new KMeans(dimensions);
        ArrayList<Cluster> clusters = clustering.compute(points);

        // Plot
        point_graph = (GraphView) findViewById(R.id.graph);
        GraphPlot.plot(points, clusters, point_graph);

        // Set possible states
        int n = clusters.size();
        double[] dClusters = new double[n*dimensions];
        double[] curCoord = null;
        for (int i = 0; i < n; i++) {
            curCoord = clusters.get(i).getCentroid().getCoordinates();
            for (int dim = 0; dim < dimensions; dim++)
                dClusters[i * dimensions + dim] = curCoord[dim];
        }
        // Initialize state of the client
        state = new State(this);
        state.setPossibleStates(dClusters);

        textNervousnet.setText(WriteJSON.serialize("possibleStates", state.getPossibleStates()));
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
            }
        };
    }

    protected void initButtonNervousnet(final Context context) {
        // Create button connector
        this.buttonNervousnetOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendResponse.setText("Collecting data ...");
                NervousnetButtonHandler myClientTask = new NervousnetButtonHandler(context,
                        textNervousnet, nervousnet, state, sendResponse, point_graph, dimensions);
                myClientTask.execute();
            }
        };
    }
}