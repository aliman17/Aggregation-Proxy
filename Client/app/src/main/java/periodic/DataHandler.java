package periodic;

import java.util.ArrayList;

import ch.ethz.coss.nervousnet.lib.LibConstants;
import clusteringByWindow.Point;
import sensor.MultiSensorPoint;
import sensor.SensorPoint;
import sensor.iSensorSource;

/**
 * Created by ales on 03/08/16.
 */
public class DataHandler {

    private static boolean bLight = true;
    private static boolean bBattery = true;
    private static boolean bNoise = true;

    public static Point getNextDataPoint(iSensorSource dataSource) {
        ArrayList<SensorPoint> listOfSensors = new ArrayList<>();

        // The ORDER is important. Only first two dimensions will be plotted.

        // Get data from all sensors you want and create multidimensional point
        if (bLight) {
            SensorPoint lightValues = dataSource.getLatestLightValue();
            listOfSensors.add(lightValues);
        }

        if (bNoise) {
            SensorPoint noiseValues = dataSource.getLatestNoiseValue();
            listOfSensors.add(noiseValues);
        }

        if (bBattery) {
            SensorPoint batteryValues = dataSource.getLatestBatteryValue();
            listOfSensors.add(batteryValues);
        }

        MultiSensorPoint ms = new MultiSensorPoint(listOfSensors);

        return new Point(ms.getValues(), ms);
    }

    public static ArrayList<Point> getInitData(iSensorSource dataSource) {

        ArrayList<Point> points = new ArrayList<>();

        for( int i = 0; i < 50; i++) {
            Point newPoint = getNextDataPoint(dataSource);
            points.add(newPoint);
        }
        return points;
    }
}
