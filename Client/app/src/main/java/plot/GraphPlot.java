package plot;

import android.graphics.Color;
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
    public static void plot(ArrayList<Point> pointsInit, ArrayList<Cluster> clustersInit, GraphView point_graph) {

        // Convert data of Points into DataPoint
        DataPoint[] data = new DataPoint[pointsInit.size()];
        DataPoint[] clusters = new DataPoint[clustersInit.size()];
        for(int i = 0; i < pointsInit.size(); i++) {
            double[] coord = pointsInit.get(i).getCoordinates();
            data[i] = new DataPoint(coord[0], coord[1]);
        }
        for(int i = 0; i < clustersInit.size(); i++) {
            double[] coord = clustersInit.get(i).getCentroid().getCoordinates();
            clusters[i] = new DataPoint(coord[0], coord[1]);
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
