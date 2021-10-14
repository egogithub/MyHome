package net.ego.myhome.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import net.ego.myhome.database.TemperatureDbContract;
import net.ego.myhome.database.TemperatureDbHelper;

import java.util.concurrent.locks.ReentrantLock;

public class TempContentProvider extends ContentProvider {

    //Temperature database helper
    private TemperatureDbHelper mTempDbHelper;
    public static final String CONTENT_PROVIDER_TEMPERATURE_AUTHORITY = "net.ego.myhome.TempAuthority";
    public static final Uri TEMPERATURE_URI = Uri.parse(("content://"+CONTENT_PROVIDER_TEMPERATURE_AUTHORITY + "/" + TemperatureDbContract.TABLE_TEMPERATURE));
    public static final String TEMPERATURE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.myhome.tempdevices";

    public static final String BATTERY_LEVEL = TemperatureDbContract.BATTERY_LEVEL;
    public static final String DEVICE_DATA = TemperatureDbContract.DEVICE_DATA;
    public static final String DEW_POINT = TemperatureDbContract.DEW_POINT;
    public static final String FAVORITE = TemperatureDbContract.FAVORITE;
    public static final String HUMIDITY = TemperatureDbContract.HUMIDITY;
    public static final String HUMIDITY_STATUS = TemperatureDbContract.HUMIDITY_STATUS;
    public static final String DEVICE_ID = TemperatureDbContract.DEVICE_ID;
    public static final String LAST_UPDATE = TemperatureDbContract.LAST_UPDATE;
    public static final String DEVICE_NAME = TemperatureDbContract.DEVICE_NAME;
    public static final String DEVICE_SUBTYPE = TemperatureDbContract.DEVICE_SUBTYPE;
    public static final String TEMPERATURE = TemperatureDbContract.TEMPERATURE;
    public static final String DEVICE_TYPE = TemperatureDbContract.DEVICE_TYPE;
    public static final String TYPE_IMG = TemperatureDbContract.TYPE_IMG;
    public static final String DEVICE_IDX = TemperatureDbContract.DEVICE_IDX;

    static final String _ID = "_id";
    static final int DEVICES = 1;
    static final int DEV_ID = 2;

    //The URI matcher to check if URI is correct
    static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(CONTENT_PROVIDER_TEMPERATURE_AUTHORITY, "temperature", DEVICES);
        mUriMatcher.addURI(CONTENT_PROVIDER_TEMPERATURE_AUTHORITY, "temperature/#", DEV_ID);
    }

    // Use a Lock to be thread-safe
    private final ReentrantLock mLock = new ReentrantLock();

    public TempContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int count=0;
        SQLiteDatabase db = mTempDbHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case DEVICES:
                mLock.lock();
                try {
                    count = db.delete(TemperatureDbContract.TABLE_TEMPERATURE, selection, selectionArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                } catch (Exception e) {
                    return 0;
                }
                finally {
                    mLock.unlock();
                }
                break;
            case DEV_ID:
                mLock.lock();
                String id = uri.getPathSegments().get(1);
                try {
                    count = db.delete(TemperatureDbContract.TABLE_TEMPERATURE, _ID+" = " + id +
                            (!TextUtils.isEmpty(selection) ? " AND ("+
                                    selection + ')' : ""), selectionArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    mLock.unlock();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+uri);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case DEVICES:
                return TEMPERATURE_CONTENT_TYPE;
            case DEV_ID:
                return TEMPERATURE_CONTENT_TYPE;
        }
        throw new IllegalArgumentException("Unknown URI "+uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mTempDbHelper.getWritableDatabase();
        switch(mUriMatcher.match(uri)) {
            case DEVICES:
                mLock.lock();
                try {
                    final long rowId = db.insert(TemperatureDbContract.TABLE_TEMPERATURE, "", values);
                    if (rowId > 0) {
                        final Uri _uri = ContentUris.withAppendedId(TEMPERATURE_URI, rowId);
                        getContext().getContentResolver().notifyChange(_uri, null);
                        return _uri;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mLock.unlock();
                }
                break;
            case DEV_ID:
                Log.d("TempContentProvider", "Selected URI not implemented yet");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+uri);
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        mTempDbHelper = new TemperatureDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mTempDbHelper.getWritableDatabase();
        String orderBy;
        if (null == sortOrder) {
            orderBy = TemperatureDbContract.ORDER_BY_DEVICE_IDX;
        } else {
            orderBy = sortOrder;
        }

        int match = mUriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case DEVICES:
                //query database for all temperature sensors
                c=db.query(TemperatureDbContract.TABLE_TEMPERATURE, projection, selection,
                        selectionArgs, null, null, orderBy);
                c.setNotificationUri(getContext().getContentResolver(), TempContentProvider.TEMPERATURE_URI);
                break;
            case DEV_ID:
                long tempID = ContentUris.parseId(uri);
                c=db.query(TemperatureDbContract.TABLE_TEMPERATURE, projection,
                        TemperatureDbContract._ID + " = " + tempID +
                                (!TextUtils.isEmpty(selection) ?
                                        " AND (" + selection +")" : ""),
                        selectionArgs, null, null, sortOrder);

                c.setNotificationUri(getContext().getContentResolver(), TempContentProvider.TEMPERATURE_URI);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: "+uri);
        }
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}