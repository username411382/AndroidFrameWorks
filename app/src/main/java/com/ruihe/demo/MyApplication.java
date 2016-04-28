package com.ruihe.demo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 描述：全局的Application
 * Created by ruihe on 2016/4/28.
 */
public class MyApplication extends Application {

    private static MyApplication instance ;
    private RequestQueue mQueue;


    public static synchronized MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mQueue = Volley.newRequestQueue(this);
    }
}
