package com.ruihe.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ruihe.demo.common.utils.ToastUtil;
import com.zxing.CaptureActivity;


/**
 * 扫码
 */
public class ActivityScanCode extends CaptureActivity implements View.OnClickListener {

    public static void redirectToActivity(Context context) {
        Intent intent = new Intent(context, ActivityScanCode.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void handleResult(String resultString) {
        ToastUtil.show(resultString);
        restartPreview();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


}
