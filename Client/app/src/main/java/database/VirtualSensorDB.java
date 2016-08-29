package database;

/**
 * Created by ales on 27/06/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import sensor.VirtualSensorPoint;

public class VirtualSensorDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VirtualSensorDB";
    private static final String TABLE = "VirtualSensor";
    private static final String KEY_ID = "id";

    private static final String vtimestamp = "VTimestamp";
    private static final String vnoise = "VNoise";
    private static final String vlight = "VLight";
    private static final String vbattery = "VBattery";
    private static final String vaccX = "VaccX";
    private static final String vaccY = "VaccY";
    private static final String vaccZ = "VaccZ";
    private static final String vgyroX = "VgyroX";
    private static final String vgyroY = "VgyroY";
    private static final String vgyroZ = "VgyroZ";
    private static final String vproxim = "VProximity";

    private static final String onoise = "ONoise";
    private static final String olight = "OLight";
    private static final String obattery = "OBattery";
    private static final String oaccX = "OaccX";
    private static final String oaccY = "OaccY";
    private static final String oaccZ = "OaccZ";
    private static final String ogyroX = "OgyroX";
    private static final String ogyroY = "OgyroY";
    private static final String ogyroZ = "OgyroZ";
    private static final String oproxim = "OProximity";

    public VirtualSensorDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        String CREATE_TABLE = "CREATE TABLE "+TABLE+" ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                // Virtual sensor data
                vtimestamp +" NUMERIC," +
                vnoise +" REAL," +
                vlight  +" REAL," +
                vbattery  +" REAL," +
                vaccX  +" REAL," +
                vaccY +" REAL," +
                vaccZ +" REAL," +
                vgyroX +" REAL," +
                vgyroY +" REAL," +
                vgyroZ +" REAL," +
                vproxim +" REAL," +
                // Original sensor data
                onoise +" REAL," +
                olight +" REAL," +
                obattery +" REAL," +
                oaccX +" REAL," +
                oaccY +" REAL," +
                oaccZ +" REAL," +
                ogyroX +" REAL," +
                ogyroY +" REAL," +
                ogyroZ +" REAL," +
                oproxim +" REAL" +
                ")";
        // create table
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);

        // create fresh table
        this.onCreate(db);
    }

    public void add(VirtualSensorPoint point){
        //Log.d("addSensorValue", String.valueOf(Arrays.toString(point.getCluster().getCoordinates())));
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(vnoise, point.getNoise());
        values.put(vlight, point.getLight());
        /*values.put(vbattery, point.getBattery());
        values.put(vaccX, point.getAccelerometer()[0]);
        values.put(vaccY, point.getAccelerometer()[1]);
        values.put(vaccZ, point.getAccelerometer()[2]);
        values.put(vgyroX, point.getGryometer()[0]);
        values.put(vgyroY, point.getGryometer()[1]);
        values.put(vgyroZ, point.getGryometer()[2]);
        values.put(vproxim, point.getProximity());*/

        values.put(vnoise, point.getNoise());
        values.put(vlight, point.getLight());
        /*values.put(vbattery, point.getBattery());
        values.put(vaccX, point.getAccelerometer()[0]);
        values.put(vaccY, point.getAccelerometer()[1]);
        values.put(vaccZ, point.getAccelerometer()[2]);
        values.put(vgyroX, point.getGryometer()[0]);
        values.put(vgyroY, point.getGryometer()[1]);
        values.put(vgyroZ, point.getGryometer()[2]);
        values.put(vproxim, point.getProximity());*/
        // 3. insert

        db.insert(TABLE,    // table
                null,       //nullColumnHack
                values);    // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public VirtualSensorPoint get(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE,     // a. table
                        null,       // b. column names
                        null,       // c. selections
                        null,       // d. selections args
                        null,       // e. group by
                        null,       // f. having
                        null,       // g. order by
                        null);      // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build object

        //Log.d("getSensorValue("+id+")", cursor.getString(1));
        VirtualSensorPoint vpoint = getVSP(cursor);

        Log.d("DB-get", Arrays.toString(vpoint.getCoordinates()));
        // 5. return
        return vpoint;
    }

    private VirtualSensorPoint getVSP(Cursor cursor){

        VirtualSensorPoint vp = new VirtualSensorPoint();

        vp.setNoise( cursor.getDouble(2));
        vp.setLight( cursor.getDouble(3));
        vp.setBattery( cursor.getDouble(4));
        vp.setAccelerometer( cursor.getDouble(5),
                cursor.getDouble(6),
                cursor.getDouble(7));
        vp.setGyrometer( cursor.getDouble(8),
                cursor.getDouble(9),
                cursor.getDouble(10));
        vp.setProximity( cursor.getDouble(11));
        vp.finishSetting();
        return vp;
    }

    public ArrayList<VirtualSensorPoint> getAll() {
        ArrayList<VirtualSensorPoint> values = new ArrayList();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build sensor value and add it to list
        if (cursor.moveToFirst()) {
            do {
                VirtualSensorPoint vp = getVSP(cursor);
                values.add(vp);
            } while (cursor.moveToNext());
        }
        return values;
    }

    public static void test(Context context){
        Log.d("TEST", "Start ...");
        VirtualSensorPoint vpoint = new VirtualSensorPoint();

        // Test adding values
        vpoint.setNoise(123);
        vpoint.setLight(511);

        VirtualSensorDB db = new VirtualSensorDB(context);
        db.add(vpoint);

        // Test reading values
        ArrayList<VirtualSensorPoint> list = db.getAll();
        for( VirtualSensorPoint vp : list )
            Log.d("VP",Arrays.toString(vp.getOriginalValues()));

        Log.d("TEST", "Stop");
    }
}
