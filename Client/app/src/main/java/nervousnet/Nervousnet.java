package nervousnet;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.ErrorReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.NervousnetSensorDataListener;
import ch.ethz.coss.nervousnet.lib.NervousnetServiceConnectionListener;
import ch.ethz.coss.nervousnet.lib.NervousnetServiceController;
import ch.ethz.coss.nervousnet.lib.NoiseReading;
import ch.ethz.coss.nervousnet.lib.RemoteCallback;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import sensor.SensorPoint;
import sensor.iSensorSource;

/**
 * Created by ales on 28/06/16.
 */
public class Nervousnet implements iSensorSource, NervousnetServiceConnectionListener, NervousnetSensorDataListener {

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
        nervousnetServiceController = new NervousnetServiceController(this.context, this);
        nervousnetServiceController.connect();
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


    public SensorPoint getLatestLightValue(){
        LightReading lReading;

        try{
            lReading  = (LightReading)nervousnetServiceController.getLatestReading(LibConstants.SENSOR_LIGHT);
            long timestamp = lReading.timestamp;
            double[] values = {lReading.getLuxValue()};
            int type = lReading.type;
            Log.d("NERVOUSNET", "Getting light value ... " + values[0]);
            return new SensorPoint(type, timestamp, values);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<SensorPoint> getLightValues(long startTime, long stopTime) {
        // TODO: get real data
        ArrayList<SensorPoint> values = new ArrayList<>();
        for(int i = 0; i < 50; i++)
            values.add(getLatestLightValue());
        return values;
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

        final RemoteCallback.Stub cbBinder = new RemoteCallback.Stub() {

            @Override
            public void success(List list) throws RemoteException {
                        SensorReading lReading = null;
                Log.d("LightmeterActivity", "GREAT1!!!");
                if(list.size() > 0)
                            lReading = (SensorReading) list.get(0);
                if(lReading != null) {
                    if(lReading instanceof LightReading)
                        Log.d("LightmeterActivity", "GREAT!!!");
                    if(lReading instanceof AccelerometerReading)
                        Log.d("LightmeterActivity", "GREAT!!!");

                }else {
                    Log.d("LightmeterActivity", "BAD");
                }

            }

            @Override

            public void failure(ErrorReading reading) throws RemoteException {
                Log.d("LightmeterActivity", "FAILURE");
            }

        };

        try {
            nervousnetServiceController.mService.getReadings(
                    LibConstants.SENSOR_ACCELEROMETER,
                    startTime,
                    stopTime,
                    cbBinder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
    public void onServiceConnectionFailed() {

    }


    /*public ArrayList<Double> getLightValues(int n){

        if (mServiceConnection == null) initConnection();
        if (mService == null) doBindService();

        // If binding is not ok, this function will handle it
        checkBinding();

        ArrayList<Double> array = new ArrayList<Double>();

        if (mService != null) {
            // Example
            for(int i = 0; i < n; i++){
                Float fl = new Float(getLatestLightValue());
                array.add(fl.doubleValue());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(context.getApplicationContext(),
                    "NervousnetRemote Service not connected", Toast.LENGTH_SHORT).show();
        }

        return array;
    }

    public float getSimulatedValue(){
        Random rand = new Random();
        return rand.nextFloat();
    }
*/
    /*public ArrayList<Double> getLightValues(long startTimeEpoch, long endTimeEpoch){

        checkBinding();
        ArrayList<Double> values = new ArrayList();
        RemoteCallbackList list = new RemoteCallbackList();
        try {
            mService.getReadings(LibConstants.SENSOR_LIGHT, startTimeEpoch, endTimeEpoch, list);
        } catch (DeadObjectException doe) {
            doe.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return values;
    }*/

    /*public ArrayList getAccelerometerValues(long startTimeEpoch, long endTimeEpoch){

        checkBinding();
        ArrayList list = new ArrayList();
        try {
            mService.getReadings(LibConstants.SENSOR_ACCELEROMETER, System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), System.currentTimeMillis(), list);
        } catch (DeadObjectException doe) {
            doe.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //Log.d("ACC", values.get(0).toString());
        return list;
    }*/


    /*public float getLatestLightValue(){
        LightReading lReading = null;
        Log.d("MSERVICE_2", mService+"");
        try {
            lReading = (LightReading) mService.getLatestReading(LibConstants.SENSOR_LIGHT);
            long a = lReading.timestamp;
            if (lReading != null) {
                Log.d("Nervousnet", "Light=" + lReading.getLuxValue());
                return lReading.getLuxValue();
            } else {
                Log.d("Light object is null", "");
            }
        } catch (DeadObjectException doe) {
            // TODO Auto-generated catch block
            doe.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return -1;
    }*/

}
