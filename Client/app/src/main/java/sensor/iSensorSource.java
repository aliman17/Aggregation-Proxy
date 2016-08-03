package sensor;

import java.util.ArrayList;

/**
 * Created by ales on 03/08/16.
 */
public interface iSensorSource {
    public SensorPoint getLatestLightValue();
    public ArrayList<SensorPoint> getLightValues(long startTime, long stopTime);

    public SensorPoint getLatestAccValue();
    public ArrayList<SensorPoint> getAccValues(long startTime, long stopTime);
}
