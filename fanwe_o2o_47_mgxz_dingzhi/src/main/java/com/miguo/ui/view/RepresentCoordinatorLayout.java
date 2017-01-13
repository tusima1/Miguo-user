package com.miguo.ui.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zlh on 2017/1/12.
 */

public class RepresentCoordinatorLayout extends CoordinatorLayout {

    String tag = getClass().getSimpleName();

    public RepresentCoordinatorLayout(Context context) {
        super(context);
    }

    public RepresentCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RepresentCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                Log.d(tag, "move y: " + ev.getY());
                break;
        }
        return super.onTouchEvent(ev);
    }
}
