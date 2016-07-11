package ethz.ch.client;

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

import state.State;

public class Client extends Activity {
    String response = "";
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    OnClickListener buttonConnectOnClickListener;

    State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create front-end view
        setContentView(R.layout.activity_client);

        // Initialize state of the client
        state = new State(this);

        // Store element on the view in arguments
        buttonConnect = (Button)findViewById(R.id.connect);
        textResponse = (TextView)findViewById(R.id.response);

        // Create button handler for Connect
        initButtonConnectOnClickListener(this);
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
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
                MyClientTask myClientTask = new MyClientTask(
                        bundle.getString(dstAddress),
                        bundle.getInt(dstPort));
                myClientTask.execute();
            }
        };
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;

        MyClientTask(String addr, int port){
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