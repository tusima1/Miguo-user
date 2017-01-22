package com.fanwe.user.presents;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.utils.SxbLog;


/**
 * Created by Administrator on 2016/7/22.
 */
public class LoginHelper {
    private static final String TAG = LoginHelper.class.getSimpleName();

    /**
     * 退出imsdk
     * <p>
     * 退出成功会调用退出AVSDK
     */
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                SxbLog.e(TAG, "IMLogout fail ：" + i + " msg " + s);
            }

            @Override
            public void onSuccess() {
                SxbLog.i(TAG, "IMLogout succ !");
                //反向初始化avsdk
                stopAVSDK();
            }
        });

    }

    /**
     * 反初始化AVADK
     */
    public void stopAVSDK() {
        QavsdkControl.getInstance().stopContext();
    }

}
