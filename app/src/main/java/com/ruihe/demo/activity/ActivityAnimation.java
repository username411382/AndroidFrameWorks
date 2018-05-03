package com.ruihe.demo.activity;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.ruihe.demo.R;
import com.ruihe.demo.adapter.AdapterItemLeftIn;

/**
 * Created by ruihe on 2017/1/16.
 */

public class ActivityAnimation extends BaseActivity {

    private AdapterItemLeftIn mAdapter;
    private Activity mActivity;

    private RecyclerView rcyLeftIn;


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
        mActivity = this;
        mAdapter = new AdapterItemLeftIn(mActivity);
    }

    private void initView() {
        rcyLeftIn = findViewById(R.id.rcy_left_in);


    }

    private void bindData() {


    }


}
