package com.miguo.utils.dev;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Vibrator;

import com.fanwe.app.ActivityLifeManager;
import com.fanwe.library.utils.SDActivityUtil;

/**
 * Created by didik on 2016/11/29.
 */

public class DevUtil {
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private Activity activity;
    private static final float SENSOR_SHAKE = 19.5f;
    private float testMaxValueX = 0f;
    private float testMaxValueY = 0f;
    private float testMaxValueZ = 0f;
    private float middleValue;

    public DevUtil(Activity activity) {
        if (activity == null) return;
        this.activity = activity;
        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.BRAND.equalsIgnoreCase("samsung")){
            middleValue=70f;
        }else {
            middleValue=SENSOR_SHAKE;
        }
    }

    public void registerShakeListener() {
        if (sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor
                    (Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterShakeListener() {
        if (sensorManager != null) {
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
            float absX = Math.abs(x);
            float absY = Math.abs(y);
            float absZ = Math.abs(z);
            if (absX > testMaxValueX) {
                testMaxValueX = absX;
            }
            if (absY > testMaxValueY) {
                testMaxValueY = absY;
            }
            if (absZ > testMaxValueZ) {
                testMaxValueZ = absZ;
            }
            if (absX > middleValue || absY > middleValue || absZ > middleValue) {
                vibrator.vibrate(200);
                Activity lastActivity = ActivityLifeManager.getInstance().getLastActivity();
                if (!(lastActivity instanceof DevActivity)) {
                    SDActivityUtil.startActivity(activity, DevActivity.class);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

}
