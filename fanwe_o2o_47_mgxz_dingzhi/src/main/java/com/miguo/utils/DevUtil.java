package com.miguo.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

import com.fanwe.library.utils.SDActivityUtil;
import com.miguo.ui.view.DevActivity;

/**
 * Created by didik on 2016/11/29.
 */

public class DevUtil {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private Activity activity;
    private static final int SENSOR_SHAKE = 70;
    private long first=0;

    public DevUtil(Activity activity) {
        if (activity==null)return;
        this.activity=activity;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void registerShakeListener(){
        if (sensorManager!=null){
            sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterShakeListener(){
        if (sensorManager!=null){
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            Log.i("Test", "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = SENSOR_SHAKE;// 如果不敏感请自行调低该数值,低于10的话就不行了,因为z轴上的加速度本身就已经达到10了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis-first>200){
                    first=currentTimeMillis;
                    SDActivityUtil.startActivity(activity, DevActivity.class);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

}
