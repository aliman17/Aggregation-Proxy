package ethz.ch.client;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import clusteringByWindow.Cluster;
import clusteringByWindow.Clustering;
import clusteringByWindow.KMeans;
import clusteringByWindow.Point;
import json.WriteJSON;
import nervousnet.Nervousnet;
import periodic.PeriodicExecution;
import plot.GraphPlot;
import state.State;

/**
 * Created by ales on 13/07/16.
 */
public class NervousnetButtonHandler {

    Context context;
    TextView nervousnetText;
    Nervousnet nervousnet;
    //ArrayList<Double> data;
    State state;
    TextView sendResponse;
    GraphView graph;
    ArrayList<Point> points;
    ArrayList<Cluster> clusters;
    int dimensions;
    int numOfClusters;
    Button buttonNervousnet;
    Clustering clustering;

    static boolean isRunning = false;
    static PeriodicExecution thread = null;

    public NervousnetButtonHandler(Context context, TextView nervousnetText, Nervousnet nervousnet,
                                   State state, TextView sendResponse, GraphView graph, int dimensions,
                                   int numOfClusters, Button buttonNervousnet){
        this.context = context;
        this.nervousnetText = nervousnetText;
        this.nervousnet = nervousnet;
        this.state = state;
        this.sendResponse = sendResponse;
        this.graph = graph;
        this.dimensions = dimensions;
        this.numOfClusters = numOfClusters;
        this.buttonNervousnet = buttonNervousnet;
    }

    private void initPoints(){
        Log.d("BUTTON", "Initialize points ...");
        points = new ArrayList<>();
        //for(Double d : data){
        //double[] coordinates = {d.doubleValue(), 1};
        for(int i = 0; i < 30; i++)   { ;
            Random rand = new Random();
            double[] coordinates = {rand.nextDouble(), i+1};

            points.add(new Point(coordinates));
        }
    }

    private void computeClusters(){
        Log.d("BUTTON", "Compute clusters ...");
        // Cluster initially
        int dim = 2;
        int nOfClusters = 5;
        clustering = new KMeans(dim, nOfClusters);
        clusters = clustering.compute(points);
        finish();
    }


    protected void execute() {
        Log.d("BUTTON", "Nervousnet button pressed and is under execution ...");
        if (!isRunning) {
            initPoints();
            computeClusters();

            isRunning = true;
            thread = new PeriodicExecution(points, clustering, nervousnet);
            buttonNervousnet.setText("Stop nervousnet ...");
            thread.start();
        }
        else {
            isRunning = false;
            buttonNervousnet.setText("Stopping ...");
            if (thread != null) {
                thread.stopExecution();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            buttonNervousnet.setText("Get nervousnet data");
        }

    }

    protected void finish() {
        Log.d("PLOTTING", "Start plotting ...");
        graph.removeAllSeries();
        try {
            GraphPlot.plot(points, clusters, graph);
        } catch (Exception e) {

        }
        this.nervousnetText.setText(WriteJSON.serialize("possibleStates", clusters));
        this.sendResponse.setText("Click 'SEND' to update sever ...");
        Log.d("PLOTTING", "Plotting completed!");
    }


    /*private void run(){
        // Nervousnet
        // data = nervousnet.getAccelerometerValues(1368830762000L, 1468830782000L);

        // Convert into Points
        points = new ArrayList<>();
        for (double d : data){
            double[] coord = {d, d};
            points.add(new Point(coord));
        }

        // Clustering
        Clustering clustering = new KMeans(this.dimensions, this.numOfClusters);
        clustering.compute(points);

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
    }
    */
}
