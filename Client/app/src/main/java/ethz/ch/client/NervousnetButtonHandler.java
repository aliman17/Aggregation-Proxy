package ethz.ch.client;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import nervousnet.Nervousnet;
import state.State;

/**
 * Created by ales on 13/07/16.
 */
public class NervousnetButtonHandler extends AsyncTask<Void, Void, Void> {

    TextView nervousnetText;
    Nervousnet nervousnet;
    double[] data;
    State state;
    TextView sendResponse;

    public NervousnetButtonHandler(TextView nervousnetText, Nervousnet nervousnet, State state, TextView sendResponse){
        this.nervousnetText = nervousnetText;
        this.nervousnet = nervousnet;
        this.state = state;
        this.sendResponse = sendResponse;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        data = nervousnet.getLightValues(1398700136002L, 1468492917000L);
        Log.d("Nervousnet", "Number of data from database "+data.length);
        state.setPossibleStates(data);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        this.nervousnetText.setText(Arrays.toString(data));
        this.sendResponse.setText("Click 'SEND' to update sever ...");
        super.onPostExecute(result);
    }

}
