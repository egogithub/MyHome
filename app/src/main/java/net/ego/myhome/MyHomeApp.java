package net.ego.myhome;

import android.app.Application;
import android.content.Context;

import be.banksys.maps.sync.EventLoop;

public class MyHomeApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        EventLoop.start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventLoop.stop();
    }

    public static Context getContext() {
        return mContext;
    }
}
