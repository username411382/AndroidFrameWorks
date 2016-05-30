package com.ruihe.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.ruihe.demo.R;
import com.ruihe.demo.bean.ItemTrace;
import com.ruihe.demo.common.utils.DateUtils;
import com.ruihe.demo.common.utils.trace.TraceUtils;

/**
 * 描述：鹰眼轨迹
 * Created by ruihe on 2016/5/17.
 */
public class ActivityPatrolTrace extends BaseActivity implements TraceUtils.OnReceiveTraceListener, SeekBar.OnSeekBarChangeListener {

    private static final int MSG_CURRENT_DISTANCE = 100;
    private static final int MSG_REFRESH_REMINDER_VIEW = 101;
    private static final String INTENT_TASK_NAME = "taskName";
    private static final String INTENT_TASK_RECORD_ID = "taskRecordId";
    private static final String INTENT_TASK_ID = "taskId";
    private static final String INTENT_MERCHANT_ID = "merchantId";
    private static final String INTENT_SERVICE_ID = "serviceId";

    private Activity mActivity;
    private MapView mMapView;
    public static BaiduMap mBaiduMap;


    private TextView tvTraceTime;
    private TextView tvTraceDistance;
    private View reminderViewBg;
    private ImageView ivTraceRecord;
    private TextView tvTraceRecording;

    private SeekBar seekBar;
    private ItemTrace mItemTrace;
    private String mTaskName;

    private ShotMapViewListener mShotMapViewListener;
    private int mTaskRecoedId;
    private int mTaskId;
    private int mMerchatId;
    private int mServicedId;


    public static void redirectToActivity(Context context, String taskName, int taskId, int merchantId, int serviceId, int taskRecordId) {
        Intent intent = new Intent(context, ActivityPatrolTrace.class);
        intent.putExtra(INTENT_TASK_NAME, taskName);
        intent.putExtra(INTENT_TASK_RECORD_ID, taskRecordId);
        intent.putExtra(INTENT_TASK_ID, taskId);
        intent.putExtra(INTENT_MERCHANT_ID, merchantId);
        intent.putExtra(INTENT_SERVICE_ID, serviceId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_CURRENT_DISTANCE:
                    reminderViewBg.setBackgroundColor(Color.parseColor("#50cd6d"));
                    ivTraceRecord.setImageResource(R.drawable.ic_map_record);
                    tvTraceRecording.setText(R.string.security_task_going);
                    break;
                case MSG_REFRESH_REMINDER_VIEW:
                    reminderViewBg.setBackgroundColor(Color.parseColor("#ff3e3e"));
                    ivTraceRecord.setImageResource(R.drawable.ic_map_stoped);
                    tvTraceRecording.setText(R.string.security_task_no_signal);
                    break;
                default:
                    break;
            }
            tvTraceTime.setText(getResources().getString(R.string.security_task_total_time) + DateUtils.getStringByFormat(System.currentTimeMillis(), "mm:ss"));
            tvTraceDistance.setText(mItemTrace.totalDistance + getResources().getString(R.string.security_task_total_distance));
        }
    };


    @Override
    public int getViewId() {
        return R.layout.activity_patrol_trace;
    }

    @Override
    public void onActivityViewCreated() {


        mActivity = this;
        mTaskName = getIntent().getStringExtra(INTENT_TASK_NAME);
        mTaskId = getIntent().getIntExtra(INTENT_TASK_ID, 0);
        mTaskRecoedId = getIntent().getIntExtra(INTENT_TASK_RECORD_ID, 0);
        mMerchatId = getIntent().getIntExtra(INTENT_MERCHANT_ID, 0);
        mServicedId = getIntent().getIntExtra(INTENT_SERVICE_ID, 0);

        mItemTrace = new ItemTrace();
        mShotMapViewListener = new ShotMapViewListener();

        mMapView = (MapView) findViewById(R.id.view_mapView);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        tvTraceTime = (TextView) findViewById(R.id.tv_trace_time);
        tvTraceDistance = (TextView) findViewById(R.id.tv_trace_distance);
        seekBar = (SeekBar) findViewById(R.id.seek_move_shot_map);
        reminderViewBg = findViewById(R.id.ry_reminder_view);
        ivTraceRecord = (ImageView) findViewById(R.id.iv_trace_record_ing);
        tvTraceRecording = (TextView) findViewById(R.id.tv_trace_record_ing);

        initListener();
        bindData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBar.setProgress(0);
    }

    private void initListener() {
        seekBar.setOnSeekBarChangeListener(this);

    }

    private void bindData() {
        mTitleView.removeAllMenu();
        mTitleView.addRightText(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.clear();

            }
        }, "清除轨迹");
        mTitleView.addLeftTextMenu(mActivity, R.string.cancel, 15, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TraceUtils.getInstance().stopTrace();
            }
        });

        mTitleView.setTitle(mTaskName);

        TraceUtils.getInstance().initTraceParameters(mActivity);
        tvTraceTime.setText(TraceUtils.getInstance().mEntityName);

        TraceUtils.getInstance().startTrace();
        TraceUtils.getInstance().queryHistoryTrack(this);

    }

    @Override
    public void getDistance(ItemTrace itemTrace) {
        mItemTrace = itemTrace;
        mHandler.sendEmptyMessage(MSG_CURRENT_DISTANCE);
    }

    @Override
    public void refreshReminderView() {
        mHandler.sendEmptyMessage(MSG_REFRESH_REMINDER_VIEW);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (!fromUser)
            return;
        seekBar.setProgress(progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getProgress() < 80) {//判断小于80的不算unlock操作
            seekBar.setProgress(0);
        } else {
            seekBar.setProgress(100);
            mBaiduMap.snapshotScope(null, mShotMapViewListener);
        }
    }


    /**
     * 地图截屏回调接口
     */
    private class ShotMapViewListener implements BaiduMap.SnapshotReadyCallback {
        @Override
        public void onSnapshotReady(Bitmap bitmap) {
            TraceUtils.getInstance().stopTrace();
        }
    }

    private String getTaskDescribe() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("完成\"");
        strBuffer.append(mTaskName);
        strBuffer.append("\"共");
        strBuffer.append(mItemTrace.totalDistance / 1000);
        strBuffer.append("公里,用时");
        strBuffer.append(TraceUtils.getInstance().getTotalRecordTime());
        strBuffer.append("分钟。");
        return strBuffer.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TraceUtils.getInstance().mClient.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TraceUtils.getInstance().stopTrace();
    }
}
