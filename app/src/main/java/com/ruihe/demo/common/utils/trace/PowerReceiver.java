package com.ruihe.demo.common.utils.trace;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerReceiver extends BroadcastReceiver {

    @SuppressLint("Wakelock")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();

        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            System.out.println("screen off,acquire wake lock!");
            if (null != TraceUtils.getInstance().mWakeLock && !(TraceUtils.getInstance().mWakeLock.isHeld())) {
                TraceUtils.getInstance().mWakeLock.acquire();
            }
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            System.out.println("screen on,release wake lock!");
            if (null != TraceUtils.getInstance().mWakeLock && TraceUtils.getInstance().mWakeLock.isHeld()) {
                TraceUtils.getInstance().mWakeLock.release();
            }
        }
    }

}
