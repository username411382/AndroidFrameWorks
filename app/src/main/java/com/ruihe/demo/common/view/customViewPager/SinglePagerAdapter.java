package com.ruihe.demo.common.view.customViewPager;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ruihe.demo.R;

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
       /* ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(view);*/
        SimpleDraweeView drawView = new SimpleDraweeView(container.getContext());
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(container.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(R.drawable.default_img)
                .build();
        Uri uri = Uri.parse(mUrls.get(position));
        drawView.setHierarchy(hierarchy);
        drawView.setImageURI(uri);
        container.addView(drawView);

        return drawView;
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
