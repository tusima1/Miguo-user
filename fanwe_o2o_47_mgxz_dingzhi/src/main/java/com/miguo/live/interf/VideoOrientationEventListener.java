package com.miguo.live.interf;

import android.content.Context;
import android.view.OrientationEventListener;

import com.tencent.av.utils.PhoneStatusTools;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;

/**
 * Created by didik on 2016/7/25.
 */
public class VideoOrientationEventListener extends OrientationEventListener {
    boolean mbIsTablet = false;
    int mRotationAngle = 0;

    public VideoOrientationEventListener(Context context, int rate) {
        super(context, rate);
        mbIsTablet = PhoneStatusTools.isTablet(context);
    }

    int mLastOrientation = -25;

    /**
     *
     * @param orientation 方向改变
     */
    @Override
    public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            mLastOrientation = orientation;
            return;
        }

        if (mLastOrientation < 0) {
            mLastOrientation = 0;
        }

        if (((orientation - mLastOrientation) < 20)
                && ((orientation - mLastOrientation) > -20)) {
            return;
        }

        if (mbIsTablet) {
            orientation -= 90;
            if (orientation < 0) {
                orientation += 360;
            }
        }
        mLastOrientation = orientation;

        if (orientation > 314 || orientation < 45) {
            if (QavsdkControl.getInstance() != null) {
                QavsdkControl.getInstance().setRotation(0);
            }
            mRotationAngle = 0;
        } else if (orientation > 44 && orientation < 135) {
            if (QavsdkControl.getInstance() != null) {
                QavsdkControl.getInstance().setRotation(90);
            }
            mRotationAngle = 90;
        } else if (orientation > 134 && orientation < 225) {
            if (QavsdkControl.getInstance() != null) {
                QavsdkControl.getInstance().setRotation(180);
            }
            mRotationAngle = 180;
        } else {
            if (QavsdkControl.getInstance() != null) {
                QavsdkControl.getInstance().setRotation(270);
            }
            mRotationAngle = 270;
        }
    }
}
