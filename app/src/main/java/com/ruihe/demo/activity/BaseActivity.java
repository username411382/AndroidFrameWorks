package com.ruihe.demo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ruihe.demo.MyApplication;
import com.ruihe.demo.R;
import com.ruihe.demo.common.ActivitiesContainer;
import com.ruihe.demo.common.view.TitleView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import qiu.niorgai.StatusBarCompat;


/**
 * 所有活动的基类
 */
public abstract class BaseActivity extends FragmentActivity {


    public static final String INTENT_BUNDLE_EXTRA = "intent_bundle";
    public TitleView mTitleView;
    private RxPermissions mRxPermissions;
    private Unbinder mUnbinder;
    protected BaseActivity holder;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getViewId());
        holder = this;
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


    @SuppressLint("CheckResult")
    protected void requestStorePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getRxPermissions().requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {

                    if (!permission.granted) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("请开启存储权限");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", MyApplication.getInstance().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.create().show();
                    }
                }
            });
        }
    }

}
