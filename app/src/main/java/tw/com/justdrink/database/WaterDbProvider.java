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

import static tw.com.justdrink.database.WaterDatabase.WATER_TABLE;


public class WaterDbProvider extends ContentProvider {

    Context context;
    SQLiteDatabase sqLiteDatabase;
    WaterDatabase waterDatabase;

    private static HashMap<String, String> WaterMap;

    public static final String PROVIDER_NAME = "tw.com.justdrink.database.WaterProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/Water";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static final int Water = 1;
    private static final int Water_Id = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "Water", Water);
        uriMatcher.addURI(PROVIDER_NAME, "Water/#", Water);
    }

    public WaterDbProvider() {
    }


    @Override
    public boolean onCreate() {
        context = getContext();
        waterDatabase = new WaterDatabase(context);
        sqLiteDatabase = waterDatabase.getWritableDatabase();
        if (sqLiteDatabase == null)
            return false;
        else
            return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);
        qb.setTables(WATER_TABLE);
        switch (uriType) {
            case Water:
                qb.setProjectionMap(WaterMap);
                break;
            case Water_Id:
                qb.appendWhere(WaterDatabase.KEY_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            // No sorting-> sort on names by default
            sortOrder = WaterDatabase.KEY_DATE + " DESC, " + WaterDatabase.KEY_ID;
        }
        Cursor cursor = qb.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
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
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        Uri _uri = null;
        switch (uriType) {
            case Water:
                long rowID = sqLiteDatabase.insert(WATER_TABLE, null, values);
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            default:
                throw new SQLException("Error inserting into table: " + WATER_TABLE);
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
                count = sqLiteDatabase.delete(WATER_TABLE, WaterDatabase.KEY_ID + " = " + id +
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
                count = sqLiteDatabase.update(WATER_TABLE, values, WaterDatabase.KEY_ID +
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
