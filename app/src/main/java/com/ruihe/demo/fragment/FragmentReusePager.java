package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ruihe.demo.R;

/**
 * description:
 * Created by ruihe on 2017/3/3
 */

public class FragmentReusePager extends BaseFragment {


    private static final String KEY_PAGE_Number = "key_page_postion";

    private TextView tvPageNumber;
    private TextView tvPageRealValue;


    public static FragmentReusePager getFragment(int pageNumber) {
        FragmentReusePager fragmentReusePager = new FragmentReusePager();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_Number, pageNumber);
        fragmentReusePager.setArguments(bundle);
        return fragmentReusePager;
    }


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        tvPageNumber = (TextView) view.findViewById(R.id.tv_page_number);
        int pageNumber = getArguments().getInt(KEY_PAGE_Number);
        tvPageNumber.setText(String.format("%d", pageNumber));
        tvPageRealValue = (TextView) view.findViewById(R.id.tv_page_real_value);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_reuse_pager;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }

    public void setPageValue(int realPageValue) {
        tvPageRealValue.setText(realPageValue + "");
    }

}
