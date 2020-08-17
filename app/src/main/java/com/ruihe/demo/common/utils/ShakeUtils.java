package com.ruihe.demo.common.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 摇一摇工具类
 * Created by RH on 2018/9/14
 */
public class ShakeUtils implements SensorEventListener {

    public interface OnShakeListener {
        void onShake();
    }

    private SensorManager mSensorManager;
    private OnShakeListener mOnShakeListener;
    private static final int SENSOR_VALUE = 9;

    public ShakeUtils(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        mOnShakeListener = onShakeListener;
    }

    public void onResume() {
        LogUtil.d( "registerListener");
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
        LogUtil.d("unregisterListener");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        LogUtil.d( "onSensorChanged:"+sensorType);
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            //这里可以调节摇一摇的灵敏度
            LogUtil.d( "start sensor value == " + " " + values[0] + " " + values[1] + " " + values[2]);
            if (Math.abs(values[0]) > SENSOR_VALUE && Math.abs(values[1]) > SENSOR_VALUE) {
                LogUtil.d( "end sensor value == " + " " + values[0] + " " + values[1] + " " + values[2]);
                if (null != mOnShakeListener) {
                    mOnShakeListener.onShake();
                }
                return;
            }

            if (Math.abs(values[1]) > SENSOR_VALUE && Math.abs(values[2]) > SENSOR_VALUE){
                LogUtil.d( "end sensor value == " + " " + values[0] + " " + values[1] + " " + values[2]);
                if (null != mOnShakeListener) {
                    mOnShakeListener.onShake();
                }
                return;
            }

            if (Math.abs(values[0]) > SENSOR_VALUE && Math.abs(values[2]) > SENSOR_VALUE) {
                LogUtil.d( "end sensor value == " + " " + values[0] + " " + values[1] + " " + values[2]);
                if (null != mOnShakeListener) {
                    mOnShakeListener.onShake();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
