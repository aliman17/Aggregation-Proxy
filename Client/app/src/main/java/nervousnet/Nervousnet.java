package nervousnet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

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

    public Nervousnet(Context context){
        this.context = context;
        initConnection();
    }

    public void initConnection(){
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("IT COMES HERE", "");
                mService = NervousnetRemote.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
                mServiceConnection = null;
            }
        };
    }

    public void doBindService(){
        Intent it = new Intent();
        it.setClassName("ch.ethz.coss.nervousnet.hub", "ch.ethz.coss.nervousnet.hub.NervousnetHubApiService");
        bindFlag = context.bindService(it, mServiceConnection, 0);
        Log.d("Nervousnet", "mService:"+mService + " bindFlag:"+bindFlag);
    }

    public void doUnbindService(){
        context.unbindService(mServiceConnection);
        bindFlag = false;
    }


    public void test(){
        if (mServiceConnection == null) {
            initConnection();
        }

        if (mService == null) {
            doBindService();
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AccelerometerReading lReading = null;
            try {
                lReading = (AccelerometerReading) mService.getReading(LibConstants.SENSOR_ACCELEROMETER);
                if (lReading != null) {
                    Log.d("Light", ""+lReading.getX());
                } else {
                    Log.d("Light object is null", "");
                }
            } catch (DeadObjectException doe) {
                // TODO Auto-generated catch block
                doe.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
        } else {
            Log.d("", "mService is NULL");
        }

    }
}
