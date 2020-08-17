package com.ruihe.demo.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ruihe.demo.fragment.random.FragmentCoin;
import com.ruihe.demo.fragment.random.FragmentDice;


/**
 * 随机事件pager适配器
 * Created by RH on 2018/9/13
 */
public class AdapterRandomPager extends FragmentPagerAdapter {

    private static final String[] titles = {"掷骰子", "抛硬币"};

    public AdapterRandomPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new FragmentDice();
        }

        return new FragmentCoin();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
