package tw.com.justdrink.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import static tw.com.justdrink.database.WaterDBHelper.WATER_TABLE;
import static tw.com.justdrink.database.WaterDBHelper.WEIGHT_TABLE;


public class WaterDbProvider extends ContentProvider {

    Context context;
    SQLiteDatabase sqLiteDatabase;
    WaterDBHelper WaterDBHelper;

    private static HashMap<String, String> WaterMap;
    private static HashMap<String, String> WeightMap;

    public static final String PROVIDER_NAME = "tw.com.justdrink.database.WaterProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/Water";
    public static final String WEI = "content://" + PROVIDER_NAME + "/Weight";
    public static final Uri CONTENT_URI_WATER = Uri.parse(URL);
    public static final Uri CONTENT_URI_WEIGHT = Uri.parse(WEI);

    private static final int Water = 1;
    private static final int Water_Id = 2;
    private static final int Weight = 3;
    private static final int Weight_Id = 4;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "Water", Water);
        uriMatcher.addURI(PROVIDER_NAME, "Water/#", Water);
        uriMatcher.addURI(PROVIDER_NAME, "Weight", Weight);
        uriMatcher.addURI(PROVIDER_NAME, "Weight/#", Weight);
    }

    public WaterDbProvider() {
    }


    @Override
    public boolean onCreate() {
        context = getContext();
        WaterDBHelper = new WaterDBHelper(context);
        sqLiteDatabase = WaterDBHelper.getWritableDatabase();
        if (sqLiteDatabase == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case Water:
            case Water_Id:
                qb.setTables(WATER_TABLE);
                if (sortOrder == null || sortOrder == "") {
                    // No sorting-> sort on names by default
                    sortOrder = WaterDBHelper.KEY_DATE + " DESC, " + WaterDBHelper.KEY_ID;
                }
                break;
            case Weight:
            case Weight_Id:
                qb.setTables(WEIGHT_TABLE);
                if (sortOrder == null || sortOrder == "") {
                    // No sorting-> sort on names by default
                    sortOrder = WaterDBHelper.KEY_WDATE + " DESC, " + WaterDBHelper.KEY_WID;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        switch (uriType) {
            case Water:
                qb.setProjectionMap(WaterMap);
                break;
            case Water_Id:
                qb.appendWhere(WaterDBHelper.KEY_ID + "=" + uri.getLastPathSegment());
                break;
            case Weight:
                qb.setProjectionMap(WeightMap);
                break;
            case Weight_Id:
                qb.appendWhere(WaterDBHelper.KEY_WID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        SQLiteDatabase db = WaterDBHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case Water:
                return "vnd.android.cursor.dir/Water";
            case Water_Id:
                return "vnd.android.cursor.item/Water/#";
            case Weight:
                return "vnd.android.cursor.dir/Weight";
            case Weight_Id:
                return "vnd.android.cursor.item/Weight/#";
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null){
            values = new ContentValues(initialValues);
        }else {
            values = new ContentValues();
        }
        int uriType = uriMatcher.match(uri);
        Uri _uri = null;
        switch (uriType) {
            case Water:
                long rowID = sqLiteDatabase.insert(WATER_TABLE, null, values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_WATER, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case Weight:
                long row2ID = sqLiteDatabase.insert(WEIGHT_TABLE, null, values);
                if (row2ID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_WEIGHT, row2ID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default:
                throw new SQLException("Error inserting into table " + _uri);
        }
        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case Water:
                count = sqLiteDatabase.delete(WATER_TABLE, selection, selectionArgs);
                break;
            case Water_Id:
                String id = uri.getLastPathSegment();    //gets the id
                count = sqLiteDatabase.delete(WATER_TABLE, WaterDBHelper.KEY_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case Weight:
                count = sqLiteDatabase.delete(WEIGHT_TABLE, selection, selectionArgs);
                break;
            case Weight_Id:
                String id2 = uri.getLastPathSegment();    //gets the id
                count = sqLiteDatabase.delete(WEIGHT_TABLE, WaterDBHelper.KEY_WID + " = " + id2 +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case Water:
                count = sqLiteDatabase.update(WATER_TABLE, values, selection, selectionArgs);
                break;
            case Water_Id:
                count = sqLiteDatabase.update(WATER_TABLE, values, WaterDBHelper.KEY_ID +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            case Weight:
                count = sqLiteDatabase.update(WEIGHT_TABLE, values, selection, selectionArgs);
                break;
            case Weight_Id:
                count = sqLiteDatabase.update(WEIGHT_TABLE, values, WaterDBHelper.KEY_WID +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
