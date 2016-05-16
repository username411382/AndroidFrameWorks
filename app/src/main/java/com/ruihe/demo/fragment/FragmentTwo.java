package com.ruihe.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.ToastUtils;
import com.ruihe.demo.common.utils.trace.TraceUtils;

/**
 * 描述：首页二
 * Created by ruihe on 2016/4/28.
 */
public class FragmentTwo extends BaseFragment implements View.OnClickListener {


    private MapView mMapView;
    public static BaiduMap mBaiduMap;

    private Button btnStart;
    private Button btnEnd;


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        mMapView = (MapView) view.findViewById(R.id.view_mapView);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);

        btnEnd = (Button) view.findViewById(R.id.btn_end);
        btnStart = (Button) view.findViewById(R.id.btn_start);

        initListener();

        bindData();

    }

    private void initListener() {

        btnEnd.setOnClickListener(this);
        btnStart.setOnClickListener(this);
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

        TraceUtils.getInstance().initTraceParameters(getActivity());

    }


    @Override
    public void onClick(View v) {

        if (v == btnStart) {
            ToastUtils.show("正在开启轨迹服务，请稍候");
            TraceUtils.getInstance().startTrace();
        } else if (v == btnEnd) {
            ToastUtils.show("正在停止轨迹服务，请稍候");
            TraceUtils.getInstance().stopTrace();
        }
    }
}
