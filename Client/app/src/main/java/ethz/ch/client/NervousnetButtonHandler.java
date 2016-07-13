package ethz.ch.client;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import nervousnet.Nervousnet;

/**
 * Created by ales on 13/07/16.
 */
public class NervousnetButtonHandler extends AsyncTask<Void, Void, Void> {

    TextView nervousnetText;
    Nervousnet nervousnet;
    ArrayList<Double> data;

    public NervousnetButtonHandler(TextView nervousnetText, Nervousnet nervousnet){
        this.nervousnetText = nervousnetText;
        this.nervousnet = nervousnet;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        data = nervousnet.getLightData();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        this.nervousnetText.setText(data.toString());
        super.onPostExecute(result);
    }

}
