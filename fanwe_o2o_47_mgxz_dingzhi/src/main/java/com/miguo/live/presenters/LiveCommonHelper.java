package com.miguo.live.presenters;

import android.app.Activity;
import android.widget.Toast;

import com.miguo.live.interf.IHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;

/**
 * Created by didik on 2016/7/30.
 */
public class LiveCommonHelper implements IHelper {

    private Activity mActivity;
    private LiveHelper mLiveHelper;

    public LiveCommonHelper(LiveHelper liveHelper, Activity activity) {
        this.mLiveHelper=liveHelper;
        this.mActivity=activity;
    }

    /**
     * 开启闪光灯
     */
    public void openLighting(){
        //开启闪光灯
        if (mLiveHelper.isFrontCamera() == true) {
            Toast.makeText(mActivity, "当前为前置摄像头,无法开启闪光灯!", Toast.LENGTH_SHORT).show();
        } else {
            mLiveHelper.toggleFlashLight();
        }
    }

    /**
     * 切换摄像头
     */
    public void switchCamera(){
        mLiveHelper.switchCamera();
    }

    /**
     * 开关录音功能
     */
    public void doMic(){
        if (mLiveHelper.isMicOpen() == true) {
            mLiveHelper.muteMic();
        } else {
            mLiveHelper.openMic();
        }
    }
    @Override
    public void onDestroy() {
        mLiveHelper=null;
    }
}
