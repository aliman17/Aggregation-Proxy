package plot;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;

import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

import clusteringByWindow.Cluster;
import clusteringByWindow.Point;
import ethz.ch.client.Client;

/**
 * Created by ales on 19/07/16.
 */
public class GraphPlot {
    /**
     * The function takes points and clusters and plots them. It considers only first two coordinates
     * which means only 2 dimensions as we can easily plot in 2D plane. If the dimension is 1, then
     * the function automatically sets the second coordinate to a constant value 1 and plots it.
     * @param pointsInit    points
     * @param clustersInit  clusters
     * @param point_graph   graph object for plotting
     */
    public static void plot(ArrayList<Point> pointsInit, ArrayList<Cluster> clustersInit, GraphView point_graph) {

        // Convert data of Points into DataPoint
        DataPoint[] data = new DataPoint[pointsInit.size()];
        DataPoint[] clusters = new DataPoint[clustersInit.size()];
        for(int i = 0; i < pointsInit.size(); i++) {
            double[] coord = pointsInit.get(i).getCoordinates();
            if (coord.length >= 2)
                data[i] = new DataPoint(coord[0], coord[1]);
            else if (coord.length == 1)
                data[i] = new DataPoint(coord[0], 1);
            else {
                // ignore this point as it has wrong number of coordinates
                Log.d("GraphPlot", "Wrong coordinate type, so the point hasn't been plotted");
            }
        }
        for(int i = 0; i < clustersInit.size(); i++) {
            double[] coord = clustersInit.get(i).getCentroid().getCoordinates();
            if (coord.length >= 2)
                clusters[i] = new DataPoint(coord[0], coord[1]);
            else if (coord.length == 1)
                clusters[i] = new DataPoint(coord[0], 1);
            else {
                // ignore this cluster as it has wrong number of coordinates
                Log.d("GraphPlot", "Wrong coordinate type, so the cluster hasn't been plotted");
            }
        }

        // Plot Points
        PointsGraphSeries<DataPoint> point_series = new PointsGraphSeries<DataPoint>(data);
        point_graph.addSeries(point_series);
        point_series.setShape(PointsGraphSeries.Shape.POINT);
        point_series.setColor(Color.BLACK);
        point_series.setSize(5);

        // Plot clusters
        PointsGraphSeries<DataPoint> point_series2 = new PointsGraphSeries<DataPoint>(clusters);
        point_graph.addSeries(point_series2);
        point_series2.setShape(PointsGraphSeries.Shape.POINT);
        point_series2.setColor(Color.RED);
        point_series2.setSize(10);

        // Additional format
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(point_graph);
        point_graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

    }
}
