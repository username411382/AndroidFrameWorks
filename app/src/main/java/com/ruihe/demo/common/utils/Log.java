package com.ruihe.demo.common.utils;

import com.ruihe.demo.BuildConfig;

/**
 * Function：日志
 * Author：rui.he
 * Date：2020/8/17 15:57
 */
public class Log {

    private static final String TAG = "AndroidFrameWorks";

    /**
     * 分段输出超过logcat最大长度的log
     */
    public static void logLongInfo(String str) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        if (str.length() > 4000) {
            android.util.Log.i(TAG, str.substring(0, 4000));
            logLongInfo(str.substring(4000));
        } else {
            android.util.Log.i(TAG, str);
        }
    }
}
