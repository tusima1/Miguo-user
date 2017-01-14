package com.miguo.model;

import android.view.MotionEvent;

/**
 * Created by zlh on 2017/1/14.
 */

public interface TouchToMoveListener {

    void onActionDown(MotionEvent ev);
    void onActionMove(MotionEvent ev);
    void onActionCancel(MotionEvent ev);

}
