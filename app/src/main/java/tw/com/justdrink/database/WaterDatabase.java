package tw.com.justdrink.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WaterDatabase extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "JustDrink";

    // Table name
    static final String WATER_TABLE = "Water";
    static final String WEIGHT_TABLE = "Weight";

    // Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_POS = "position";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_WEIGHT = "weight";

    public WaterDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String water_sql = "CREATE TABLE " + WATER_TABLE + " ( "
                + KEY_ID + " integer PRIMARY KEY, "
                + KEY_POS + " text NOT NULL, "
                + KEY_DATE + " text NOT NULL, "
                + KEY_TIME + " text NOT NULL"
                + " ) ";

        String weight_sql = "CREATE TABLE " + WEIGHT_TABLE + " ( "
                + KEY_ID + " integer PRIMARY KEY, "
                + KEY_WEIGHT + " int NOT NULL, "
                + KEY_DATE + " text NOT NULL, "
                + KEY_TIME + " text NOT NULL"
                + " ) ";

        db.execSQL(water_sql);
        db.execSQL(weight_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WATER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WEIGHT_TABLE);
        onCreate(db);
    }
}
