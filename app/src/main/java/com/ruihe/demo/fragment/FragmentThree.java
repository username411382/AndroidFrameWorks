package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.view.SlidingSeekBar;

/**
 * 描述：滑动解锁
 * Created by ruihe on 2016/5/28.
 */
public class FragmentThree extends BaseFragment implements SlidingSeekBar.OnSeekBarStatusListener {


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
    public void onAttached() {

    }

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        mView = view;
        initView();
        initListener();
        bindData();
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_three;
    }


    private void initView() {
        viewBg = mView.findViewById(R.id.view_bg);
        tvSlideToRightHint = (TextView) mView.findViewById(R.id.tv_slide_to_right);
        slideSeekBar = (SlidingSeekBar) mView.findViewById(R.id.seek_bar);

    }

    private void initListener() {
        slideSeekBar.initSlideToRightUnlock(holder, tvSlideToRightHint, viewBg, this);
    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_third);

    }

    @Override
    public void onDetached() {

    }

    @Override
    public void onFinishUnlock() {
        Toast.makeText(holder, "恭喜，解锁成功！", Toast.LENGTH_SHORT).show();
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }
}
