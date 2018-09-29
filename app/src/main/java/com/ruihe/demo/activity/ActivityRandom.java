package com.ruihe.demo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihe.demo.R;
import com.ruihe.demo.adapter.AdapterRandomPager;
import com.ruihe.demo.common.ActivitiesContainer;
import com.ruihe.demo.common.utils.DensityUtil;
import com.ruihe.demo.common.utils.DialogUtils;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import qiu.niorgai.StatusBarCompat;

/**
 * 随机事件
 * Created by RH on 2018/9/13
 */
public class ActivityRandom extends BaseActivity {


    @BindView(R.id.tab_random_event)
    TabLayout tabRandom;
    @BindView(R.id.vp_random_event)
    ViewPager vpRandom;

    private Activity mActivity;
    private boolean isEnableExit;

    public static void redirectToActivity(Context context) {
        Intent intent = new Intent(context, ActivityRandom.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getViewId() {
        return R.layout.activity_random;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityViewCreated() {
        mActivity = this;
        setUnBinder(ButterKnife.bind(this));

        AdapterRandomPager adapterRandomPager = new AdapterRandomPager(getSupportFragmentManager());
        vpRandom.setAdapter(adapterRandomPager);
        tabRandom.setupWithViewPager(vpRandom);
        setTabIndicatorWidth(tabRandom);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.black1));
    }

    /**
     * 设置tab宽度为文字宽度
     */
    private void setTabIndicatorWidth(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    int widthRemain = DensityUtil.getScreenWidth(mActivity) - DensityUtil.dip2px(mActivity, 10 + 10);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //字多宽线就多宽，所以测量mTextView的宽度
                        int width;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //先拿到TabView的宽度减去TextView宽度除以2，得到TabView的左右margin,因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = (widthRemain - width * 2) / 4;
                        params.rightMargin = (widthRemain - width * 2) / 4;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isEnableExit) {
            ActivitiesContainer.getInstance().finishAllActivities();
            return;
        }
        super.onBackPressed();
    }

    public void setExit(boolean isEnableExit) {
        this.isEnableExit = isEnableExit;
    }
}
