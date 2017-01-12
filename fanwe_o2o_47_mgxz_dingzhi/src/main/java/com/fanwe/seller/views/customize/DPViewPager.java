package com.fanwe.seller.views.customize;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义viewpager重新 计算子类的高度。
 * Created by zhouhy on 2017/1/6.
 */

public class DPViewPager extends ViewPager {
    public boolean isNeedScroll = true;
    public DPViewPager(Context context) {
        super(context);
    }

    public DPViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height =0;

        for(int i  =0;i < getChildCount();i++) {

            View child = getChildAt(i);

            child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

            int h = child.getMeasuredHeight();

            if(h > height)

                height = h;

        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!isNeedScroll){
            return isNeedScroll;
        }else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isNeedScroll){
            return isNeedScroll;
        }else {
            return super.onTouchEvent(ev);
        }
    }

    public boolean isNeedScroll() {
        return isNeedScroll;
    }

    public void setNeedScroll(boolean needScroll) {
        isNeedScroll = needScroll;
    }
}

