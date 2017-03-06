package com.ruihe.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ruihe.demo.common.utils.LogUtil;
import com.ruihe.demo.fragment.FragmentReusePager;

/**
 * description:
 * Created by ruihe on 2017/3/3
 */

public class AdapterReuseViewPager extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private FragmentReusePager[] mFragmentReusePagers;
    private int mCurrentValue;


    public AdapterReuseViewPager(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        mFragmentReusePagers = new FragmentReusePager[3];
        this.viewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.setCurrentItem(1, false);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public Fragment getItem(int position) {

        if (mFragmentReusePagers[position] == null) {
            mFragmentReusePagers[position] = FragmentReusePager.getFragment(position);
        }
        return mFragmentReusePagers[position];
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogUtil.d("onPageScrolled: " + position);
    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.d("onPageSelected: " + position);
        if (mFragmentReusePagers[position] == null) {
            return;
        }

        mFragmentReusePagers[position].setPageValue(mCurrentValue);


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int position = viewPager.getCurrentItem();
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            LogUtil.d("onPageScrollStateChanged: " + position);
            if (position == 2) {
                mCurrentValue++;
                mFragmentReusePagers[position].setPageValue(mCurrentValue);
                viewPager.setCurrentItem(1, false);
            } else if (position == 0) {
                mCurrentValue--;
                mFragmentReusePagers[position].setPageValue(mCurrentValue);
                viewPager.setCurrentItem(1, false);
            }
        }
    }
}
