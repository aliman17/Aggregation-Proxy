package sensor;

/**
 * Created by ales on 03/08/16.
 */
public class SensorPoint {
    protected int sensorType;
    protected long timestamp;
    protected double[] values;
    protected int dimensions;

    public SensorPoint(){}

    public SensorPoint(int type, long timestamp, double[] values){
        this.sensorType = type;
        this.timestamp = timestamp;
        this.values = values;
        this.dimensions = values.length;
    }

    public int getSensorType()  { return sensorType; }
    public int getDimensions()  { return this.dimensions; }
    public long getTimestamp()  { return timestamp; }
    public double[] getValues() { return values; }
}
