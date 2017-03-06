package com.ruihe.demo.activity;

import android.support.v4.view.ViewPager;

import com.ruihe.demo.R;
import com.ruihe.demo.adapter.AdapterReuseViewPager;

/**
 * description:拖动控件
 * Created by ruihe on 2016/12/20
 */

public class ActivityDragView extends BaseActivity {


    private ViewPager viewPager;


    @Override
    public int getViewId() {
        return R.layout.activity_drag_view;
    }

    @Override
    public void onActivityViewCreated() {

        viewPager = (ViewPager) findViewById(R.id.vp);
        new AdapterReuseViewPager(getSupportFragmentManager(), viewPager);
    }


    private String getCalculateResult() {


        String result = "计算结果: " + (3 / 2 + 5 / 2);

        return result;
    }

}
