package net.ego.myhome;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.worldline.spica.helpers.services.SpicaServiceConnectionCallback;
import com.worldline.spica.msmd.MsmdService;
import com.worldline.spica.msmd.MsmdServiceProvider;

import androidx.annotation.Nullable;

public class MyHomeStartService extends Service {
    private final String TAG = "MyHomeStartService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MsmdServiceProvider msmdServiceProvider = new MsmdServiceProvider(this);
        msmdServiceProvider.connect(connectionCallback);
        return START_NOT_STICKY;
    }

    private final SpicaServiceConnectionCallback <MsmdService> connectionCallback = new SpicaServiceConnectionCallback<MsmdService>() {
        @Override
        public void onConnect(MsmdService msmdService) {
            Log.i(TAG, "MSMD connected");
            Intent intent = new Intent(MyHomeStartService.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @Override
        public void onDisconnect() {
            Log.i(TAG, "MSMD disconnected");
        }
    };
}
