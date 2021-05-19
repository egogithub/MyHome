package net.ego.myhome.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MyHomeContentProvider {

    public static final String DATABASE_NAME = "myhome.db";
    public static final String TEMP_TABLE_NAME = "temperature";
    public static final String SWITCH_TABLE_NAME = "switch";

    static int DATABASE_VERSION = 1;

    private static class MyHomeDbHelper extends SQLiteOpenHelper {

        private MyHomeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTempTable(db);
            createSwitchTable(db);
        }

        private void createTempTable(SQLiteDatabase db) {
            String qs = "CREATE TABLE " + TEMP_TABLE_NAME + "(" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    T
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
