package ethz.ch.client;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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
import periodic.PeriodicExecutionHandler;
import plot.GraphPlot;
import state.State;

/**
 * Created by ales on 13/07/16.
 */
public class NervousnetButtonHandler {

    Context context;
    TextView textNervousnet;
    Nervousnet nervousnet;
    //ArrayList<Double> data;
    State state;
    TextView sendResponse;
    GraphPlot graph;
    ArrayList<Point> points;
    ArrayList<Cluster> clusters;
    int dimensions;
    int numOfClusters;
    Button buttonNervousnet;
    Clustering clustering;

    static boolean isRunning = false;
    static PeriodicExecution thread = null;

    public NervousnetButtonHandler(Context context, Nervousnet nervousnet,
                                   State state, GraphPlot graph, int dimensions,
                                   int numOfClusters){
        this.context = context;
        this.nervousnet = nervousnet;
        this.state = state;
        this.graph = graph;
        this.dimensions = dimensions;
        this.numOfClusters = numOfClusters;
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
