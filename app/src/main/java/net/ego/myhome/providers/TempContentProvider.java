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

    static final String _ID = "_id";
    static final int DEVICES = 1;
    static final int DEVICE_ID = 2;

    //The URI matcher to check if URI is correct
    static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(TemperatureDbContract.CONTENT_PROVIDER_TEMPERATURE_AUTHORITY, "devices", DEVICES);
        mUriMatcher.addURI(TemperatureDbContract.CONTENT_PROVIDER_TEMPERATURE_AUTHORITY, "devices/#", DEVICE_ID);
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
            case DEVICE_ID:
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
                return TemperatureDbContract.TEMPERATURE_CONTENT_TYPE;
            case DEVICE_ID:
                return TemperatureDbContract.TEMPERATURE_CONTENT_TYPE;
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
                        final Uri _uri = ContentUris.withAppendedId(TemperatureDbContract.TEMPERATURE_URI, rowId);
                        getContext().getContentResolver().notifyChange(_uri, null);
                        return _uri;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mLock.unlock();
                }
                break;
            case DEVICE_ID:
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
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}