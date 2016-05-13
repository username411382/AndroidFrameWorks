package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.ruihe.demo.R;

/**
 * 描述：首页二
 * Created by ruihe on 2016/4/28.
 */
public class FragmentTwo extends BaseFragment {


    private MapView mMapView;
    public static BaiduMap mBaiduMap;


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        mMapView = (MapView) view.findViewById(R.id.view_mapView);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);


        bindData();

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_two;
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }


    private void bindData() {
        holder.mTitleView.removeAllMenu();
        holder.mTitleView.setTitle(R.string.main_second);

    }


}
