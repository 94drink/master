package tw.com.justdrink.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WaterDBHelper extends SQLiteOpenHelper {

    // Database Version
    static final int DATABASE_VERSION = 1;

    // Database Name
    static final String DATABASE_NAME = "JustDrink.db";

    // Table name
    static final String WATER_TABLE = "Water";
    static final String WEIGHT_TABLE = "Weight";

    // Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_POS = "position";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_WID = "_id";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_WDATE = "date";
    public static final String KEY_ML = "ml";
    public static final String KEY_WIML = "wiml";
    public static final String KEY_SEML = "seml";
    public static final String KEY_WEML = "weml";
    public static final String KEY_SPML = "spml";
    public static final String KEY_TOTML = "totml";

    public WaterDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String water_sql = "CREATE TABLE " + WATER_TABLE + " ( "
                + KEY_ID + " integer PRIMARY KEY, "
                + KEY_POS + " text NOT NULL, "
                + KEY_ML + " integer NOT NULL, "
                + KEY_DATE + " text NOT NULL, "
                + KEY_TIME + " text NOT NULL"
                + " ) ";

        String weight_sql = "CREATE TABLE " + WEIGHT_TABLE + " ( "
                + KEY_WID + " integer PRIMARY KEY, "
                + KEY_WEIGHT + " text NOT NULL, "
                + KEY_WDATE + " text NOT NULL, "
                + KEY_WIML + " int NOT NULL, "
                + KEY_SEML + " int NOT NULL, "
                + KEY_WEML + " int NOT NULL, "
                + KEY_SPML + " int NOT NULL, "
                + KEY_TOTML + " int NOT NULL"
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
