package com.ruihe.demo.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ruihe.demo.R;
import com.ruihe.demo.common.DensityUtil;

/**
 * 上移背景动画
 * Created by ruihe on 2017/1/17.
 */

public class UpMoveAnimationView extends RelativeLayout {

    private ImageView ivLogo;
    private ValueAnimator valueAnimator;
    private AlphaAnimation alphaAnimation;
    private int measureHeight;
    private int mRepeatCount;
    private int[] bgResource = {R.drawable.bg_page1, R.drawable.bg_page2, R.drawable.bg_page3};
    private Context mContext;

    public UpMoveAnimationView(Context context) {
        super(context);
        initVariable(context);
    }

    public UpMoveAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariable(context);
    }

    public UpMoveAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable(context);
    }

    private void initVariable(Context context) {
        mContext = context;
        valueAnimator = ValueAnimator.ofInt(1, 100);
        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        for (int i = 0; i < 2; i++) {
            addViewToRelativeLayout(i == 0);
        }
    }


    private void setLayoutParams(ImageView imageView, int topMargin) {
        LinearLayout.LayoutParams rp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        rp.topMargin = topMargin;
        imageView.setLayoutParams(rp);
    }

    private void addViewToRelativeLayout(boolean isInitFirstImgView) {
        View itemView = View.inflate(mContext, R.layout.item_bg, null);
        addView(itemView);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_bg);
        if (isInitFirstImgView) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(bgResource[0]);
            ivLogo = imageView;
            measureHeight = DensityUtil.getMeasureHeight(imageView);
        }
    }


    public void startUpAlphaAnimation() {
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            IntEvaluator mIntEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                Log.d("ruihe", "currentValue: " + value);
                float fraction = animation.getAnimatedFraction();
                setLayoutParams(ivLogo, mIntEvaluator.evaluate(fraction, 0, -(measureHeight - DensityUtil.getScreenHeight(mContext))));
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRepeatCount++;
                mRepeatCount = (mRepeatCount == 3) ? 0 : mRepeatCount;
                ImageView imageView = (ImageView) getChildAt(1).findViewById(R.id.iv_bg);
                imageView.setImageResource(bgResource[mRepeatCount]);
                imageView.setVisibility(View.VISIBLE);
                ivLogo = imageView;
                imageView.startAnimation(alphaAnimation);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        removeViewAt(0);
                        addViewToRelativeLayout(false);
                        valueAnimator.start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        valueAnimator.setDuration(3000).start();
    }


    public void endUpAlphaAnimation() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
            removeAllViews();
            for (int i = 0; i < 2; i++) {
                addViewToRelativeLayout(i == 0);
            }
            mRepeatCount = 0;
            Log.d("ruihe", "end: " + mRepeatCount);
        }
    }

    public void restartUpAlphaAnimation() {
        if (valueAnimator != null && !valueAnimator.isRunning()) {
            valueAnimator.start();
            Log.d("ruihe", "restart: " + mRepeatCount);
        }
    }

}
