package com.miguo.live.views;

import android.hardware.SensorManager;

import com.fanwe.app.App;
import com.miguo.live.interf.VideoOrientationEventListener;

/**
 * Created by didik on 2016/7/26.
 * 屏幕方向控制类
 */
public class LiveOrientationHelper {

    public LiveOrientationHelper(){
        registerOrientationListener();
    }
    /**
     * 屏幕方向改变
     */
    private VideoOrientationEventListener mOrientationEventListener;

    public void registerOrientationListener() {
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new VideoOrientationEventListener(App.getInstance(), SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void startOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.enable();
        }
    }

    public void stopOrientationListener() {
        if (mOrientationEventListener != null) {
            mOrientationEventListener.disable();
        }
    }
}
