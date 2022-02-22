package net.ego.myhome.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import net.ego.myhome.R;
import net.ego.myhome.async.UpdateTempAsyncTask;
import net.ego.myhome.database.TemperatureDbContract;
import net.ego.myhome.interfaces.TempDeviceListener;
import net.ego.myhome.pojo.TempDevice;

public class TempFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "TempFragment";
    private static final String ARG_NAME = "devicename";
    private static final String ARG_VALUE = "value";
    UpdateTempAsyncTask mUpdateTempAsyncTask;
    int updateTimer = 10000;
    private Handler timerHandler = new Handler();
    private String deviceID;

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_temp_device, container, false);
        Bundle bundle = getArguments();
        if (bundle!=null) {
            String name = bundle.getString(TemperatureDbContract.DEVICE_NAME);
            ((TextView)view.findViewById(R.id.sensor_name)).setText(name);

            String temp = bundle.getString(TemperatureDbContract.TEMPERATURE)+"°C";
            ((TextView)view.findViewById(R.id.temp)).setText(temp);

            String hum = bundle.getString(TemperatureDbContract.HUMIDITY)+"%";
            ((TextView)view.findViewById(R.id.humid)).setText(hum);

            String humStatus = bundle.getString(TemperatureDbContract.HUMIDITY_STATUS);
            ((TextView)view.findViewById(R.id.humidity_status)).setText(humStatus);

            String dewPoint = "Dew point: "+bundle.getString(TemperatureDbContract.DEW_POINT)+"°C";
            ((TextView)view.findViewById(R.id.dew_point)).setText(dewPoint);

            String lastUpdate = bundle.getString(TemperatureDbContract.LAST_UPDATE);
            ((TextView)view.findViewById(R.id.last_update)).setText(lastUpdate);

            String id = "id: "+bundle.getString(TemperatureDbContract.DEVICE_ID);
            ((TextView)view.findViewById(R.id.sensor_id)).setText(id);
            deviceID = bundle.getString(TemperatureDbContract.DEVICE_IDX);

        }

        //mView=view;
        return view;
    }

    private Runnable refreshTask = new Runnable() {
        @Override
        public void run() {
            //String value =bundle.getString(ARG_VALUE);
            //((TextView)view.findViewById(R.id.sensor_id)).setText(value);
            Log.d(TAG, "Refreshing temperature");
            mUpdateTempAsyncTask = new UpdateTempAsyncTask(new TempDeviceListener() {
                View view = getView();
                @Override
                public void onTempDevice(TempDevice tempDevice) {
                    if (null != tempDevice) {
                        Log.d(TAG, "Updated device "+tempDevice.Name);
                        ((TextView)view.findViewById(R.id.sensor_name)).setText(tempDevice.Name);
                        ((TextView)view.findViewById(R.id.temp)).setText(tempDevice.Temp+"°C");
                        ((TextView)view.findViewById(R.id.humid)).setText(tempDevice.Humidity+"%");
                        ((TextView)view.findViewById(R.id.humidity_status)).setText(tempDevice.HumidityStatus);
                        ((TextView)view.findViewById(R.id.dew_point)).setText(tempDevice.DewPoint+"°C");
                        ((TextView)view.findViewById(R.id.last_update)).setText(tempDevice.LastUpdate);
                        ((TextView)view.findViewById(R.id.sensor_id)).setText(tempDevice.ID);

                    } else {
                        Log.d(TAG, "Error updating device "+deviceID);
                    }
                    timerHandler.postDelayed(refreshTask, updateTimer);
                }
            });
            mUpdateTempAsyncTask.execute(deviceID);
        }
    };



/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_temp_device, container, false);
    }
*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated calld");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        timerHandler.postDelayed(refreshTask, updateTimer);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        mUpdateTempAsyncTask.cancel(true);
        //timerHandler.
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
        //mUpdateTempAsyncTask.cancel(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
