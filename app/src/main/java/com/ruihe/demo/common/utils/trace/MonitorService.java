package com.ruihe.demo.common.utils.trace;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;

import java.util.List;

public class MonitorService extends Service {

    protected static boolean isCheck = false;

    protected static boolean isRunning = false;

    private static final String SERVICE_NAME = "com.baidu.trace.LBSTraceService";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread() {

            @Override
            public void run() {

                while (isCheck) {
                    try {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("thread sleep failed");
                    }

                    if (!isServiceWork(getApplicationContext(), SERVICE_NAME)) {
                        System.out.println("轨迹服务已停止，重启轨迹服务");
                        if (null != TraceUtils.getInstance().mClient && null != TraceUtils.getInstance().trace) {
                            TraceUtils.getInstance().mClient.startTrace(TraceUtils.getInstance().trace);
                        } else {
                            TraceUtils.getInstance().mClient = null;
                            TraceUtils.getInstance().mClient = new LBSTraceClient(getApplicationContext());
                            TraceUtils.getInstance().mEntityName = TraceUtils.getInstance().getImei(getApplicationContext());
                            TraceUtils.getInstance().trace = new Trace(getApplicationContext(), TraceUtils.getInstance().SERVICE_ID,
                                    TraceUtils.getInstance().mEntityName);
                            TraceUtils.getInstance().mClient.startTrace(TraceUtils.getInstance().trace);
                        }
                    } else {
                        System.out.println("轨迹服务正在运行");
                    }

                }
            }

        }.start();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：com.baidu.trace.LBSTraceService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> myList = myAM.getRunningServices(80);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            System.out.println("serviceName : " + mName);
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
