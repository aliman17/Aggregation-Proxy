package data;

import android.os.RemoteException;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import clustering.Point;
import sensor.MultiSensorPoint;
import sensor.SensorPoint;

/**
 * Created by ales on 03/08/16.
 */
public class DataSourceHelper {

    private static boolean bLight = true;
    private static boolean bBattery = false;
    private static boolean bNoise = true;

    private static final long oneDayMiliseconds = 86400000;

    public static Point getNextDataPoint(iDataSource dataSource) throws RemoteException {
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

    public static ArrayList<Point> getInitData(iDataSource dataSource) throws RemoteException {

        ArrayList<Point> points = new ArrayList<>();

        for( int i = 0; i < 50; i++) {
            Point newPoint = null;
            try {
                newPoint = getNextDataPoint(dataSource);
                points.add(newPoint);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        ArrayList<ArrayList> listOfSensorsArrays = new ArrayList<>();


        long stop = System.currentTimeMillis();
        long start = stop - oneDayMiliseconds;

        if (bLight) {
            ArrayList<SensorPoint> arr = dataSource.getLightValues( start, stop );
            listOfSensorsArrays.add( arr );

            for (SensorPoint point : arr) {
                Log.d("TIMESTAMP-LIGHT", "" + point.getTimestamp());
            }

        }

        if (bNoise) {
            ArrayList<SensorPoint> arr = dataSource.getNoiseValues( start, stop );
            listOfSensorsArrays.add( arr );

            for (SensorPoint point : arr) {
                Log.d("TIMESTAMP-NOISE", "" + point.getTimestamp());
            }
        }

        if (bBattery) {
            ArrayList<SensorPoint> arr = dataSource.getBatteryValues( start, stop );
            listOfSensorsArrays.add( arr );

            for (SensorPoint point : arr) {
                Log.d("TIMESTAMP-BATTERY", "" + point.getTimestamp());
            }
        }




        return points;
    }
}
