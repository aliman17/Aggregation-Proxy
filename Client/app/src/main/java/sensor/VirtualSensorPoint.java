package sensor;

import clustering.Point;

/**
 * Created by ales on 03/08/16.
 */
public class VirtualSensorPoint extends Point {

    private long timestamp;

    /*
    The coordinates represents [Noise, Light, Battery, accX, accY, accZ, gyroX, gyroY, gyroZ, Proxim, free, free]
    The fields are initialized to -INF. If there is field with value -INF, then the data hasn't been
    inserted. The field positions of specific sensord are determined below.
     */
    final int N = 0;
    final int L = 1;
    final int B = 2;
    final int ACCX = 3;
    final int ACCY = 4;
    final int ACCZ = 5;
    final int GYROX = 6;
    final int GYROY = 7;
    final int GYROZ = 8;
    final int PROX = 9;

    private static final int DIMENSIONS = 10;
    private double[] origValues; // original values
    private double[] coordinates; // coordinates for clustering

    public VirtualSensorPoint(){
        super(DIMENSIONS);
        origValues = new double[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++){
            origValues[i] = Double.NaN;
        }
    }


    // SETTER

    public void setNoise(double val){
        this.origValues[N] = val;
    }

    public void setLight(double val){
        this.origValues[L] = val;
    }

    public void setBattery(double val){
        this.origValues[B] = val;
    }

    public void setAccelerometer(double x, double y, double z){
        this.origValues[ACCX] = x;
        this.origValues[ACCY] = y;
        this.origValues[ACCZ] = z;
    }

    public void setGyrometer(double x, double y, double z){
        this.origValues[GYROX] = x;
        this.origValues[GYROY] = y;
        this.origValues[GYROZ] = z;
    }

    public void setProximity(double val){
        this.origValues[PROX] = val;
    }

    public void finishSetting(){
        int size = 0;
        for(int i = 0; i < DIMENSIONS; i++)
            if (origValues[i] != Double.NaN)
                size ++;
        this.coordinates = new double[size];
        int coordPtr = 0;
        for(int i = 0; i < DIMENSIONS; i++){
            if (origValues[i] != Double.NaN){
                coordinates[coordPtr] = origValues[i];
                coordPtr++;
            }
        }
    }


    // GETTER
    public long getTimestamp()  {
        return timestamp;
    }

    public double getNoise(){
        return origValues[N];
    }

    public double getLight(){
        return origValues[L];
    }

    public double getBattery() {
        return origValues[B];
    }

    public double[] getAccelerometer() {
        return new double[] {
                origValues[ACCX],
                origValues[ACCY],
                origValues[ACCZ]
        };
    }

    public double[] getGryometer() {
        return new double[] {
                origValues[GYROX],
                origValues[GYROY],
                origValues[GYROZ]
        };
    }

    public double getProximity() {
        return origValues[PROX];
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }

    public double[] getOriginalValues(){
        return origValues;
    }
}
