package ethz.ch.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
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

import java.util.ArrayList;

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
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

        // Initialize state of the client
        state = new State(this);


/*
        private void initPossibleStates(Context context){
            // Creating database and table
            DatabaseHandler db = new DatabaseHandler(context);
            sensorValues = db.getAllSensorValues();
            ArrayList<Point> points = new ArrayList<>();
            for( Double d : sensorValues ) {
                points.add(new Point(d));
            }

            Clustering clustering = new KMeans_first_example();
            clustering.compute(points);
            List<Cluster> clusters = clustering.getClusters();
            int len = clusters.size();
            possibleStates = new double[len];
            for( int i = 0; i < len; i++ ) {
                possibleStates[i] = clusters.get(i).centroid.getX();
            }
        }
        */

        /*// PLOT
        GraphView point_graph = (GraphView) findViewById(R.id.graph);

        DataPoint[] data = new DataPoint[state.sensorValues.size()];
        DataPoint[] clusters = new DataPoint[state.getPossibleStates().length];

        for(int i = 0; i < state.sensorValues.size(); i++)
            data[i] = new DataPoint(state.sensorValues.get(i), i);

        double[] possibleStates = state.getPossibleStates();
        for(int i = 0; i < state.getPossibleStates().length; i++)
            clusters[i] = new DataPoint(possibleStates[i], -5);

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
        */
        //Log.d("PossibleStates", Arrays.toString(possibleStates));


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
                NervousnetButtonHandler myClientTask = new NervousnetButtonHandler(
                        textNervousnet, nervousnet, state, sendResponse);
                myClientTask.execute();
            }
        };
    }


}