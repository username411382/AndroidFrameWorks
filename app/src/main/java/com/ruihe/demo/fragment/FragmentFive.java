package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;

import com.ruihe.demo.R;

/**
 * 描述：首页五
 * Created by ruihe on 2016/4/28.
 */
public class FragmentFive extends BaseFragment {


    @Override
    public void onAttached() {

    }


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        initView();
        bindData();
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_five;
    }


    private void initView() {

    }

    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_fifth);
    }


    @Override
    public void onDetached() {

    }





}
