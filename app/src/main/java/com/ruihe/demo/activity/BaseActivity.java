package com.ruihe.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ruihe.demo.R;
import com.ruihe.demo.common.ActivitiesContainer;
import com.ruihe.demo.common.view.TitleView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import qiu.niorgai.StatusBarCompat;


/**
 * 所有活动的基类
 */
public abstract class BaseActivity extends FragmentActivity {


    public static final String INTENT_BUNDLE_EXTRA = "intent_bundle";
    public TitleView mTitleView;
    private RxPermissions mRxPermissions;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getViewId());
        mTitleView = (TitleView) findViewById(R.id.common_title_view);
        mRxPermissions = new RxPermissions(this);
        onActivityViewCreated();
        // 启动activity时添加Activity到堆栈
        ActivitiesContainer.getInstance().addActivity(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorLightBlue));

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        // 结束Activity&从栈中移除该Activity
        ActivitiesContainer.getInstance().removeActivity(this);
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }


    public void redirectToActivity(Context context, Class<? extends Activity> targetActivity) {
        redirectToActivity(context, null, targetActivity);
    }

    public void redirectToActivity(Context context, Bundle bundle, Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra(INTENT_BUNDLE_EXTRA, bundle);
        }
        startActivity(intent);
    }


    public abstract int getViewId();

    public abstract void onActivityViewCreated();

    /**
     * 获取权限请求类
     */
    protected RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    protected void setUnBinder(Unbinder unBinder) {
        mUnbinder = unBinder;
    }


}
