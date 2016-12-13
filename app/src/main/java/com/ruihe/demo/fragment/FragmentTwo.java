package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihe.demo.R;
import com.ruihe.demo.common.view.SlidingSeekBar;

/**
 * 描述：首页二
 * Created by ruihe on 2016/4/28.
 */
public class FragmentTwo extends BaseFragment implements View.OnClickListener, SlidingSeekBar.OnSeekBarStatusListener {


    private TextView tvSecond;
    private View mView;
    private View viewBg;
    private TextView tvSlideToRightHint;
    private SlidingSeekBar slideSeekBar;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            slideSeekBar.setProgress(0);
            tvSlideToRightHint.setAlpha(1.0f);
            viewBg.setAlpha(0.0f);
        }
    };

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        mView = view;
        tvSecond = (TextView) view.findViewById(R.id.tv_second);
        initView();
        initListener();
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


    private void initView() {
        viewBg = mView.findViewById(R.id.view_bg);
        tvSlideToRightHint = (TextView) mView.findViewById(R.id.tv_slide_to_right);
        slideSeekBar = (SlidingSeekBar) mView.findViewById(R.id.seek_bar);

    }

    private void initListener() {
        slideSeekBar.initSlideToRightUnlock(holder, tvSlideToRightHint, viewBg, this);
        tvSecond.setOnClickListener(this);
    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_third);

    }


    @Override
    public void onClick(View v) {
    }





    @Override
    public void onFinishUnlock() {
        Toast.makeText(holder, "恭喜，解锁成功！", Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }
}

