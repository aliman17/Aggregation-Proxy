package state;

/**
 * Created by ales on 28/07/16.
 */
public class PossibleStatePoint {
    public double[] value;

    public PossibleStatePoint(){}

    public PossibleStatePoint(double[] coordinates){
        this.value = coordinates;
    }

    public void setCopy(double[] coordinates){
        value = new double[coordinates.length];
        int len = coordinates.length;
        for (int i = 0; i < len; i++)
            value[i] = coordinates[i];
    }
}
