package com.ruihe.demo.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.Constants.MainPage;
import com.ruihe.demo.common.chat.util.RongIMUtil;
import com.ruihe.demo.fragment.FragmentFive;
import com.ruihe.demo.fragment.FragmentFour;
import com.ruihe.demo.fragment.FragmentOne;
import com.ruihe.demo.fragment.FragmentThree;
import com.ruihe.demo.fragment.FragmentTwo;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.util.ConvertUtils;
import io.rong.imlib.RongIMClient;
import qiu.niorgai.StatusBarCompat;

/**
 * 描述：主页面 分支加上的。但是主分支又把它切换回去了
 * Created by ruihe on 2016/4/28.
 */
public class ActivityMain extends BaseActivity implements View.OnClickListener {


    private Activity mActivity;

    private FragmentManager mFragmentManager;
    private Fragment[] mFragments;
    private LinearLayout[] mLyPages;

    @Override
    public int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onActivityViewCreated() {
        initVariable();
        initView();
        initListener();
        bindData();
    }

    private void initVariable() {
        mActivity = this;
        mFragmentManager = getSupportFragmentManager();
        mFragments = new Fragment[5];
        mLyPages = new LinearLayout[5];
    }

    private void initView() {
        mLyPages[0] = (LinearLayout) findViewById(R.id.ly_first);
        mLyPages[1] = (LinearLayout) findViewById(R.id.ly_second);
        mLyPages[2] = (LinearLayout) findViewById(R.id.ly_third);
        mLyPages[3] = (LinearLayout) findViewById(R.id.ly_fourth);
        mLyPages[4] = (LinearLayout) findViewById(R.id.ly_five);
    }

    private void initListener() {
        for (LinearLayout lyPage : mLyPages) {
            lyPage.setOnClickListener(this);
        }
    }

    private void bindData() {
        mLyPages[0].setSelected(true);
        refreshFragment(MainPage.PAGE_FIRST);

        //连接融云
        //connectRongIM(Constants.userTokens[0]);
        try {
            ArrayList<Province> provinces = new ArrayList<>();
            String json = ConvertUtils.toString(mActivity.getAssets().open("city.json"));
            provinces.addAll(JSON.parseArray(json, Province.class));

            for (int i = 0; i < provinces.size(); i++) {
                if (provinces.get(i).getAreaName().equals("贵州省")) {
                    for (int j = 0; j < provinces.get(i).getCities().size(); j++) {
                        City city = provinces.get(i).getCities().get(j);
                        Log.d("ruihe", "city: " + city.getAreaName());
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void connectRongIM(String token) {

        if (getApplicationInfo().packageName.equals(RongIMUtil.getCurProcessName(getApplicationContext()))) {

            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                @Override
                public void onTokenIncorrect() {

                    Log.d("ruihe", "--onTokenIncorrect");
                }

                @Override
                public void onSuccess(String userid) {
                    Log.d("ruihe", "--onSuccess---" + userid);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("ruihe", "--onError" + errorCode);
                }
            });
        }

    }

    private void refreshFragment(MainPage currentPage) {


        int currentFragment = 0;

        switch (currentPage) {

            case PAGE_FIRST:
                currentFragment = 0;
                if (mFragments[0] == null) {
                    mFragments[0] = new FragmentOne();
                    mFragmentManager.beginTransaction().add(R.id.fy_main, mFragments[0]).commitAllowingStateLoss();
                }
                mTitleView.setTitle(R.string.main_first);
                break;
            case PAGE_SECOND:
                currentFragment = 1;
                if (mFragments[1] == null) {
                    mFragments[1] = new FragmentTwo();
                    mFragmentManager.beginTransaction().add(R.id.fy_main, mFragments[1]).commitAllowingStateLoss();
                }
                mTitleView.setTitle(R.string.main_second);
                break;

            case PAGE_THIRD:
                currentFragment = 2;
                if (mFragments[2] == null) {
                    mFragments[2] = new FragmentThree();
                    mFragmentManager.beginTransaction().add(R.id.fy_main, mFragments[2]).commitAllowingStateLoss();
                }
                mTitleView.setTitle(R.string.main_third);
                break;
            case PAGE_FOURTH:
                currentFragment = 3;
                if (mFragments[3] == null) {
                    mFragments[3] = new FragmentFour();
                    mFragmentManager.beginTransaction().add(R.id.fy_main, mFragments[3]).commitAllowingStateLoss();
                }
                mTitleView.setTitle(R.string.main_fourth);
                break;
            case PAGE_FIVE:
                currentFragment = 4;
                if (mFragments[4] == null) {
                    mFragments[4] = new FragmentFive();
                    mFragmentManager.beginTransaction().add(R.id.fy_main, mFragments[4]).commitAllowingStateLoss();
                }
                mTitleView.setTitle(R.string.main_fifth);
                break;
            default:
                break;

        }

        changePageBackground(currentFragment);

        for (Fragment fragment : mFragments) {
            if (fragment != null) {
                mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
            }
        }

        mFragmentManager.beginTransaction().show(mFragments[currentFragment]).commitAllowingStateLoss();

    }


    private void changePageBackground(int page) {

        for (int i = 0; i < mLyPages.length; i++) {
            if (i == page) {
                mLyPages[i].setSelected(true);
            } else {
                mLyPages[i].setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ly_first:
                refreshFragment(MainPage.PAGE_FIRST);
                break;
            case R.id.ly_second:
                refreshFragment(MainPage.PAGE_SECOND);
                break;
            case R.id.ly_third:
                refreshFragment(MainPage.PAGE_THIRD);
                break;
            case R.id.ly_fourth:
                refreshFragment(MainPage.PAGE_FOURTH);
                break;
            case R.id.ly_five:
                refreshFragment(MainPage.PAGE_FIVE);
                break;
            default:
                break;

        }
    }


}
