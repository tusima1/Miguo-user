package com.miguo.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.fanwe.view.RecyclerScrollView;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by zlh/Barry/狗蛋哥 on 16/8/19.
 */
public class HomeBannerViewPager extends ViewPager {

    RecyclerScrollView recyclerScrollView;
    ViewGroup ptrFrameLayout;
    String tag = HomeBannerViewPager.class.getSimpleName();

    public HomeBannerViewPager(Context context) {
        super(context);
    }

    public HomeBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                LogUtil.d(tag, "action down...");
                if(ptrFrameLayout != null){
                    ptrFrameLayout.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                LogUtil.d(tag, "action move...");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(ptrFrameLayout != null){
                    ptrFrameLayout.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ptrFrameLayout != null){
            ptrFrameLayout.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ptrFrameLayout != null){
            ptrFrameLayout.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setRecyclerScrollView(RecyclerScrollView recyclerScrollView) {
        this.recyclerScrollView = recyclerScrollView;
    }

    public void setPtrFrameLayout(PtrFrameLayout ptrFrameLayout) {
        this.ptrFrameLayout = ptrFrameLayout;
    }
}
