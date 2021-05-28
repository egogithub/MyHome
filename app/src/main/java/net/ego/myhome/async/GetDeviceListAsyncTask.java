package net.ego.myhome.async;

import android.os.AsyncTask;
import android.util.Log;

import net.ego.myhome.helpers.DomoticzHelper;
import net.ego.myhome.interfaces.DomoDeviceListListener;
import net.ego.myhome.pojo.DmDevice;

import java.util.List;

public class GetDeviceListAsyncTask extends AsyncTask <Void, Void, List<DmDevice>> {
    private static final String TAG = "GetDeviceListAsyncTask";
    DomoDeviceListListener mListener;

    public GetDeviceListAsyncTask(DomoDeviceListListener listener) {
        mListener = listener;
    }


    @Override
    protected List<DmDevice> doInBackground(Void... voids) {
        Log.d(TAG, "in doInBackground");
        return DomoticzHelper.getDeviceList();
    }

    @Override
    protected void onPostExecute(List<DmDevice> deviceList) {
        Log.d(TAG, "in onPostExecute");
        mListener.onDevicesList(deviceList);
    }

}
