package com.miguo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Barry on 16/9/24.
 * 解决PtrFrameLayout和ViewPager冲突
 */
public class PtrFrameLayoutForViewPager extends PtrFrameLayout {

    boolean disallowInterceptTouchEvent = false;
    String tag = this.getClass().getSimpleName();

    public PtrFrameLayoutForViewPager(Context context) {
        super(context);
    }

    public PtrFrameLayoutForViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrFrameLayoutForViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void requestDisallowInterceptTouchEvent2(boolean disallowIntercept) {
        setDisallowInterceptTouchEvent(disallowIntercept);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if(isDisallowInterceptTouchEvent()){
            return super.dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    public boolean isDisallowInterceptTouchEvent() {
        return disallowInterceptTouchEvent;
    }

    public void setDisallowInterceptTouchEvent(boolean disallowInterceptTouchEvent) {
        this.disallowInterceptTouchEvent = disallowInterceptTouchEvent;
    }
}
