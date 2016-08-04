package sensor;

import java.util.ArrayList;

/**
 * Created by ales on 03/08/16.
 */
public class MultiSensorPoint extends SensorPoint {

    public MultiSensorPoint(ArrayList<SensorPoint> points){
        // Determine type, we just set to -1
        this.sensorType = -1;

        // Determine dimensions
        int dimensions = 0;
        long timestamp = 0;
        for (SensorPoint p : points) {
            dimensions += p.getDimensions();
            timestamp += p.getTimestamp();
        }
        timestamp /= dimensions;
        this.dimensions = dimensions;
        this.timestamp = timestamp;

        // Set values
        double[] values = new double[dimensions];
        int i = 0;
        for (SensorPoint p : points) {
            double[] pValues = p.getValues();
            for (int j = 0; j < pValues.length; j++)
                values[i++] = pValues[j];
        }
        this.values = values;

    }
}
