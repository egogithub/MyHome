package net.ego.myhome.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import net.ego.myhome.R;
import net.ego.myhome.adapters.DevicesCursorAdapter;
import net.ego.myhome.interfaces.DeviceListener;
import net.ego.myhome.pojo.Device;
import net.ego.myhome.pojo.DmDevice;
import net.ego.myhome.providers.DevicesProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DeviceListFragment extends Fragment {
    private final static String TAG = "DeviceListFragment";
    private ListView mListView;
    private DevicesCursorAdapter mCursorAdapter;
    private Cursor mCursor;
    private DeviceListener mDeviceListener;

    public DeviceListFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof DeviceListener) {
            Log.d(TAG, "instance of DeviceListener");
            mDeviceListener = (DeviceListener) getActivity();
        } else {
            Log.d(TAG, "not instance of DeviceListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_devicelist, container, false);

        mListView = (ListView)rootView.findViewById(R.id.deviceListView);

        String[] mProjection = {
                "_id",
                DevicesProvider.NAME,
                DevicesProvider.VALUE,
        };

        mCursor = getActivity().getContentResolver().query(DevicesProvider.CONTENT_URI, mProjection, null, null, null);
        mCursorAdapter = new DevicesCursorAdapter(getActivity(), mCursor, 0);
        mListView.setAdapter(mCursorAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //final DmDevice dev = (DmDevice) adapterView.getItemAtPosition(position);
                Log.d(TAG, "clicked at position "+position + ", id "+id);
                mCursor.moveToPosition(position);
                String name = mCursor.getString(1);
                String value = mCursor.getString(2);
                Log.d(TAG, "Name = " + name+", value = "+value);
                if (null != mDeviceListener) {
                    mDeviceListener.onDeviceSelect(name, value);
                }
            }
        });

        return rootView;
    }
}
