package com.miguo.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.miguo.model.TouchToMoveListener;

/**
 * Created by Administrator on 2017/1/5.
 */

public class RepresentViewPager extends ViewPager {

    TouchToMoveListener touchToMoveListener;
    PtrFrameLayoutForViewPager ptrFrameLayout;


    public RepresentViewPager(Context context) {
        super(context);
    }

    public RepresentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                handlerActionDown(ev);

                break;
            case MotionEvent.ACTION_MOVE:
                handlerActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handlerActionCancel(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void handlerActionDown(MotionEvent ev){
        if(ptrFrameLayout != null){
            ptrFrameLayout.requestDisallowInterceptTouchEvent2(true);
        }
        if(touchToMoveListener != null){
            touchToMoveListener.onActionDown(ev);
        }
    }

    public void handlerActionMove(MotionEvent ev){
        if(ptrFrameLayout != null){
            ptrFrameLayout.requestDisallowInterceptTouchEvent2(true);
        }
        if(touchToMoveListener != null){
            touchToMoveListener.onActionDown(ev);
        }

        if(touchToMoveListener != null){
            touchToMoveListener.onActionMove(ev);
        }

    }

    public void handlerActionCancel(MotionEvent ev){
        if(ptrFrameLayout != null){
            ptrFrameLayout.requestDisallowInterceptTouchEvent2(false);
        }
        if(touchToMoveListener != null){
            touchToMoveListener.onActionCancel(ev);
        }
    }

    public void setTouchToMoveListener(TouchToMoveListener touchToMoveListener) {
        this.touchToMoveListener = touchToMoveListener;
    }

    public void setPtrFrameLayout(PtrFrameLayoutForViewPager ptrFrameLayout) {
        this.ptrFrameLayout = ptrFrameLayout;
    }
}
