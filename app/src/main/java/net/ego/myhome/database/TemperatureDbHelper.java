package net.ego.myhome.database;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class TemperatureDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "temperature.db";

    // Mandatory constructor
/*
    private TemperatureDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DATABASE_VERSION);
    }
*/


    public TemperatureDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Re-create database at every start
        db.execSQL("DROP TABLE IF EXISTS " + TemperatureDbContract.TABLE_TEMPERATURE + ";");
        createTable(db);
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL(TemperatureDbContract.TABLE_TEMPERATURE_CREAT_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Anyway we drop and create database at each startup
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TemperatureDbContract.TABLE_TEMPERATURE + ";");
        createTable(sqLiteDatabase);
    }

    public static class TempCursor extends SQLiteCursor {

        private TempCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(driver, editTable, query);
        }
    }

}
