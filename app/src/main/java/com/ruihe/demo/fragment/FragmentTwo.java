package com.ruihe.demo.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.MapView;
import com.ruihe.demo.R;
import com.ruihe.demo.bean.ItemTrace;
import com.ruihe.demo.common.utils.DateUtils;
import com.ruihe.demo.common.utils.ToastUtils;
import com.ruihe.demo.common.utils.trace.TraceUtils;

/**
 * 描述：首页二
 * Created by ruihe on 2016/4/28.
 */
public class FragmentTwo extends BaseFragment implements View.OnClickListener, TraceUtils.OnReceiveTraceDistanceListener {


    private static final int MSG_CURRENT_DISTANCE = 100;


    private MapView mMapView;
    public static BaiduMap mBaiduMap;


    private TextView tvTraceTime;
    private TextView tvTraceDistance;
    private Button btnShot;
    private ItemTrace mItemTrace;
    private ImageView ivShotMap;

    private ShotMapViewListener mShotMapViewListener;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case MSG_CURRENT_DISTANCE:
                    tvTraceTime.setText("时间" + DateUtils.getStringByFormat(System.currentTimeMillis(), "mm:ss"));
                    tvTraceDistance.setText(mItemTrace.totalDistance + "米");
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    public void getFragmentView(View view, Bundle savedInstanceState) {

        mItemTrace = new ItemTrace();
        mShotMapViewListener = new ShotMapViewListener();


        mMapView = (MapView) view.findViewById(R.id.view_mapView);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);

        tvTraceTime = (TextView) view.findViewById(R.id.tv_trace_time);
        tvTraceDistance = (TextView) view.findViewById(R.id.tv_trace_distance);
        ivShotMap = (ImageView) view.findViewById(R.id.iv_shot_map);
        btnShot = (Button) view.findViewById(R.id.btn_shot_map);
        initListener();
        bindData();

    }

    private void initListener() {
        btnShot.setOnClickListener(this);
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
        holder.mTitleView.addRightText(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.clear();
            }
        }, "清除轨迹");
        holder.mTitleView.addLeftTextMenu(holder, R.string.cancel, 15, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("正在停止轨迹服务，请稍候");
                TraceUtils.getInstance().stopTrace();
            }
        });

        holder.mTitleView.setTitle(R.string.main_second);

        TraceUtils.getInstance().initTraceParameters(getActivity());
        tvTraceTime.setText(TraceUtils.getInstance().mEntityName);

        TraceUtils.getInstance().startTrace();
        TraceUtils.getInstance().queryHistoryTrack(this);

    }


    /**
     * 地图截屏回调接口
     */
    private class ShotMapViewListener implements SnapshotReadyCallback {
        @Override
        public void onSnapshotReady(Bitmap bitmap) {
            ivShotMap.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onClick(View v) {
        mBaiduMap.snapshotScope(null, mShotMapViewListener);
    }


    @Override
    public void getDistance(ItemTrace itemTrace) {
        mItemTrace = itemTrace;
        mHandler.sendEmptyMessage(MSG_CURRENT_DISTANCE);
    }
}
