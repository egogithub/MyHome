package net.ego.myhome.async;

import android.os.AsyncTask;
import android.util.Log;

import net.ego.myhome.helpers.DomoticzHelper;
import net.ego.myhome.interfaces.TempDeviceListListener;
import net.ego.myhome.pojo.TempDevice;

import java.util.List;

public class GetTempDeviceListAsyncTask extends AsyncTask <Void, Void, List<TempDevice>> {
    private static final String TAG = "TempDeviceListAsyncTask";
    TempDeviceListListener mListener;

    public GetTempDeviceListAsyncTask (TempDeviceListListener lsnr) {
        mListener=lsnr;
    }

    @Override
    protected List<TempDevice> doInBackground(Void... voids) {
        return DomoticzHelper.getTempDevices();
    }

    @Override
    protected void onPostExecute(List<TempDevice> tempDeviceList) {
        Log.d(TAG, "onPostExecute");
        mListener.onTempDeviceList(tempDeviceList);
    }
}
