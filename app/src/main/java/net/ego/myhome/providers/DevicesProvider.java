package net.ego.myhome.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DevicesProvider extends ContentProvider {

    static final String TAG="DevicesProvider";
    static final String PROVIDER_NAME = "net.ego.myhome.providers.DevicesProvider";
    static final String URL = "content://"+PROVIDER_NAME+"/devices";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String VALUE = "value";

    static final int DEVICES = 1;
    static final int DEVICE_ID = 2;

    private static HashMap<String, String> DEVICES_PROJECTION_MAP;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "devices", DEVICES);
        uriMatcher.addURI(PROVIDER_NAME, "devices/#", DEVICE_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "domoticz.db";
    static final String DEVICES_TABLE_NAME = "devices";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            "CREATE TABLE "+ DEVICES_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "name TEXT NOT NULL, "+
                    "value TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DEVICES_TABLE_NAME);
            onCreate(db);
        }
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
       return (db == null) ? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DEVICES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case DEVICES:
                queryBuilder.setProjectionMap(DEVICES_PROJECTION_MAP);
                break;
            case DEVICE_ID:
                queryBuilder.appendWhere(_ID + "="+uri.getPathSegments().get(1));
                break;
            default:
        }

        if (sortOrder == null || sortOrder == "") {
            sortOrder = NAME; // by default sort on device name
        }

        Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DEVICES:
                return "vnd.android.cursor.dir/vnd.example.devices";
            case DEVICE_ID:
                return "vnd.android.cursor.item/vnd.example.devices";
            default:
                throw new IllegalArgumentException("Unsupported uri "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //Add device record
        if (null == db) {
            Log.d(TAG,"db is null");
        }
        if (null == values) {
            Log.d(TAG, "values is null");
        }
        long rowId = db.insert(DEVICES_TABLE_NAME,"",values);

        //check if record successfully added, notify about added uri
        if (rowId>0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add record into "+uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri)) {
            case DEVICES:
                count = db.delete(DEVICES_TABLE_NAME, selection, selectionArgs);
                break;

            case DEVICE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DEVICES_TABLE_NAME, _ID+" = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND ("+
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case DEVICES:
                count = db.update(DEVICES_TABLE_NAME, values, selection, selectionArgs);
                break;
            case DEVICE_ID:
                count = db.update(DEVICES_TABLE_NAME, values, _ID + "=" +
                        uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
