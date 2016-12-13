package com.ruihe.demo.common;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihe.demo.MyApplication;
import com.ruihe.demo.R;

/**
 * 描述：弹窗
 * Created by ruihe on 2016/5/4.
 */
public class ToastUtil {

    private static long lastShowTime;
    private static Toast toast;
    private static int duration = 2000;

    @SuppressLint("ShowToast")
    public static Toast getToast() {
        if (toast == null) {
            toast = new Toast(MyApplication.getInstance());
            toast.setView(View.inflate(MyApplication.getInstance(), R.layout.toast_custom, null));
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        return toast;
    }

    public static void show(String content) {
        if (toast != null && System.currentTimeMillis() - lastShowTime < duration) {
            toast.cancel();
            toast = null;
        }
        ((TextView) getToast().getView().findViewById(R.id.tv_toast)).setText(content);
        getToast().show();
        lastShowTime = System.currentTimeMillis();
    }

    public static void show(int resId) {
        if (toast != null && System.currentTimeMillis() - lastShowTime < duration) {
            toast.cancel();
            toast = null;
        }
        ((TextView) getToast().getView().findViewById(R.id.tv_toast)).setText(resId);
        getToast().show();
        lastShowTime = System.currentTimeMillis();
    }

    public static void cancel() {
        if (toast != null && System.currentTimeMillis() - lastShowTime < duration) {
            toast.cancel();
        }
    }

}
