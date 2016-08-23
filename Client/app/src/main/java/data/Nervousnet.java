package data;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.ErrorReading;
import ch.ethz.coss.nervousnet.lib.GyroReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.NervousnetSensorDataListener;
import ch.ethz.coss.nervousnet.lib.NervousnetServiceConnectionListener;
import ch.ethz.coss.nervousnet.lib.NervousnetServiceController;
import ch.ethz.coss.nervousnet.lib.NoiseReading;
import ch.ethz.coss.nervousnet.lib.RemoteCallback;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import sensor.SensorPoint;

/**
 * Created by ales on 28/06/16.
 *
 * Nervousnet queries the data in Nervousnet application. This class is source of the data.
 */
public class Nervousnet implements iDataSource, NervousnetServiceConnectionListener, NervousnetSensorDataListener {

    private enum SensorType{
        ACC,
        BATTERY,
        GYRO,
        LIGHT,
        LOC,
        NOISE
    }

    // We need context to get connections and sensor data
    private Context context;

    // Connection to the service
    NervousnetServiceController nervousnetServiceController;

    // Constructor
    public Nervousnet(Context context){
        this.context = context;
    }

    // Connect
    public void connect(){
        Log.d("NERVOUSNET", "Connecting ...");
        nervousnetServiceController = new NervousnetServiceController(this.context, this);
        Log.d("NERVOUSNET", "Connecting2 ..." + nervousnetServiceController);
        try {
            nervousnetServiceController.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////////////////////////////
    // GET LATEST DATA
    /////////////////////////////////////////////////////////////////////////

    public SensorPoint getLatestAccValue() throws RemoteException {
        AccelerometerReading lReading  = (AccelerometerReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_ACCELEROMETER);
        return generateSensorPointAcc(lReading);
    }


    public SensorPoint getLatestBatteryValue() throws RemoteException {
        BatteryReading lReading  = (BatteryReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_BATTERY);
        return generateSensorPointBattery(lReading);
    }


    public SensorPoint getLatestLightValue() throws RemoteException {
        LightReading reading = (LightReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_LIGHT);
        return generateSensorPointLight(reading);
    }


    public SensorPoint getLatestNoiseValue() throws RemoteException {
        NoiseReading lReading  = (NoiseReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_NOISE);
        return generateSensorPointNoise(lReading);
    }


    /////////////////////////////////////////////////////////////////////////
    // GET DATA FROM THE GIVEN RANGE
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ArrayList<SensorPoint> getAccValues(long startTime, long stopTime) throws RemoteException {
        ArrayList<SensorPoint> values = new ArrayList<>();
        Callback cb = new Callback(SensorType.ACC, values);
        nervousnetServiceController.getReadings(LibConstants.SENSOR_ACCELEROMETER, startTime, stopTime, cb);
        return values;
    }

    @Override
    public ArrayList<SensorPoint> getBatteryValues(long startTime, long stopTime) throws RemoteException {
        ArrayList<SensorPoint> values = new ArrayList<>();
        Callback cb = new Callback(SensorType.BATTERY, values);
        nervousnetServiceController.getReadings(LibConstants.SENSOR_BATTERY, startTime, stopTime, cb);
        return values;
    }

    @Override
    public ArrayList<SensorPoint> getLightValues(long startTime, long stopTime) throws RemoteException {
        ArrayList<SensorPoint> values = new ArrayList<>();
        Callback cb = new Callback(SensorType.LIGHT, values);
        nervousnetServiceController.getReadings(LibConstants.SENSOR_LIGHT, startTime, stopTime, cb);
        return values;
    }



    /////////////////////////////////////////////////////////////////////////
    // GENERATORS OF SENSOR POINT
    /////////////////////////////////////////////////////////////////////////

    public SensorPoint generateSensorPointAcc(AccelerometerReading reading) {
        double[] coordinates = {reading.getX(), reading.getY(), reading.getZ()};
        SensorPoint sensorpoint = new SensorPoint(
                reading.type,                           // type
                reading.timestamp,                      // timestamp
                coordinates                             // coordinates
        );
        return sensorpoint;
    }


    public SensorPoint generateSensorPointBattery(BatteryReading reading){
        double[] coordinates = {reading.getPercent()};
        SensorPoint sensorpoint = new SensorPoint(
                reading.type,                           // type
                reading.timestamp,                      // timestamp
                coordinates                             // coordinates
        );
        return sensorpoint;
    }


    public SensorPoint generateSensorPointLight(LightReading reading){
        double[] coordinates = {reading.getLuxValue()};
        SensorPoint sensorpoint = new SensorPoint(
                reading.type,                           // type
                reading.timestamp,                      // timestamp
                coordinates                             // coordinates
        );
        return sensorpoint;
    }


    public SensorPoint generateSensorPointNoise(NoiseReading reading){
        double[] coordinates = {reading.getdbValue()};
        SensorPoint sensorpoint = new SensorPoint(
                reading.type,                           // type
                reading.timestamp,                      // timestamp
                coordinates                             // coordinates
        );
        return sensorpoint;
    }


    /////////////////////////////////////////////////////////////////////////
    // OVERWRITE
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void onSensorDataReady(SensorReading reading) {

    }

    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void onServiceConnectionFailed(ErrorReading errorReading) {

    }

    //@Override
    public void onServiceConnectionFailed() {

    }


    /////////////////////////////////////////////////////////////////////////
    // CALLBACK
    /////////////////////////////////////////////////////////////////////////

    class Callback extends RemoteCallback.Stub {
        private SensorType sType;
        private List listToFill;

        public Callback(SensorType sType, List listToFill){
            this.sType = sType;
            this.listToFill = listToFill;
        }

        @Override
        public void success(final List<SensorReading> list) throws RemoteException {

            Log.d("NERVOUSNET CALLBACK", sType + " callback success " + list.size());

            Iterator<SensorReading> iterator;
            iterator = list.iterator();

            while (iterator.hasNext()) {
                SensorReading sReading = iterator.next();
                SensorPoint sensorpoint = null;
                switch (sType) {
                    case ACC:
                        // TODO
                        break;
                    case BATTERY:
                        BatteryReading bReading = (BatteryReading) sReading;
                        sensorpoint = generateSensorPointBattery(bReading);
                        break;
                    case GYRO:
                        // TODO
                        break;
                    case LIGHT:
                        LightReading lReading = (LightReading) sReading;
                        sensorpoint = generateSensorPointLight(lReading);
                        break;
                    case LOC:
                        // TODO
                        break;
                    case NOISE:
                        NoiseReading nReading = (NoiseReading) sReading;
                        sensorpoint = generateSensorPointNoise(nReading);
                        break;
                }
                Log.d("NERVOUSNET CALLBACK", "sensor type:" + sensorpoint.getSensorType() + " coordinates:" + Arrays.toString(sensorpoint.getValues()));
                listToFill.add(sensorpoint);
            }
        }

        @Override
        public void failure(final ErrorReading reading) throws RemoteException {
            Log.d("NERVOUSNET CALLBACK", "callback failure "+reading.getErrorString());
        }

    }




/*

    public SensorPoint getLatestGyroValue(){
        GyroReading lReading = null;
        try {
            lReading  = (GyroReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_GYROSCOPE);
            long timestamp = lReading.timestamp;
            double[] values = {lReading.getGyroX(), lReading.getGyroY(), lReading.getGyroZ()};
            int type = lReading.type;
            Log.d("NERVOUSNET", "Getting battery value ... " + values[0]);
            return new SensorPoint(type, timestamp, values);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public SensorPoint getLatestLocValue(){
        // TODO
        return null;
    }*/
}
