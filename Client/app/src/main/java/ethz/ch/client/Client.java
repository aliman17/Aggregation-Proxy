package ethz.ch.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import clustering.Cluster;
import clustering.Clustering;
import clustering.KMeans_first_example;
import clustering.Point;
import nervousnet.Nervousnet;
import state.State;

public class Client extends Activity {
    String response = "";
    TextView sendResponse;
    TextView textNervousnet;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonNervousnet;

    OnClickListener buttonConnectOnClickListener;
    OnClickListener buttonNervousnetOnClickListener;

    State state;

    protected NervousnetRemote mService;
    private ServiceConnection mServiceConnection;
    private Boolean bindFlag;

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
        Clustering clustering = new KMeans_first_example();
        ArrayList<Cluster> clusters = clustering.compute(points);

        // Plot
        plot(points, clusters);

        // Set possible states
        int n = clusters.size();
        double[] dClusters = new double[n];
        for (int i = 0; i < n; i++)
            dClusters[i] = clusters.get(i).getCentroid().getX();

        // Initialize state of the client
        state = new State(this);
        state.setPossibleStates(dClusters);


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
                        textNervousnet, nervousnet, state, sendResponse);
                myClientTask.execute();
            }
        };
    }


    public void plot(ArrayList<Point> pointsInit, ArrayList<Cluster> clustersInit) {
        // PLOT
        GraphView point_graph = (GraphView) findViewById(R.id.graph);

        DataPoint[] data = new DataPoint[pointsInit.size()];
        DataPoint[] clusters = new DataPoint[clustersInit.size()];

        for(int i = 0; i < pointsInit.size(); i++)
            data[i] = new DataPoint(pointsInit.get(i).getX(), i);

        for(int i = 0; i < clustersInit.size(); i++)
            clusters[i] = new DataPoint(clustersInit.get(i).getCentroid().getX(), -5);

        PointsGraphSeries<DataPoint> point_series2 = new PointsGraphSeries<DataPoint>(clusters);

        point_graph.addSeries(point_series2);
        point_series2.setShape(PointsGraphSeries.Shape.POINT);
        point_series2.setColor(Color.RED);
        point_series2.setSize(10);

        PointsGraphSeries<DataPoint> point_series = new PointsGraphSeries<DataPoint>(data);

        point_graph.addSeries(point_series);
        point_series.setShape(PointsGraphSeries.Shape.POINT);
        point_series.setColor(Color.BLACK);
        point_series.setSize(5);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(point_graph);
        //staticLabelsFormatter.setHorizontalLabels(new String[]{"Jan", "Feb", "March", "Apr", "May", "june", "Aug", "Sept", "OCt", "Nov", "Dec"});
        point_graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        point_series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(Client.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

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