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

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExampleDB";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        String CREATE_TABLE = "CREATE TABLE sensor ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value REAL )";
        // create table
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS sensor");

        // create fresh table
        this.onCreate(db);
    }

    // Sensor table name
    private static final String TABLE = "sensor";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VALUE = "value";
    private static final String[] COLUMNS = {KEY_ID, KEY_VALUE};


    public void addSensorValue(double sensorMeasurement){
        Log.d("addSensorValue", String.valueOf(sensorMeasurement));
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, sensorMeasurement);

        // 3. insert
        db.insert(TABLE,    // table
                null,       //nullColumnHack
                values);    // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public double getSensorValue(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE,     // a. table
                        COLUMNS,    // b. column names
                        " id = ?",  // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null,       // e. group by
                        null,       // f. having
                        null,       // g. order by
                        null);      // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build object

        Log.d("getSensorValue("+id+")", cursor.getString(1));

        // 5. return
        return Double.parseDouble(cursor.getString(1));
    }

    public ArrayList<Double> getAllSensorValues() {
        ArrayList<Double> values = new ArrayList<Double>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build sensor value and add it to list
        if (cursor.moveToFirst()) {
            do {
                double val = Double.parseDouble(cursor.getString(1));
                values.add(val);
            } while (cursor.moveToNext());
        }

        Log.d("getAllSensorValues()", values.toString());

        return values;
    }
}
