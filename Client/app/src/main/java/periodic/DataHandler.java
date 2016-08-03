package periodic;

import java.util.ArrayList;

import clusteringByWindow.Point;
import sensor.MultiSensorPoint;
import sensor.SensorPoint;
import sensor.iSensorSource;

/**
 * Created by ales on 03/08/16.
 */
public class DataHandler {

    public static Point getNextDataPoint(iSensorSource dataSource) {
        ArrayList<SensorPoint> listOfSensors = new ArrayList<>();

        // Get data from all sensors you want and create multidimensional point
        SensorPoint light = dataSource.getLatestLightValue();
        listOfSensors.add(light);
        MultiSensorPoint ms = new MultiSensorPoint(listOfSensors);

        return new Point(ms.getValues(), ms);
    }

    public static ArrayList<Point> getInitData(iSensorSource dataSource) {
        // Get data
        ArrayList<SensorPoint> sensors = dataSource.getLightValues(1470111256, 1470211256);

        // Combine somehow into multidimensional points and create points
        // TODO: for now we use only light
        
        ArrayList<Point> points = new ArrayList<>();
        for (SensorPoint s : sensors){
            Point p = new Point(s.getValues(), s);
            points.add(p);
        }

        return points;
    }
}
