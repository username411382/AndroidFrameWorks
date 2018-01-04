package com.ruihe.demo.common.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * description:日志工具类
 * Created by ruihe on 2017/3/6
 */

public class LogUtil {

    private static final String TAG = "frameworkInfo";


    public static void d(String printValue) {
        d(null, printValue);
    }

    public static void d(String tag, String printValue) {
        Log.d(TextUtils.isEmpty(tag) ? TAG : tag, printValue);
    }

}
