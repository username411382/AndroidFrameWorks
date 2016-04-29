package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ruihe.demo.R;

/**
 * 描述：首页一
 * Created by ruihe on 2016/4/28.
 */
public class FragmentOne extends BaseFragment {

    private int[] mArray = {5, 3, 8, 6, 4};
    private StringBuffer strBuffer;

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        initView();
        bindData();

    }

    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_first);

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_one;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    private void initView() {

    }


    //冒泡排序
    private void bubbleSort(int[] arrays) {

        if (arrays == null || arrays.length == 0) {
            return;
        }
        for (int i = 0; i < arrays.length; i++) {
            for (int j = arrays.length - 1; j > i; j--) {
                if (arrays[j - 1] > arrays[j]) {
                    swap(arrays, j - 1, j);
                }
            }
        }
        for (int a : arrays) {
            strBuffer.append(a);
        }
        Log.d("ruihe", "------冒泡排序后的数组---------" + strBuffer);

    }

    private static void swap(int[] arrays, int i, int j) {
        int temp = arrays[i];
        arrays[i] = arrays[j];
        arrays[j] = temp;
    }



   /* private void showAnimation() {

        final ValueAnimator va = new ValueAnimator();

        va.setDuration(1000);

        if (isExpand) {
            va.setFloatValues(0f, 1f);
        } else {
            va.setFloatValues(1f, 0f);
        }


        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }
        });


        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();
                ly.getLayoutParams().height = (int) (windowSize * value);
                ly.setLayoutParams(ly.getLayoutParams());
            }
        });

        va.start();

    }*/


}
