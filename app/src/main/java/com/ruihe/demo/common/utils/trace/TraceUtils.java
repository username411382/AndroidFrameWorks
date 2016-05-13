package com.ruihe.demo.common.utils.trace;

import android.content.Context;
import android.graphics.Color;
import android.os.Looper;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.ruihe.demo.R;
import com.ruihe.demo.common.utils.trace.;
import com.ruihe.demo.common.utils.ToastUtils;
import com.ruihe.demo.fragment.FragmentTwo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：百度轨迹工具类
 * Created by ruihe on 2016/5/13.
 */
public class TraceUtils {


    public static TraceUtils instance = null;

    //采集周期（单位 : 秒）
    private int gatherInterval = 2;
    //轨迹服务
    protected Trace trace;

    //entity标识
    protected String entityName;

    //鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
    protected static long serviceId = 116664;

    // 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
    private int traceType = 2;

    //轨迹服务客户端
    protected LBSTraceClient mClient;

    //Entity监听器
    protected OnEntityListener entityListener;

    private List<LatLng> pointList = new ArrayList<>();
    protected boolean isInUploadFragment = true;
    private BitmapDescriptor realTimeBitmap;
    protected MapStatusUpdate msUpdate = null;
    protected boolean isTraceStart = false;
    // 覆盖物
    protected OverlayOptions overlay;
    // 路线覆盖物
    private PolylineOptions polyline;
    //刷新地图线程(获取实时点)
    protected RefreshThread refreshThread;
    private Context mContext;

    /**
     * 开启轨迹服务监听器
     */
    protected OnStartTraceListener startTraceListener = null;

    /**
     * 停止轨迹服务监听器
     */
    protected OnStopTraceListener stopTraceListener = null;


    public static TraceUtils getInstance() {

        if (instance == null) {
            instance = new TraceUtils();
        }
        return instance;
    }


    public void initTraceParameters(Context context) {
        mContext = context;

        // 初始化轨迹服务客户端
        mClient = new LBSTraceClient(mContext);

        // 设置定位模式
        mClient.setLocationMode(LocationMode.High_Accuracy);

        // 初始化entity标识
        entityName = DateUtils.getStringByFormat(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");

        // 初始化轨迹服务
        trace = new Trace(mContext, serviceId, entityName, traceType);

        // 初始化OnEntityListener
        initOnEntityListener();
    }


    /**
     * 初始化OnEntityListener
     */
    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                ToastUtils.show("entity请求失败回调接口消息 : " + arg0);
            }

            // 添加entity回调接口
            public void onAddEntityCallback(String arg0) {
                ToastUtils.show("添加entity回调接口消息 : " + arg0);
            }

            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String message) {
                ToastUtils.show("entityList回调消息 : " + message);
            }

            @Override
            public void onReceiveLocation(TraceLocation location) {
                showRealTimeTrack(location);
            }

        };
    }


    //显示实时轨迹
    protected void showRealTimeTrack(TraceLocation location) {


        if (null == refreshThread || !refreshThread.refresh) {
            return;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            ToastUtils.show("当前查询无轨迹点");

        } else {

            LatLng latLng = new LatLng(latitude, longitude);

            if (1 == location.getCoordType()) {
                LatLng sourceLatLng = latLng;
                CoordinateConverter converter = new
                        CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(sourceLatLng);
                latLng =
                        converter.convert();
            }

            pointList.add(latLng);

            if (isInUploadFragment) {
                // 绘制实时点
                drawRealTimePoint(latLng);
            }

        }


    }

    // 绘制实时点
    private void drawRealTimePoint(LatLng point) {

        FragmentTwo.mBaiduMap.clear();

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19).build();

        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        realTimeBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);

        overlay = new MarkerOptions().position(point)
                .icon(realTimeBitmap).zIndex(9).draggable(true);

        if (pointList.size() >= 2 && pointList.size() <= 10000) {
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(pointList);
        }
        addMarker();

    }

    //添加地图覆盖物
    protected void addMarker() {

        if (null != msUpdate) {
            FragmentTwo.mBaiduMap.setMapStatus(msUpdate);
        }

        // 路线覆盖物
        if (null != polyline) {
            FragmentTwo.mBaiduMap.addOverlay(polyline);
        }

    /*    // 围栏覆盖物
        if (null != Geofence.fenceOverlay) {
            MainActivity.mBaiduMap.addOverlay(Geofence.fenceOverlay);
        }

        // 实时点覆盖物
        if (null != overlay) {
            MainActivity.mBaiduMap.addOverlay(overlay);
        }*/
    }


    //查询实时轨迹
    private void queryRealTimeLoc() {
        mClient.queryRealtimeLoc(serviceId, entityListener);
    }


    protected class RefreshThread extends Thread {

        protected boolean refresh = true;

        @Override
        public void run() {
            Looper.prepare();
            while (refresh) {
                // 查询实时位置
                queryRealTimeLoc();
                try {
                    Thread.sleep(gatherInterval * 1000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠失败");
                }
            }
            Looper.loop();
        }
    }


    /**
     * 开启轨迹服务
     */
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        client.startTrace(trace, startTraceListener);

        if (!MonitorService.isRunning) {
            // 开启监听service
            MonitorService.isCheck = true;
            MonitorService.isRunning = true;
            startMonitorService();
        }
    }

    /**
     * 停止轨迹服务
     */
    private void stopTrace() {
        // 通过轨迹服务客户端client停止轨迹服务
        client.stopTrace(trace, stopTraceListener);

        // 停止监听service
        MonitorService.isCheck = false;
        MonitorService.isRunning = false;
        MainActivity.mContext.stopService(serviceIntent);
    }


    /**
     * 初始化OnStartTraceListener
     */
    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {

            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            public void onTraceCallback(int arg0, String arg1) {
                ToastUtils.show("开启轨迹服务回调接口消息 [消息编码 : " + arg0 + "，消息内容 : " + arg1 + "]");
                if (0 == arg0 || 10006 == arg0 || 10008 == arg0 || 10009 == arg0) {
                    isTraceStart = true;
                    // startRefreshThread(true);
                }
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            public void onTracePushCallback(byte arg0, String arg1) {
                // TODO Auto-generated method stub
                if (0x03 == arg0) {
                    try {
                        JSONObject dataJson = new JSONObject(arg1);
                        if (null != dataJson) {
                            String mPerson = dataJson.getString("monitored_person");
                            String action = dataJson.getInt("action") == 1 ? "进入" : "离开";
                            String date = DateUtils.getDate(dataJson.getInt("time"));
                            ToastUtils.show("监控对象[" + mPerson + "]于" + date + " 【" + action + "】围栏");
                        }

                    } catch (JSONException e) {
                        ToastUtils.show("轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]");
                    }
                } else {
                    ToastUtils.show("轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]");
                }
            }

        };
    }

    /**
     * 初始化OnStopTraceListener
     */
    private void initOnStopTraceListener() {
        // 初始化stopTraceListener
        stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                ToastUtils.show("停止轨迹服务成功");
                isTraceStart = false;
                startRefreshThread(false);
            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                ToastUtils.show("停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]");
                startRefreshThread(false);
            }
        };
    }

    protected void startRefreshThread(boolean isStart) {
        if (null == refreshThread) {
            refreshThread = new RefreshThread();
        }
        refreshThread.refresh = isStart;
        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }
    }


}
