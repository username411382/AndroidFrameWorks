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
import com.ruihe.demo.common.utils.ToastUtil;
import com.ruihe.demo.common.view.autoViewpager.adapter.AutoScrollViewPagerAdapter;
import com.ruihe.demo.common.view.autoViewpager.viewpager.AutoScrollViewPagers;
import com.ruihe.demo.common.view.customViewPager.AutoScrollViewPager;
import com.ruihe.demo.common.view.customViewPager.CirclePageIndicator;
import com.ruihe.demo.common.view.customViewPager.SinglePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：滑动解锁
 * Created by ruihe on 2016/5/28.
 */
public class FragmentThree extends BaseFragment implements ViewPager.OnPageChangeListener, AutoScrollViewPager.OnPageClickListener {


    private View mView;
    private AutoScrollViewPagers viewPager;
    private AutoScrollViewPagerAdapter pagerAdapter;
    private LinearLayout indicator;

    private AutoScrollViewPager customViewPager;
    private CirclePageIndicator customIndicator;
    private SinglePagerAdapter mPagerAdapter;
    private List<String> mUrls;

    private String[] images = {"http://h.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=fed1392e952bd40742c7d7f449b9a532/e4dde71190ef76c6501a5c2d9f16fdfaae5167e8.jpg",
            "http://a.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=25d477ebe51190ef01fb96d6fc2ba675/503d269759ee3d6df51a20cd41166d224e4adedc.jpg",
            "http://c.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=70d2b81e60d0f703e6b291d53aca6a5e/0ff41bd5ad6eddc4ab1b5af23bdbb6fd5266333f.jpg",
            "http://fitimg.yunimg.cn//post//146405632530861.jpg",
            "http://fitimg.yunimg.cn//post//146440221124513.jpg",
            "http://fitimg.yunimg.cn//post//videoPost//cover//145639865537095.jpg"};
    private int[] ids = {R.drawable.bg_guide_page0, R.drawable.bg_page1, R.drawable.bg_page2, R.drawable.bg_page3, R.drawable.bg_guide_page4};

    private int realPageCount;


    @Override
    public void onAttached() {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_three;
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


        mUrls = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            mUrls.add(images[i]);
        }
        mPagerAdapter = new SinglePagerAdapter(holder, mUrls);
    }


    private void initView() {
        viewPager = (AutoScrollViewPagers) mView.findViewById(R.id.view_pager);
        indicator = (LinearLayout) mView.findViewById(R.id.indicator);

        customViewPager = (AutoScrollViewPager) mView.findViewById(R.id.view_pager_auto);
        customIndicator = (CirclePageIndicator) mView.findViewById(R.id.indicator_auto);

    }

    private void initListener() {
        viewPager.addOnPageChangeListener(this);
        customViewPager.setOnPageClickListener(this);
    }


    private void bindData() {
        addIndicators();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setInterval(1500);
        viewPager.setAutoScrollDurationFactor(10);
        viewPager.start();


        customViewPager.setAdapter(mPagerAdapter);
        customViewPager.setIndicator(customViewPager, customIndicator,
                getResources().getColor(R.color.bg_indicator_selected), getResources().getColor(R.color.bg_indicator),
                getResources().getColor(R.color.bg_indicator_stroke), 1.0f);
        customViewPager.setScrollFactor(5);
        customViewPager.setOffscreenPageLimit(4);
        customViewPager.startAutoScroll(2000);


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

    @Override
    public void onPageClick(AutoScrollViewPager pager, int position) {

        ToastUtil.show("点击的是第" + (position) + "张图");

    }
}
