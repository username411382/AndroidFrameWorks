package com.ruihe.demo.common.utils.view.customViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.net.VolleyUtils;

import java.util.ArrayList;
import java.util.List;

public class SinglePagerAdapter extends PagerAdapter {


    private List<String> mUrls;
    private RequestQueue mQueue;
    private Context mContext;


    public SinglePagerAdapter(Context context, List<String> listData) {
        mUrls = new ArrayList<>();
        mContext = context;
        mUrls = listData;
        mQueue = Volley.newRequestQueue(mContext);
    }


    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        VolleyUtils.loadImage(mQueue, mUrls.get(position), view, R.drawable.default_img);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public void notifyDataChange(List<String> listData) {

        if (listData != null && listData.size() > 0) {
            mUrls.clear();
            mUrls.addAll(listData);
        }
        super.notifyDataSetChanged();
    }

}
