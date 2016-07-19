package nervousnet;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import ch.ethz.coss.nervousnet.lib.Utils;

/**
 * Created by ales on 28/06/16.
 */
public class Nervousnet {

    // We need context to get connections and sensor data
    private Context context;

    // Connection to the service
    protected NervousnetRemote mService;
    private   ServiceConnection mServiceConnection;
    private   Boolean bindFlag;

    // Data
    private ArrayList<Double> lightData;

    // Constructor
    public Nervousnet(Context context){
        this.context = context;
    }

    // RUN THIS
    public void connect(){
        initConnection();
        doBindService();
    }

    // 1) Initialize connection
    public void initConnection(){
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mService = NervousnetRemote.Stub.asInterface(service);
                Toast.makeText(context.getApplicationContext(),
                        "NervousnetRemote Service connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
                mServiceConnection = null;
                Toast.makeText(context.getApplicationContext(),
                        "NervousnetRemote Service disconnected", Toast.LENGTH_SHORT).show();
            }
        };
    }

    // 2) Bind service to the mService
    public void doBindService(){
        Intent it = new Intent();
        it.setClassName("ch.ethz.coss.nervousnet.hub",
                "ch.ethz.coss.nervousnet.hub.NervousnetHubApiService");
        if (mService == null)
            bindFlag = context.bindService(it, mServiceConnection, 0);
    }

    // 3) Unbind service when needed
    public void doUnbindService(){
        context.unbindService(mServiceConnection);
        bindFlag = false;
    }

    public void checkBinding(){
        if (!bindFlag){
            Utils.displayAlert(context, "Alert",
                    "Nervousnet HUB application is required to be installed and running to use " +
                            "this app. If not installed please download it from the App Store. " +
                            "If already installed, please turn on the Data Collection option " +
                            "inside the Nervousnet HUB application.",
                    "Download Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=ch.ethz.coss.nervousnet.hub")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=ch.ethz.coss.nervousnet.hub")));
                            }

                        }
                    }, "Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    });
        }
    }

    public float getLightValue(){
        LightReading lReading = null;
        try {
            lReading = (LightReading) mService.getReading(LibConstants.SENSOR_LIGHT);
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
        }
        return -1;
    }

    public ArrayList<Double> getLightValues(int n){

        if (mServiceConnection == null) initConnection();
        if (mService == null) doBindService();

        // If binding is not ok, this function will handle it
        checkBinding();

        ArrayList<Double> array = new ArrayList<Double>();

        if (mService != null) {
            // Example
            for(int i = 0; i < n; i++){
                Float fl = new Float(getLightValue());
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


}
