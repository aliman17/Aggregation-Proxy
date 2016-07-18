package ethz.ch.client;

import android.content.Context;
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

import database.DatabaseHandler;
import json.WriteJSON;
import nervousnet.Nervousnet;
import state.State;

/**
 * Created by ales on 13/07/16.
 */
public class NervousnetButtonHandler extends AsyncTask<Void, Void, Void> {

    Context context;
    TextView nervousnetText;
    Nervousnet nervousnet;
    ArrayList<Double> data;
    State state;
    TextView sendResponse;

    public NervousnetButtonHandler(Context context, TextView nervousnetText, Nervousnet nervousnet, State state, TextView sendResponse){
        this.context = context;
        this.nervousnetText = nervousnetText;
        this.nervousnet = nervousnet;
        this.state = state;
        this.sendResponse = sendResponse;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Nervousnet
        data = nervousnet.getLightValues(1368830762000L, 1468830782000L);

        // Or databases
        //DatabaseHandler db = new DatabaseHandler(context);
        //data = db.getAllSensorValues();

        //state.setPossibleStates(data);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        this.nervousnetText.setText(WriteJSON.serialize("possibleStates", data));
        this.sendResponse.setText("Click 'SEND' to update sever ...");
        super.onPostExecute(result);
    }

}
