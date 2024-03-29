package net.ego.myhome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.ego.myhome.async.GetDeviceListAsyncTask;
import net.ego.myhome.async.GetDomoVersionAsyncTask;
import net.ego.myhome.async.GetTempDeviceListAsyncTask;
import net.ego.myhome.database.TemperatureDbContract;
import net.ego.myhome.interfaces.DomoDeviceListListener;
import net.ego.myhome.interfaces.DomoVersionListener;
import net.ego.myhome.interfaces.TempDeviceListListener;
import net.ego.myhome.pojo.DmDevice;
import net.ego.myhome.pojo.DomoticzInfo;
import net.ego.myhome.pojo.TempDevice;
import net.ego.myhome.providers.DevicesProvider;
import net.ego.myhome.providers.TempContentProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DomoVersionListener, View.OnClickListener, DomoDeviceListListener{
    private static final String TAG="MainActivity";
    private GetDomoVersionAsyncTask mVersionAsyncTask;
    private GetDeviceListAsyncTask mDeviceListAsyncTask;
    private GetTempDeviceListAsyncTask mTempDeviceAsyncTask;
    private static boolean connected = false;
    private Button btnMain;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String domo_ip = prefs.getString("domo_ip", null);
        TextView statusText = (TextView)findViewById(R.id.textStatus);
        if (null == domo_ip) {
            Log.d(TAG, "ip address is not defined");
            statusText.setText("ip address is not defined");
            return;
        }
        Log.d(TAG, "ip address = " + domo_ip);

        String domo_port = prefs.getString("domo_port", null);
        if (null == domo_port) {
            Log.d(TAG, "port number not defined");
            statusText.setText("port number is not defined");
            return;
        }
        Log.d(TAG, "port number = " + domo_port);

         */
        btnMain = (Button)findViewById(R.id.btnMain);
        getVersion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void getVersion() {
        Log.d(TAG, "Getting version from server");
        btnMain.setEnabled(false);
        //btnMain.setVisibility(View.INVISIBLE);
        TextView statusText = (TextView)findViewById(R.id.textStatus);
        statusText.setText("Connecting ...");
        mVersionAsyncTask = new GetDomoVersionAsyncTask(this);
        mVersionAsyncTask.execute();
    }

    private void getDeviceList() {
        Log.d(TAG,"Getting device list");
        mDeviceListAsyncTask = new GetDeviceListAsyncTask(this);
        mDeviceListAsyncTask.execute();
    }

    private void getTempDevices() {
        Log.d(TAG,"Getting temperature devices");
        mTempDeviceAsyncTask = new GetTempDeviceListAsyncTask(new TempDeviceListListener() {
            @Override
            public void onTempDeviceList(List<TempDevice> tempDeviceList) {
                Log.d(TAG, "Got Temperature Devices list with "+((null==tempDeviceList)?0:tempDeviceList.size())+" devices" );
                getApplicationContext().getContentResolver().delete(TempContentProvider.TEMPERATURE_URI,null,null);
                for (TempDevice tDevice : tempDeviceList) {
                    ContentValues values = new ContentValues();
                    values.put(TempContentProvider.DEVICE_IDX, tDevice.idx);
                    values.put(TempContentProvider.DEVICE_TYPE, tDevice.Type);
                    values.put(TempContentProvider.DEVICE_NAME, tDevice.Name);
                    values.put(TempContentProvider.DEVICE_DATA, tDevice.Data);
                    values.put(TempContentProvider.DEW_POINT, tDevice.DewPoint);
                    values.put(TempContentProvider.HUMIDITY_STATUS, tDevice.HumidityStatus);
                    values.put(TempContentProvider.LAST_UPDATE, tDevice.LastUpdate);
                    values.put(TempContentProvider.DEVICE_ID, tDevice.ID);
                    values.put(TempContentProvider.HUMIDITY, tDevice.Humidity);
                    values.put(TempContentProvider.TEMPERATURE, tDevice.Temp);
                    values.put(TempContentProvider.FAVORITE, tDevice.Favorite);
                    values.put(TempContentProvider.DEVICE_SUBTYPE, tDevice.SubType);
                    values.put(TempContentProvider.TYPE_IMG, tDevice.TypeImg);
                    Uri uri = getContentResolver().insert(TempContentProvider.TEMPERATURE_URI, values);
                    Log.d(TAG, "URI to insert is "+uri.toString());

                }
                Bundle b = new Bundle();
                b.putInt(TempDeviceActivity.NUMBER_DEVICES, tempDeviceList.size());
                final Intent intent = new Intent(MainActivity.this, TempDeviceActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        mTempDeviceAsyncTask.execute();
    }

    @Override
    public void onDomoticzVersion(DomoticzInfo info) {
        btnMain.setEnabled(true);
        if (null == info) {
            Log.d(TAG,"Could not contact Server to get version");
            TextView statusText = (TextView)findViewById(R.id.textStatus);
            statusText.setText("Connection error");
            btnMain.setText("RETRY");
            connected=false;
            //btnMain.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "Version = "+info.version);
            TextView statusText = (TextView)findViewById(R.id.textStatus);
            statusText.setText("Connected");
            TextView versionText = (TextView)findViewById(R.id.textVersion);
            versionText.setText("Domoticz version "+info.version);
            //btnMain.setEnabled(true);
            btnMain.setText("NEXT");
            // Get devices list from domoticz server

            //getDeviceList();
            connected=true;
            //btnMain.setVisibility(View.VISIBLE);
            //Now, get the devices info (populate database)
        }
    }

    @Override
    public void onClick(View v) {
        if (connected) {
            // Go to the next screen - Clicked on Next
            /*
            //Getting list of devices
            String[] mProjection = {
                    "_id",
                    DevicesProvider.NAME,
                    DevicesProvider.VALUE,
            };

            mCursor = getContentResolver().query(DevicesProvider.CONTENT_URI, mProjection, null, null, null);
            //For every device, get device information

            */
            //final Intent intent =  new Intent(this, DeviceList.class);
            //startActivity(intent);
            getTempDevices();
        } else {
            //Clicked on Retry
            Log.d(TAG, "Clicked on retry");
            getVersion();
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Back from settings");
        if (requestCode == SETTINGS_RESULT) {
            getVersion();
        }
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "in onResume");
        //getVersion();
    }

    @Override
    public void onDevicesList(List<DmDevice> deviceList) {
        Log.d(TAG, "Got Devices list with "+((null==deviceList)?0:deviceList.size())+" devices" );
        //try to clear database
        getApplicationContext().getContentResolver().delete(DevicesProvider.CONTENT_URI,null,null);
        //fill the database with new values
        for (DmDevice dmDevice :
             deviceList) {
            ContentValues values = new ContentValues();
            values.put(DevicesProvider.NAME, dmDevice.name);
            values.put(DevicesProvider.VALUE, dmDevice.value);
            Uri uri = getContentResolver().insert(DevicesProvider.CONTENT_URI, values);
            Log.d(TAG, "URI to insert is "+uri.toString());
        }
    }
}