package com.ruihe.demo.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 版本更新相关功能
 */
public class VersionUtils {

    private static final String SP_LAST_REMAIND_UPDATE_DATE = "SP_LAST_REMAIND_UPDATE_DATE";

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

   /* public static void checkVersion(final Activity activity, final boolean isForce){
        if (!isForce){
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(d);
            if (currentDate.equals(SPUtils.getInstance().getString(SP_LAST_REMAIND_UPDATE_DATE, ""))){
                return;
            } else {
                SPUtils.getInstance().putString(SP_LAST_REMAIND_UPDATE_DATE, currentDate);
            }
        }
        checkVersion(activity, new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        ActivityDialogUpdate.redirectToActivity(activity, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        if (isForce){
                            ToastUtils.show("没有检测到新版本");
                        }
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        //ToastUtils.show("没有wifi连接， 只在wifi下更新");
                        break;
                    case UpdateStatus.Timeout: // time out
                        //ToastUtils.show("超时");
                        break;
                }
            }
        });
    }

    public static void checkVersion(Context context, UmengUpdateListener listener){
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(listener);
        UmengUpdateAgent.silentUpdate(context);
    }*/


}
