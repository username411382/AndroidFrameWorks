package com.ruihe.demo.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.ActivitiesContainer;
import com.ruihe.demo.common.utils.view.TitleView;


/**
 * 所有活动的基类
 */
public abstract class BaseActivity extends FragmentActivity {


    public TitleView mTitleView;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getViewId());

        mTitleView = (TitleView) findViewById(R.id.common_title_view);
        onActivityViewCreated();
        // 启动activity时添加Activity到堆栈
        ActivitiesContainer.getInstance().addActivity(this);
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
        super.onDestroy();
    }


    public abstract int getViewId();

    public abstract void onActivityViewCreated();


}
