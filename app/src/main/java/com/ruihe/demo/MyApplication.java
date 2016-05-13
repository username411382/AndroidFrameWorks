package com.ruihe.demo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.liulishuo.filedownloader.FileDownloader;

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

        /**
         * 仅仅是缓存Application的Context，不耗时
         */
        FileDownloader.init(getApplicationContext());

        SDKInitializer.initialize(getApplicationContext());

    }
}
