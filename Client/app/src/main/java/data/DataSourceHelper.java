package data;

import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;


import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.GyroReading;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.NoiseReading;
import ch.ethz.coss.nervousnet.lib.ProximityReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import sensor.VirtualSensorPoint;

/**
 * Created by ales on 03/08/16.
 */
public class DataSourceHelper {

    private static boolean bLight = true;
    private static boolean bBattery = true;
    private static boolean bNoise = true;

    private static final long oneDayMiliseconds = 86400000;
    private static final long initWindowSizeMiliseconds = 10000;

    public static VirtualSensorPoint getNextVirtualSensorPoint(iDataSource dataSource) throws RemoteException {

        VirtualSensorPoint virtualPoint = new VirtualSensorPoint();

        if (bLight) {
            LightReading light = dataSource.getLatestLightValue();
            virtualPoint.setLight( light.getLuxValue() );
        }

        if (bNoise) {
            NoiseReading noise = dataSource.getLatestNoiseValue();
            virtualPoint.setNoise( noise.getdbValue() );
        }

        if (bBattery) {
            BatteryReading battery = dataSource.getLatestBatteryValue();
            virtualPoint.setBattery( battery.getPercent() );
        }

        virtualPoint.finishSetting();
        return virtualPoint;
    }

    public static ArrayList<VirtualSensorPoint> getInitData(iDataSource dataSource) throws RemoteException {

        long stop = System.currentTimeMillis();
        long start = stop - oneDayMiliseconds;

        ArrayList<ArrayList<SensorReading>> arr = new ArrayList<>();

        if (bLight) {
            ArrayList<SensorReading> light = dataSource.getLightValues( start, stop );
            if (light != null) {
                arr.add(light);

                for (SensorReading point : light) {
                    Log.d("TIMESTAMP-LIGHT", "" + point.timestamp + " " + ((LightReading) point).getLuxValue());
                }
            }
            else {

            }
        }

        if (bNoise) {
            ArrayList<SensorReading> noise = dataSource.getNoiseValues( start, stop );
            if (noise != null) {
                arr.add(noise);
                //Log.d("TIMESTAMP-NOISE", "Size: " + arr.size());
                for (SensorReading point : noise) {
                    Log.d("TIMESTAMP-NOISE", "" + point.timestamp + " " + ((NoiseReading) point).getdbValue());
                }
            }
            else{

            }
        }

        if (bBattery) {
            ArrayList<SensorReading> battery = dataSource.getBatteryValues( start, stop );
            if (battery != null) {
                arr.add(battery);
                //Log.d("TIMESTAMP-BATTERY", "Size: " + arr.size());
                for (SensorReading point : battery) {
                    Log.d("TIMESTAMP-BATTERY", "" + point.timestamp + " " + ((BatteryReading) point).getPercent());
                }
            }
            else {

            }
        }

        // COMBINE
        ArrayList<VirtualSensorPoint> vsparr = combine(arr);

        return vsparr;
    }

    private static ArrayList<VirtualSensorPoint> combine(ArrayList<ArrayList<SensorReading>> listOfSensorsReadings){
        // The hash contains all sensors that are required for combination

        long startTimestamp = Long.MIN_VALUE;
        long stopTimestamp = Long.MIN_VALUE;

        for( ArrayList<SensorReading> arr : listOfSensorsReadings ){
            long tmpStart = arr.get(0).timestamp;
            if (tmpStart > startTimestamp)
                startTimestamp = tmpStart;
            long tmpStop = arr.get(arr.size() - 1).timestamp;
            if (tmpStop > stopTimestamp)
                stopTimestamp = tmpStop;
        }

        // Let's take interval as 1s:
        long start = startTimestamp;
        long step = initWindowSizeMiliseconds;

        // Initialize pointes which will run through all arrays
        int[] pointers = new int[listOfSensorsReadings.size()];
        for( int i = 0; i < listOfSensorsReadings.size(); i++ ){
            pointers[i++] = -1;
        }

        ArrayList<VirtualSensorPoint> vsparr = new ArrayList<>();

        while ( start <= stopTimestamp ) {

            for (int i = 0; i < pointers.length; i++) {
                ArrayList<SensorReading> readings = listOfSensorsReadings.get(i);
                int sizeI = readings.size();
                while (pointers[i]+1 < sizeI && readings.get(pointers[i]+1).timestamp <= start) {
                    pointers[i]++;
                }
            }

            VirtualSensorPoint vp = new VirtualSensorPoint();
            // Fill the VirtualSensor
            for (int i = 0; i < pointers.length; i++) {
                SensorReading reading = listOfSensorsReadings.get(i).get(pointers[i]);
                if (reading instanceof NoiseReading) {
                    vp.setNoise(((NoiseReading) reading).getdbValue());
                } else if (reading instanceof LightReading) {
                    vp.setLight(((LightReading) reading).getLuxValue());
                } else if (reading instanceof AccelerometerReading) {
                    vp.setAccelerometer(((AccelerometerReading) reading).getX(),
                            ((AccelerometerReading) reading).getY(),
                            ((AccelerometerReading) reading).getZ());
                } else if (reading instanceof GyroReading) {
                    vp.setGyrometer(((GyroReading) reading).getGyroX(),
                            ((GyroReading) reading).getGyroY(),
                            ((GyroReading) reading).getGyroZ());
                } else if (reading instanceof ProximityReading) {
                    vp.setProximity(((ProximityReading) reading).getProximity());
                }
            }
            vp.finishSetting();
            vsparr.add(vp);
            start += step;
        }
        return vsparr;
    }
}
