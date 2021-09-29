package net.ego.myhome.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ego.myhome.R;

public class DeviceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "DeviceFragment";
    private static final String ARG_NAME = "devicename";
    private static final String ARG_VALUE = "value";

    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_device, container, false);
        Bundle bundle = getArguments();
        if (bundle!=null) {
            String name = bundle.getString(ARG_NAME);
            String value =bundle.getString(ARG_VALUE);
            ((TextView)view.findViewById(R.id.sensor_name)).setText(name);
            ((TextView)view.findViewById(R.id.sensor_id)).setText(value);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}