package com.ruihe.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.ToastUtil;
import com.zbar.lib.CaptureActivity;


/**
 * 扫码
 */
public class ActivityScanCode extends CaptureActivity implements View.OnClickListener {

    public static void redirectToActivity(Context context) {
        Intent intent = new Intent(context, ActivityScanCode.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private ImageView ivOpenLight;
    private ScaleAnimation mAnimation;

    @Override
    public int getViewId() {
        return R.layout.activity_scan;
    }

    @Override
    public void onActivityViewCreated() {
        super.onActivityViewCreated();
        initVariable();
        initView();
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
        mAnimation.cancel();
    }

    private void initVariable() {

    }

    private void initView() {
        mTitleView.setTitle("扫码");
        ImageView ivQrLineView = findViewById(R.id.capture_scan_line);
        RelativeLayout rlContainer = findViewById(R.id.capture_container);
        RelativeLayout rlCropLayout = findViewById(R.id.capture_crop_layout);

        mContainer = rlContainer;
        mCropLayout = rlCropLayout;
        mAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setDuration(1200);
        ivQrLineView.startAnimation(mAnimation);
        FrameLayout frameLayout = (FrameLayout) mTitleView.addRightDrawableMenu(this, R.drawable.ic_scan_flash_light_off,30
                ,30, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        light(ivOpenLight);
                    }
                });
        ivOpenLight = (ImageView) frameLayout.getChildAt(0);
    }


    @Override
    public void onScanSuccess(String result) {
        ToastUtil.show(result);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }


}
