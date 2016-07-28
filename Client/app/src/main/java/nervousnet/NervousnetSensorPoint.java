package nervousnet;

/**
 * Created by ales on 28/07/16.
 */
public class NervousnetSensorPoint {
    private int sensorType;
    private long timestamp;
    private double[] values;

    public NervousnetSensorPoint(int type, long timestamp, double[] values){
        this.sensorType = type;
        this.timestamp = timestamp;
        this.values = values;
    }

    public int getSensorType()  { return sensorType; }
    public long getTimestamp()  { return timestamp; }
    public double[] getValues() { return values; }
}
