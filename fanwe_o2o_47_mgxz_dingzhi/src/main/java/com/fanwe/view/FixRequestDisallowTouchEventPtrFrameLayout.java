package com.fanwe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2016/10/11.
 */
public class FixRequestDisallowTouchEventPtrFrameLayout extends PtrFrameLayout{

    private boolean disallowInterceptTouchEvent = false;
    String tag = FixRequestDisallowTouchEventPtrFrameLayout.class.getSimpleName();

    public FixRequestDisallowTouchEventPtrFrameLayout(Context context) {
        super(context);
    }

    public FixRequestDisallowTouchEventPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixRequestDisallowTouchEventPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
       super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public void requestDisallowInterceptTouchEvent2(boolean disallowIntercept) {
        setDisallowInterceptTouchEvent(disallowIntercept);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public void setDisallowInterceptTouchEvent(boolean disallowInterceptTouchEvent) {
        this.disallowInterceptTouchEvent = disallowInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                handlerActionDown();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handlerActionDown(){
        Log.d(tag, "handler action down..");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

}
