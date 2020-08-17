package com.ruihe.demo.common.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by ruihe on 2016/4/11.
 */
public class ServiceDemo extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
