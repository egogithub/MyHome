package net.ego.myhome.async;

import android.os.AsyncTask;

import net.ego.myhome.helpers.DomoticzHelper;
import net.ego.myhome.interfaces.DomoVersionListener;
import net.ego.myhome.pojo.DomoticzInfo;

public class GetDomoVersionAsyncTask extends AsyncTask<Void, Void, DomoticzInfo> {
    private DomoVersionListener mListener;

    public GetDomoVersionAsyncTask(DomoVersionListener listener) {
        mListener = listener;
    }

    @Override
    protected DomoticzInfo doInBackground(Void... params) {
        return DomoticzHelper.getDomoticzVersion();
    }

    @Override
    protected void onPostExecute(DomoticzInfo domoticzInfo) {
//        if (null != domoticzInfo) {
            mListener.onDomoticzVersion(domoticzInfo);
//        }
    }


}
