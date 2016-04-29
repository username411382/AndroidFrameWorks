package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;

import com.ruihe.demo.R;

/**
 * 描述：首页四
 * Created by ruihe on 2016/4/28.
 */
public class FragmentFour extends BaseFragment {


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
        return R.layout.fragment_four;
    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_fourth);
    }


    private void initView() {

    }


    @Override
    public void onDetached() {

    }


}
