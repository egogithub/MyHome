package net.ego.myhome.async;

import android.os.AsyncTask;
import android.util.Log;

import net.ego.myhome.helpers.DomoticzHelper;
import net.ego.myhome.interfaces.TempDeviceListener;
import net.ego.myhome.pojo.TempDevice;

public class UpdateTempAsyncTask extends AsyncTask <String, Void, TempDevice> {
    TempDeviceListener mListener;

    public UpdateTempAsyncTask(TempDeviceListener listener) {
        mListener=listener;
    }

    @Override
    protected TempDevice doInBackground(String... ids) {
        if (isCancelled() == true)
            return null;
        return DomoticzHelper.getTempDeviceInfo(ids[0]);
    }

    @Override
    protected void onPostExecute(TempDevice tempDevice) {
        if (null==tempDevice)
            Log.d("UpdateTempAsyncTask", "tempDevice is null!");
        mListener.onTempDevice(tempDevice);
    }
}
