package ethz.ch.client;

import android.graphics.Color;
import android.os.Bundle;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Random;

import clustering.Cluster;
import clustering.Clustering;
import clustering.KMeans;
import clustering.Point;
import json.WriteJSON;
import nervousnet.Nervousnet;
import plot.GraphPlot;
import state.State;

public class Client extends Activity {

    TextView sendResponse;
    TextView textNervousnet;
    Button buttonConnect, buttonNervousnet;
    GraphView point_graph;

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
        ArrayList points = randomPoints();

        // Clustering
        Clustering clustering = new KMeans();
        ArrayList<Cluster> clusters = clustering.compute(points);

        // Plot
        point_graph = (GraphView) findViewById(R.id.graph);
        GraphPlot.plot(points, clusters, point_graph);

        // Set possible states
        int n = clusters.size();
        double[] dClusters = new double[n];
        for (int i = 0; i < n; i++)
            dClusters[i] = clusters.get(i).getCentroid().getX();

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
                        textNervousnet, nervousnet, state, sendResponse, point_graph);
                myClientTask.execute();
            }
        };
    }


    public static ArrayList<Point> randomPoints(){
        // Test data
        ArrayList<Point> points = new ArrayList<>();

        // Create some random values
        Random rand = new Random();
        for (int i = 0; i < 10; i++){
            points.add(new Point(rand.nextDouble()* 10));
        }

        for (int i = 0; i < 10; i++){
            points.add(new Point(rand.nextDouble()* 10 + 15));
        }

        for (int i = 0; i < 10; i++){
            points.add(new Point(rand.nextDouble()* 10 + 20));
        }
        return points;
    }
}