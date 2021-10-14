package net.ego.myhome;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import net.ego.myhome.adapters.TempPagerAdapter;
import net.ego.myhome.database.TemperatureDbContract;
import net.ego.myhome.fragment.DeviceFragment;
import net.ego.myhome.fragment.DeviceListFragment;
import net.ego.myhome.fragment.TempFragment;
import net.ego.myhome.interfaces.DeviceListener;
import net.ego.myhome.providers.TempContentProvider;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class TempDeviceActivity extends AppCompatActivity {
    public static final String TAG="TempDeviceActivity";
    public static String NUMBER_DEVICES="devnumber";
    private int mDevNumber=0;
    public List<TempFragment> fragList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_2);

/*
        if (savedInstanceState == null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById).commit();
            }
        }
*/
        fragList = new ArrayList<>();
        ContentResolver tempContentResolver = getApplicationContext().getContentResolver();
        Cursor cursor = getApplicationContext().getContentResolver().query(TempContentProvider.TEMPERATURE_URI,
                null,
                null,
                null,
                null);

        Log.d(TAG, "query returns "+cursor.getCount()+" entries");

//        StringBuilder strBuild = new StringBuilder();
        if (cursor.moveToFirst()) {
            Log.d(TAG, "Building fragments");
            while (!cursor.isAfterLast()) {
                Log.d(TAG, "Build fragment "+cursor.getPosition());
//                strBuild.append("\n" + cursor.getString(cursor.getColumnIndex(TemperatureDbContract._ID)) +
//                        "-" + cursor.getString(cursor.getColumnIndex(TemperatureDbContract.DEVICE_NAME)));
                Bundle bundle = new Bundle();
                bundle.putString(TemperatureDbContract.DEVICE_NAME, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.DEVICE_NAME)));
                bundle.putString(TemperatureDbContract.TEMPERATURE, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.TEMPERATURE)));
                bundle.putString(TemperatureDbContract.HUMIDITY, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.HUMIDITY)));
                bundle.putString(TemperatureDbContract.HUMIDITY_STATUS, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.HUMIDITY_STATUS)));
                bundle.putString(TemperatureDbContract.DEW_POINT, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.DEW_POINT)));
                bundle.putString(TemperatureDbContract.LAST_UPDATE, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.LAST_UPDATE)));
                bundle.putString(TemperatureDbContract.DEVICE_ID, cursor.getString(cursor.getColumnIndex(TemperatureDbContract.DEVICE_ID)));
//                Log.d(TAG, strBuild.toString());

                final TempFragment fragment = new TempFragment();
                fragment.setArguments(bundle);
                fragList.add(fragment);
/*
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .setCustomAnimations(R.animator.slide_in_right, 0, 0, R.animator.slide_out_left)
                        //.addToBackStack(null)
                        .commit();
*/
                cursor.moveToNext();
            }
        }
        else {
            Log.d(TAG, "No records found");
        }
        // viewpager to swipe between fragments
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);

        TempPagerAdapter adapter = new TempPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragList);

        viewPager.setAdapter(adapter);

        /*        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setReorderingAllowed(true);
        for(int i=0; i<mDevNumber; i++) {
            ft.add(R.id.container, new TempFragment());
        }
        ft.commit();
*/
    }
//TODO: Create list of fragments with temperature sensors to navigate within


    //@Override
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