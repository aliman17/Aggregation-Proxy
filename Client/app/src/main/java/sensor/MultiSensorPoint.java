package sensor;

import java.util.ArrayList;

/**
 * Created by ales on 03/08/16.
 */
public class MultiSensorPoint {
    private long timestamp;     // average of all timestamps
    private int dimensions;     // sum of all points dimensions
    private double[] values;    // values of all points

    public MultiSensorPoint(ArrayList<SensorPoint> points){
        // Determine dimensions
        dimensions = 0;
        timestamp = 0;
        for (SensorPoint p : points) {
            dimensions += p.getDimensions();
            timestamp += p.getTimestamp();
        }
        timestamp /= dimensions;

        // Set values
        values = new double[dimensions];
        int i = 0;
        for (SensorPoint p : points) {
            double[] pValues = p.getValues();
            for (int j = 0; j < pValues.length; j++)
                values[i++] = pValues[j];
        }
    }

    public int getDimensions(){ return this.dimensions; }
    public double[] getValues() { return this.values; }
}
