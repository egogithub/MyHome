package net.ego.myhome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import net.ego.myhome.fragment.DeviceFragment;
import net.ego.myhome.fragment.DeviceListFragment;
import net.ego.myhome.interfaces.DeviceListener;

public class DeviceList extends AppCompatActivity implements DeviceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById).commit();
            }
        }
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.container, new DeviceListFragment())
                .commit();

    }

    @Override
    public void onDeviceSelect(String name, String dev_id) {
        final DeviceFragment dFrag = new DeviceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("devicename", name);
        bundle.putString("value",dev_id);
        dFrag.setArguments(bundle);

/*
        getSupportFragmentManager().executePendingTransactions();
        Fragment fragmentById = getSupportFragmentManager()
                .findFragmentById(R.id.container);
        if (fragmentById != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragmentById).commit();
        }
*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, dFrag)
                .setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left)
                .addToBackStack(null)
                .commit();
    }
}