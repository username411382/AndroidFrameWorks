package com.ruihe.demo.common.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ruihe.demo.common.utils.DensityUtil;


/**
 * 描述：自定义滑动解锁（横向的）seekBar
 * 特点：自定义后seekBar只能滑动禁用点击
 * 最关键的就是设置flag
 * Created by ruihe on 2016/5/28.
 */
public class SlidingSeekBar extends SeekBar implements SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    private Context mContext;
    /**
     * seekBar是否滑动的判断标志，默认为false
     */
    private boolean isSlide;
    /**
     * 每次手指按下时处在屏幕上的横坐标
     */
    private int mStartTouchX;
    /**
     * seekBar的状态监听
     */
    private OnSeekBarStatusListener mOnSeekBarStatusListener;
    private SlidingSeekBar mSlideSeekBar;
    private TextView tvHint;
    private View viewBg;
    private int[] mLocation = new int[2];

    public SlidingSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlidingSeekBar(Context context) {
        super(context);
    }

    public SlidingSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initSlideToRightUnlock(Context context, TextView tvHint, View v, OnSeekBarStatusListener onSeekBarStatusListener) {
        mSlideSeekBar = this;
        this.mContext = context;
        this.tvHint = tvHint;
        this.viewBg = v;
        this.mOnSeekBarStatusListener = onSeekBarStatusListener;
        mSlideSeekBar.setProgress(0);
        viewBg.setAlpha(0.0f);
        tvHint.setAlpha(1.0f);
        setOnTouchListener(this);
        setOnSeekBarChangeListener(this);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;

        if (!isSlide) {
            seekBar.setProgress(0);
        } else {
            seekBar.setProgress(progress);
            float tempProgress = progress * 0.01f;
            viewBg.setAlpha(tempProgress);
            tvHint.setAlpha(1.0f - tempProgress * 2.0f);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (seekBar.getProgress() < 80) {//判断小于80的不算unlock操作
            seekBar.setProgress(0);
            tvHint.setAlpha(1.0f);
            viewBg.setAlpha(0.0f);
        } else {
            seekBar.setProgress(100);
            tvHint.setAlpha(0.0f);
            viewBg.setAlpha(1.0f);
            mOnSeekBarStatusListener.onFinishUnlock();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isSlide = false;
                mStartTouchX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                mSlideSeekBar.getLocationOnScreen(mLocation);
                int mSeekBarX = mLocation[0];
                int seekBarThumbLeft = mSeekBarX + DensityUtil.dip2px(mContext, 80) + mSlideSeekBar.getThumbOffset();

                if (mStartTouchX < seekBarThumbLeft) {
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isSlide = false;
                break;
            default:
                break;
        }

        return false;
    }


    /**
     * seekBar滑动完成监听
     */
    public interface OnSeekBarStatusListener {

        void onFinishUnlock();

    }


}
