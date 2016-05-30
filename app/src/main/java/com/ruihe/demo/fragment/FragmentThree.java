package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.view.autoViewpager.adapter.AutoScrollViewPagerAdapter;
import com.ruihe.demo.common.utils.view.autoViewpager.viewpager.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：滑动解锁
 * Created by ruihe on 2016/5/28.
 */
public class FragmentThree extends BaseFragment implements ViewPager.OnPageChangeListener {


    private View mView;
    private AutoScrollViewPager viewPager;
    private AutoScrollViewPagerAdapter pagerAdapter;
    private LinearLayout indicator;

    private int[] ids = {R.drawable.bg_guide_page0, R.drawable.bg_guide_page1, R.drawable.bg_guide_page2, R.drawable.bg_guide_page3, R.drawable.bg_guide_page4};

    private int realPageCount;


    @Override
    public void onAttached() {

    }

    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {
        mView = view;
        initVariable();
        initView();
        initListener();
        bindData();
    }

    private void initVariable() {
        pagerAdapter = new AutoScrollViewPagerAdapter(holder) {

            @Override
            public List<View> buildViews() {
                return addView();
            }
        };
        realPageCount = pagerAdapter.getRealPageCount();
    }


    private void initView() {
        viewPager = (AutoScrollViewPager) mView.findViewById(R.id.view_pager);
        indicator = (LinearLayout) mView.findViewById(R.id.indicator);
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(this);
    }


    private void bindData() {
        addIndicators();
        viewPager.setAdapter(pagerAdapter);

        viewPager.setInterval(1500);
        viewPager.setAutoScrollDurationFactor(10);
        viewPager.start();
    }


    private void addIndicators() {
        for (int i = 0; i < ids.length; i++) {
            View v = new View(holder);
            v.setBackgroundResource(R.drawable.banner_pagecontrol_normal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(24, 24);
            layoutParams.setMargins(4, 4, 4, 4);
            if (i == 0) {
                v.setBackgroundResource(R.drawable.banner_pagecontrol_selected);
            }
            indicator.addView(v, layoutParams);
        }
    }

    private List<View> addView() {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView view = new ImageView(holder);
            view.setLayoutParams(params);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageResource(ids[i]);
            view.setTag(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder, "点击的position : " + v.getTag(), Toast.LENGTH_SHORT).show();
                }
            });
            list.add(view);
        }
        return list;
    }


    @Override
    public void onPageSelected(int position) {
        int pos = position % realPageCount;
        Log.d("ruihe", "" + indicator.getChildAt(pos));
        for (int i = 0; i < indicator.getChildCount(); i++) {
            indicator.getChildAt(i).setBackgroundResource(R.drawable.banner_pagecontrol_normal);
        }
        indicator.getChildAt(pos).setBackgroundResource(R.drawable.banner_pagecontrol_selected);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_three;
    }


    @Override
    public void onDetached() {

    }


    @Override
    public void onPause() {
        super.onPause();
        viewPager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.onResume();
    }
}
