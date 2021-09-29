package net.ego.myhome.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class TemperatureDbContract implements BaseColumns {
    public static final String BATTERY_LEVEL = "BatteryLevel"; //INTEGER
    public static final String DEVICE_DATA = "Data"; //TEXT
    public static final String DEW_POINT = "DewPoint"; //TEXT
    public static final String FAVORITE = "Favorite"; //INTEGER
    public static final String HUMIDITY = "Humidity"; //INTEGER
    public static final String HUMIDITY_STATUS = "HumidityStatus"; //TEXT
    public static final String DEVICE_ID = "ID"; //TEXT
    public static final String LAST_UPDATE = "LastUpdate"; //TEXT
    public static final String DEVICE_NAME = "Name"; //TEXT
    public static final String DEVICE_SUBTYPE = "SubType"; //TEXT
    public static final String TEMPERATURE = "Temp"; //INTEGER
    public static final String DEVICE_TYPE = "Type"; //TEXT
    public static final String TYPE_IMG = "TypeImg"; //TEXT
    public static final String DEVICE_IDX = "idx"; //TEXT
    //public static final String HARDWARE_TYPE_VAL = "HardwareTypeVal"; //INTEGER
    //public static final String HARDWARE_ID = "HardwareID"; //INTEGER

    // Table name
    public static final String TABLE_TEMPERATURE = "temperature";

    //Table script creation
    private static final String TABLE_GENERIC_CREATE_SCRIPT_PREFIX = "CREATE TABLE IF NOT EXISTS ";
    private static final String TABLE_TEMPERATURE_CREATE_SCRIPT_SUFFIX = "(" + _ID + " INTEGER PRIMARY KEY, " +
            BATTERY_LEVEL + " TEXT, " +
            DEVICE_DATA + " TEXT, " +
            DEW_POINT + " TEXT, " +
            FAVORITE + " INTEGER, " +
            HUMIDITY + " INTEGER, " +
            HUMIDITY_STATUS + " TEXT NOT NULL, " +
            DEVICE_ID + " TEXT NOT NULL, " +
            LAST_UPDATE + " TEXT NOT NULL, " +
            DEVICE_NAME + " TEXT NOT NULL, " +
            DEVICE_SUBTYPE + " TEXT NOT NULL, " +
            TEMPERATURE + " INTEGER, " +
            DEVICE_TYPE + " TEXT NOT NULL, " +
            TYPE_IMG + " TEXT NOT NULL, " +
            DEVICE_IDX + " TEXT NOT NULL)";

    public static final String TABLE_TEMPERATURE_CREAT_SCRIPT = TABLE_GENERIC_CREATE_SCRIPT_PREFIX +
            TABLE_TEMPERATURE + TABLE_TEMPERATURE_CREATE_SCRIPT_SUFFIX;

    // The projections
    public static final String[] PROJECTION_FULL = new String[] {
            _ID,
            BATTERY_LEVEL,
            DEVICE_DATA ,
            DEW_POINT,
            FAVORITE,
            HUMIDITY,
            HUMIDITY_STATUS,
            DEVICE_ID,
            LAST_UPDATE,
            DEVICE_NAME,
            DEVICE_SUBTYPE,
            TEMPERATURE,
            DEVICE_TYPE,
            TYPE_IMG,
            DEVICE_IDX,
    };

    //Selections
    public static final String SELECTION_BY_ID = _ID + "=?";
    public static final String SELECTION_BY_IDX = DEVICE_IDX + "=?";

    //Sort Order
    public static final String ORDER_BY_DEVICE_IDX = DEVICE_IDX + " DESC";

    //Content Provider stuff
    public static final String CONTENT_PROVIDER_TEMPERATURE_AUTHORITY = "net.ego.myhome.TempAuthority";
    public static final Uri TEMPERATURE_URI = Uri.parse(("content://"+CONTENT_PROVIDER_TEMPERATURE_AUTHORITY + "/" + TABLE_TEMPERATURE));
    public static final String TEMPERATURE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.myhome.tempdevices";
}
