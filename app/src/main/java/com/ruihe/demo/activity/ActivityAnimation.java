package com.ruihe.demo.activity;

import com.ruihe.demo.R;
import com.ruihe.demo.common.view.UpMoveAnimationView;

/**
 * Created by ruihe on 2017/1/16.
 */

public class ActivityAnimation extends BaseActivity {


    private UpMoveAnimationView ryBg;

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
        ryBg.restartUpAlphaAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ryBg.endUpAlphaAnimation();
    }

    private void initVariable() {
    }

    private void initView() {
        ryBg = (UpMoveAnimationView) findViewById(R.id.ry_bg);
    }

    private void bindData() {
        ryBg.startUpAlphaAnimation();
    }


}
