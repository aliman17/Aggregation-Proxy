package ethz.ch.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import clustering.Cluster;
import clustering.Clustering;
import clustering.KMeans_first_example;
import clustering.Point;
import database.DatabaseHandler;
import nervousnet.Nervousnet;
import state.State;

public class Client extends Activity {
    String response = "";
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    OnClickListener buttonConnectOnClickListener;

    State state;

    protected NervousnetRemote mService;
    private ServiceConnection mServiceConnection;
    private Boolean bindFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create front-end view
        setContentView(R.layout.activity_client);

        // Store element on the view in arguments
        buttonConnect = (Button)findViewById(R.id.connect);
        textResponse = (TextView)findViewById(R.id.response);

        // Create button handler for Connect
        initButtonConnectOnClickListener(this);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        // Get sensors data
        Nervousnet nervousnet = new Nervousnet(this);


        // Initialize state of the client
        //state = new State(this);


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
                SendStates myClientTask = new SendStates(
                        bundle.getString(dstAddress),
                        bundle.getInt(dstPort));
                myClientTask.execute();
            }
        };
    }

    public class SendStates extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;

        SendStates(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                // Create new socket
                socket = new Socket(dstAddress, dstPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send possible states
                String psm = state.getPossibleStatesMessage();
                out.println(psm);

                // Send selected state
                String ssm = state.getSelectedStateMessage();
                out.println(ssm);

                response = "Sent";
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "Unknown host";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException";
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(response);
            super.onPostExecute(result);
        }

    }



}