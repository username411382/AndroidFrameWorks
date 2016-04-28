package com.ruihe.demo.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ruihe.demo.common.utils.ActivitiesContainer;


/**
 * 所有活动的基类
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

}
