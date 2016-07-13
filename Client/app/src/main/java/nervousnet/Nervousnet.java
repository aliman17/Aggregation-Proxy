package nervousnet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;

/**
 * Created by ales on 28/06/16.
 */
public class Nervousnet {
    private Context context;

    protected NervousnetRemote mService;
    private ServiceConnection mServiceConnection;
    private Boolean bindFlag;

    private ArrayList<Double> lightData;

    public Nervousnet(Context context){
        this.context = context;
        //initConnection();
        //doBindService();
        //Toast.makeText(context.getApplicationContext(),
        //        "NervousnetRemote Service connected " + mService, Toast.LENGTH_SHORT).show();
    }

    public void initConnection(){
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mService = NervousnetRemote.Stub.asInterface(service);
                Toast.makeText(context.getApplicationContext(),
                        "NervousnetRemote Service connected", Toast.LENGTH_SHORT).show();

                // Example
                AccelerometerReading lReading = null;
                try {
                    lReading = (AccelerometerReading) mService.getReading(LibConstants.SENSOR_ACCELEROMETER);
                    if (lReading != null) {
                        Log.d("Light", ""+lReading.getX() + " " + mService);
                    } else {
                        Log.d("Light object is null", "");
                    }
                } catch (DeadObjectException doe) {
                    // TODO Auto-generated catch block
                    doe.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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

    public void doBindService(){
        Intent it = new Intent();
        it.setClassName("ch.ethz.coss.nervousnet.hub", "ch.ethz.coss.nervousnet.hub.NervousnetHubApiService");
        if (mService == null)
            bindFlag = context.bindService(it, mServiceConnection, 0);
    }

    public void doUnbindService(){
        context.unbindService(mServiceConnection);
        bindFlag = false;
    }

    // TODO: this is basic function, to return some example data
    public ArrayList<Double> getLightData(){
        ArrayList<Double> array = new ArrayList<Double>();
        array.add(new Double(12));
        array.add(new Double(14));
        array.add(new Double(18));
        return array;
    }
}
