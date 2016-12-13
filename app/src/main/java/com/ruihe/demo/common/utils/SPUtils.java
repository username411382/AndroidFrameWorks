package com.ruihe.demo.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ruihe.demo.MyApplication;

/**
 * description:
 * Created by ruihe on 2016/12/13
 */

public class SPUtils {

    private static SPUtils instance;
    private static final String SHAR_PREFRERENCE_NAME = "android_frame_work";
    public static final String CURRENT_USER_ID = "current_user_id";


    public static synchronized SPUtils getInstance() {

        if (instance == null) {
            instance = new SPUtils();
        }
        return instance;
    }

    private static SharedPreferences getSharePrefrerence() {
        return MyApplication.getInstance().getSharedPreferences(SHAR_PREFRERENCE_NAME, Context.MODE_PRIVATE);
    }


    public void putString(String key, String value) {
        getSharePrefrerence().edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return getSharePrefrerence().getString(key, defaultValue);
    }


}
