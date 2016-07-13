package ethz.ch.client;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import state.State;

/**
 * Created by ales on 13/07/16.
 */
public class SendStates extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    State state;
    String response;
    TextView textResponse;

    SendStates(TextView textResponse, String addr, int port, State state){
        dstAddress = addr;
        dstPort = port;
        this.state = state;
        this.textResponse = textResponse;
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
