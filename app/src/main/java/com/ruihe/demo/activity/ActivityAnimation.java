package com.ruihe.demo.activity;

import com.ruihe.demo.R;

/**
 * Created by ruihe on 2017/1/16.
 */

public class ActivityAnimation extends BaseActivity {



    @Override
    public int getViewId() {
        return R.layout.activity_animation;
    }

    @Override
    public void onActivityViewCreated() {
        initVariable();
        initView();
        bindData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initVariable() {
    }

    private void initView() {
    }

    private void bindData() {
    }


}
