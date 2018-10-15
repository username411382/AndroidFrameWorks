package com.ruihe.demo.activity;


import android.support.v7.widget.RecyclerView;

import com.ruihe.demo.R;

/**
 * description:自定义View
 * Created by ruihe on 2016/12/20
 */

public class ActivityShowCustomView extends BaseActivity {





    @Override
    public int getViewId() {
        return R.layout.activity_show_custom_view;
    }

    @Override
    public void onActivityViewCreated() {

        RecyclerView recyclerView = findViewById(R.id.rcl_view);



    }




}
