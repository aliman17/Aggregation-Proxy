package ethz.ch.client;

import android.content.pm.ActivityInfo;
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

import clustering.KMeans;
import clustering.Clustering;
import data.iDataSource;
import data.Nervousnet;
import database.VirtualSensorDB;
import periodic.PeriodicExecution;
import plot.GraphPlot;
import state.State;

public class Client extends Activity {

    State state;
    iDataSource dataSource;
    Clustering clustering;
    GraphPlot graph;
    int numOfClusters = 3;
    int numOfDimensions = 10;
    TextView sendResponse, textNervousnet;
    Button buttonConnect, buttonNervousnet;
    OnClickListener buttonConnectOnClickListener;
    OnClickListener buttonNervousnetOnClickListener;

    PeriodicExecution periodic;

    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't allow rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        // Create front-end view
        setContentView(R.layout.activity_client);

        VirtualSensorDB.test(this);


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
        Log.d("Activity", "Init nervousnet ...");
        Nervousnet nervousnet = new Nervousnet(this);
        nervousnet.connect();
        dataSource = nervousnet;

        // Clustering
        Log.d("Activity", "Init clustering ...");
        clustering = new KMeans(numOfDimensions, numOfClusters);

        // Initialize state of the client
        Log.d("Activity", "Init state ...");
        state = new State(this);

        // Plot
        Log.d("Activity", "Init plot ...");
        GraphView graph_view = (GraphView) findViewById(R.id.graph);
        //graph = new GraphPlot(graph_view);

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
                Log.d("CLICK-BUTTON", "Start");
                isRunning = true;
                buttonNervousnet.setText("Stop executing ...");
                periodic = new PeriodicExecution(state, clustering, dataSource);
                periodic.start();  // new thread
            }
            else{
                isRunning = false;
                buttonNervousnet.setText("Get nervousnet data");
                periodic.stopExecution();
                try {
                    periodic.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                periodic = null;
                //graph.plot(periodic.getVirtualPoints(), periodic.getClustering().getClusters());
            }
            }
        };
    }
}