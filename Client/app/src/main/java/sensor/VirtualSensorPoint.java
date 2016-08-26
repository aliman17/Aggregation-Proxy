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
    private boolean[] usedFields;

    public VirtualSensorPoint(){
        super(DIMENSIONS);
        usedFields = new boolean[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++){
            usedFields[i] = false;
        }
    }


    // SETTER

    public void setNoise(double val){
        this.coordinates[N] = val;
        this.usedFields[N] = true;
    }
    public void setLight(double val){
        this.coordinates[L] = val;
        this.usedFields[L] = true;
    }
    public void setBattery(double val){
        this.coordinates[B] = val;
        this.usedFields[B] = true;
    }
    public void setAccelerometer(double x, double y, double z){
        this.coordinates[ACCX] = x;
        this.coordinates[ACCY] = y;
        this.coordinates[ACCZ] = z;
        this.usedFields[ACCX] = true;
        this.usedFields[ACCY] = true;
        this.usedFields[ACCZ] = true;
    }
    public void setGyrometer(double x, double y, double z){
        this.coordinates[GYROX] = x;
        this.coordinates[GYROY] = y;
        this.coordinates[GYROZ] = z;
        this.usedFields[GYROX] = true;
        this.usedFields[GYROY] = true;
        this.usedFields[GYROZ] = true;
    }
    public void setProximity(double val){
        this.coordinates[PROX] = val;
        this.usedFields[PROX] = true;
    }


    // GETTER
    public long getTimestamp()  {
        return timestamp;
    }

    public double getNoise(){
        return coordinates[N];
    }

    public double getLight(){
        return coordinates[L];
    }

    public double getBattery() {
        return coordinates[B];
    }

    public double[] getAccelerometer() {
        return new double[] {
                coordinates[ACCX],
                coordinates[ACCY],
                coordinates[ACCZ]
        };
    }

    public double[] getGryometer() {
        return new double[] {
                coordinates[GYROX],
                coordinates[GYROY],
                coordinates[GYROZ]
        };
    }

    public double getProximity() {
        return coordinates[PROX];
    }

}
