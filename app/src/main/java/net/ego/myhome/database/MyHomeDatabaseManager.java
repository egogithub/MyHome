package net.ego.myhome.database;

import android.database.Cursor;

import net.ego.myhome.pojo.DmDevice;
import net.ego.myhome.providers.DevicesProvider;

public class MyHomeDatabaseManager {
    public static DmDevice getDeviceAtCursor(Cursor c) {
        if (null != c) {
            final DmDevice dmDevice = new DmDevice();

            if (c.getColumnIndex(DevicesProvider.NAME) >= 0) {
                dmDevice.name = c.getString(c.getColumnIndex(DevicesProvider.NAME));
            }

            if (c.getColumnIndex(DevicesProvider.VALUE) >= 0) {
                dmDevice.value = c.getString(c.getColumnIndex(DevicesProvider.VALUE));
            }
            return dmDevice;
        }
        return null;
    }
}
