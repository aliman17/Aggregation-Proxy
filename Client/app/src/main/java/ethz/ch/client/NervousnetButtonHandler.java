package ethz.ch.client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

import clusteringByWindow.Cluster;
import clusteringByWindow.Clustering;
import clusteringByWindow.KMeans;
import clusteringByWindow.Point;
import json.WriteJSON;
import nervousnet.Nervousnet;
import plot.GraphPlot;
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
    GraphView graph;
    ArrayList<Point> points;
    ArrayList<Cluster> clusters;
    int dimensions;

    public NervousnetButtonHandler(Context context, TextView nervousnetText, Nervousnet nervousnet,
                                   State state, TextView sendResponse, GraphView graph, int dimensions){
        this.context = context;
        this.nervousnetText = nervousnetText;
        this.nervousnet = nervousnet;
        this.state = state;
        this.sendResponse = sendResponse;
        this.graph = graph;
        this.dimensions = dimensions;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Nervousnet
        data = nervousnet.getLightValues(50);
        // data = nervousnet.getAccelerometerValues(1368830762000L, 1468830782000L);

        // Convert into Points
        points = new ArrayList<>();
        for (double d : data){
            double[] coord = {d, d};
            points.add(new Point(coord));
        }

        // Clustering
        Clustering clustering = new KMeans(this.dimensions);
        clusters = clustering.compute(points);

        // Store
        // Set possible states
        int n = clusters.size();
        double[] dClusters = new double[n*dimensions];
        double[] curCoord = null;
        for (int i = 0; i < n; i++) {
            curCoord = clusters.get(i).getCentroid().getCoordinates();
            for (int dim = 0; dim < dimensions; dim++)
                dClusters[i * dimensions + dim] = curCoord[dim];
        }
        // Initialize state of the client
        state.setPossibleStates(dClusters);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        graph.removeAllSeries();
        GraphPlot.plot(points, clusters, graph);
        this.nervousnetText.setText(WriteJSON.serialize("possibleStates", data));
        this.sendResponse.setText("Click 'SEND' to update sever ...");
        super.onPostExecute(result);
    }

}
