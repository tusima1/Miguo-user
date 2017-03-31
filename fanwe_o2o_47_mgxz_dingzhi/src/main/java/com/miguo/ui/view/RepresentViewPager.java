package com.miguo.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fanwe.view.FixRequestDisallowTouchEventPtrFrameLayout;
import com.miguo.model.TouchToMoveListener;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2017/1/5.
 */

public class RepresentViewPager extends ViewPager {

    TouchToMoveListener touchToMoveListener;
    PtrFrameLayout ptrFrameLayout;


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
            requestDisallowInterceptTouchEvent2(true);
        }
        if(touchToMoveListener != null){
            touchToMoveListener.onActionDown(ev);
        }
    }

    public void handlerActionMove(MotionEvent ev){
        if(ptrFrameLayout != null){
            requestDisallowInterceptTouchEvent2(true);
        }
        if(touchToMoveListener != null){
            touchToMoveListener.onActionDown(ev);
        }

        if(touchToMoveListener != null){
            touchToMoveListener.onActionMove(ev);
        }
    }

    public void requestDisallowInterceptTouchEvent2(boolean requestDisallowInterceptTouchEvent){
        if(ptrFrameLayout instanceof PtrFrameLayoutForViewPager){
            getPtrFrameLayoutForViewPager().requestDisallowInterceptTouchEvent2(requestDisallowInterceptTouchEvent);
        }
        if(ptrFrameLayout instanceof FixRequestDisallowTouchEventPtrFrameLayout){
            getFixRequestDisallowTouchEventPtrFrameLayout().requestDisallowInterceptTouchEvent2(requestDisallowInterceptTouchEvent);
        }
    }

    public PtrFrameLayoutForViewPager getPtrFrameLayoutForViewPager(){
        return (PtrFrameLayoutForViewPager)ptrFrameLayout;
    }

    public FixRequestDisallowTouchEventPtrFrameLayout getFixRequestDisallowTouchEventPtrFrameLayout(){
        return (FixRequestDisallowTouchEventPtrFrameLayout)ptrFrameLayout;
    }

    public void handlerActionCancel(MotionEvent ev){
        if(ptrFrameLayout != null){
            requestDisallowInterceptTouchEvent2(false);
        }
        if(touchToMoveListener != null){
            touchToMoveListener.onActionCancel(ev);
        }
    }

    public void setTouchToMoveListener(TouchToMoveListener touchToMoveListener) {
        this.touchToMoveListener = touchToMoveListener;
    }

    public void setPtrFrameLayout(PtrFrameLayout ptrFrameLayout) {
        this.ptrFrameLayout = ptrFrameLayout;
    }
}
