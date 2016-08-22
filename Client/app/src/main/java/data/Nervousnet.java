package data;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;


import java.util.ArrayList;
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

    // We need context to get connections and sensor data
    private Context context;

    // Connection to the service
    NervousnetServiceController nervousnetServiceController;

    // Reading
    SensorReading lReading;

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

    public SensorPoint getLatestAccValue(){
        AccelerometerReading lReading = null;
        try {
            lReading  = (AccelerometerReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_ACCELEROMETER);
            long timestamp = lReading.timestamp;
            double[] values = {lReading.getX(), lReading.getY(), lReading.getZ()};
            int type = lReading.type;
            Log.d("NERVOUSNET", "Getting acc value ..." + values[0] + " " + values[1] + " " + values[2]);
            return new SensorPoint(type, timestamp, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SensorPoint> getAccValues(long startTime, long stopTime) {


        return null;
    }


    public SensorPoint getLatestBatteryValue(){
        BatteryReading lReading = null;
        try {
            lReading  = (BatteryReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_BATTERY);
            long timestamp = lReading.timestamp;
            double[] values = {lReading.getPercent()};
            int type = lReading.type;
            Log.d("NERVOUSNET", "Getting battery value ... " + values[0]);
            return new SensorPoint(type, timestamp, values);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SensorPoint> getBatteryValues(long startTime, long stopTime) {
        // TODO: get real data
        ArrayList<SensorPoint> values = new ArrayList<>();
        for(int i = 0; i < 50; i++)
            values.add(getLatestBatteryValue());
        return values;
    }


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
    }



    public SensorPoint getLatestLightValue() throws RemoteException {
        SensorReading reading = nervousnetServiceController.getLatestReading(LibConstants.SENSOR_LIGHT);

        if (reading instanceof LightReading){
            LightReading lReading = (LightReading) reading;
            long timestamp = lReading.timestamp;
            double[] values = {lReading.getLuxValue()};
            int type = lReading.type;
            Log.d("NERVOUSNET", "Getting light value ... " + values[0]);
            return new SensorPoint(type, timestamp, values);
        } else {
            throw new RemoteException();
        }
    }

    @Override
    public ArrayList<SensorPoint> getLightValues(long startTime, long stopTime) throws RemoteException {
        // TODO: get real data
        ArrayList<SensorPoint> values = new ArrayList<>();
        for(int i = 0; i < 50; i++)
            values.add(getLatestBatteryValue());

        Callback cb = new Callback();
        ArrayList<SensorReading> readings = new ArrayList<>();
        cb.success(readings);
        nervousnetServiceController.getReadings(LibConstants.SENSOR_LIGHT,
                startTime,
                stopTime,
                cb);

        Log.d("READINGS", ""+readings.size());
        return values;
    }



    public SensorPoint getLatestNoiseValue(){
        NoiseReading lReading;

        try{
            lReading  = (NoiseReading) nervousnetServiceController.getLatestReading(LibConstants.SENSOR_NOISE);
            long timestamp = lReading.timestamp;
            double[] values = {lReading.getdbValue()};
            int type = lReading.type;
            Log.d("NERVOUSNET", "Getting noise value ... " + values[0]);
            return new SensorPoint(type, timestamp, values);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void onSensorDataReady(SensorReading reading) {
        this.lReading = reading;
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


    class Callback extends RemoteCallback.Stub {

        @Override
        public void success(final List<SensorReading> list) throws RemoteException {


                    Log.d("LightmeterActivity", "callback success "+list.size());
//                                successImpl(result);


                        Iterator<SensorReading> iterator;
                        iterator = list.iterator();
                        while (iterator.hasNext()) {
                            SensorReading reading = iterator.next();

                            Log.d("LightmeterActivity", "Light Reading found - " + ((LightReading) reading).getLuxValue());
                        }


        }

        @Override
        public void failure(final ErrorReading reading) throws RemoteException {

                    Log.d("LightmeterActivity", "callback failure "+reading.getErrorString());

        }
//
//        @Override
//        public IBinder asBinder() {
//            return super.asBinder();
//        }
    }

}
