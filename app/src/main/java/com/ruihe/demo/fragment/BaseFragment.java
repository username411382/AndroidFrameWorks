package com.ruihe.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述：所有碎片的基类
 * Created by ruihe on 2016/4/28.
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onAttached();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getContentViewId(), null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getFragmentView(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDetached();
    }


    /**
     * 初始化View
     *
     * @param view
     * @param savedInstanceState
     */
    public abstract void getFragmentView(View view, Bundle savedInstanceState);

    /**
     * 获得Fragment布局Id
     *
     * @return
     */
    public abstract int getContentViewId();


    public abstract void onAttached();

    public abstract void onDetached();


}
