package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;

import com.ruihe.demo.R;

/**
 * 描述：首页二
 * Created by ruihe on 2016/4/28.
 */
public class FragmentTwo extends BaseFragment {


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        bindData();

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_two;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_second);

    }

    private void initView() {

    }


}
