package data;

import android.os.RemoteException;

import java.util.ArrayList;

import sensor.SensorPoint;

/**
 * Created by ales on 03/08/16.
 */
public interface iDataSource {
    public SensorPoint getLatestLightValue() throws RemoteException;
    public ArrayList<SensorPoint> getLightValues(long startTime, long stopTime) throws RemoteException;

    public SensorPoint getLatestAccValue() throws RemoteException;
    public ArrayList<SensorPoint> getAccValues(long startTime, long stopTime) throws RemoteException;

    public SensorPoint getLatestBatteryValue() throws RemoteException;
    public ArrayList<SensorPoint> getBatteryValues(long startTime, long stopTime) throws RemoteException;

    public SensorPoint getLatestNoiseValue() throws RemoteException;
    public ArrayList<SensorPoint> getNoiseValues(long startTime, long stopTime) throws RemoteException;
}
