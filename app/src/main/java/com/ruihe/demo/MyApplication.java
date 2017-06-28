package com.ruihe.demo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.filedownloader.FileDownloader;
import com.ruihe.demo.common.chat.util.RongIMUtil;

/**
 * 描述：全局的Application
 * Created by ruihe on 2016/4/28.
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private RequestQueue mQueue;


    public static synchronized MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mQueue = Volley.newRequestQueue(this);

        //仅仅是缓存Application的Context，不耗时
        FileDownloader.init(getApplicationContext());

        //融云初始化
        RongIMUtil.initRongIMClient(getApplicationContext());

        Fresco.initialize(this);

    }
}
