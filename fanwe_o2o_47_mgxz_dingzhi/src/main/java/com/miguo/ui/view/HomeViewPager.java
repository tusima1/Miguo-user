package com.miguo.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by zlh/Barry/狗蛋哥 on 16/8/18.
 */
public class HomeViewPager extends ViewPager {

    String tag = "HomeViewPager";
    int pagerSelect = 0;

    public HomeViewPager(Context context) {
        super(context);
        init();
    }

    public HomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        setPagerSelect(0);
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPagerSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    public void setCurrentItem(int position){
        setPagerSelect(-2);
        super.setCurrentItem(position);
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        Log.d(tag, "screen state: " + screenState);
        super.onScreenStateChanged(screenState);
    }

    /**
     * 禁止ViewPager滑动
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
        if(getPagerSelect() == -2){
            super.scrollTo(x,y);
        }
        super.scrollTo(x,y);

    }

    public int getPagerSelect() {
        return pagerSelect;
    }

    public void setPagerSelect(int pagerSelect) {
        this.pagerSelect = pagerSelect;
    }
}


