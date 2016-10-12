package com.fanwe.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.miguo.live.views.utils.BaseUtils;


/**
 * Created by 狗蛋哥/zlh on 16/4/13.
 */
public class RecyclerScrollView extends ScrollView{

    OnRecyclerScrollViewListener onHomeScrollViewListener;
    boolean isLoading = false;

    int currentTop = 0;
    int downX = 0;
    int downY = 0;
    int touchSlop = 0;
    int moveY = 0;
    int halfScreenHeight = 0;
    int screenHeight = 0;
    String tag = RecyclerScrollView.this.getClass().getSimpleName();

    public RecyclerScrollView(Context context) {
        super(context);
    }

    public RecyclerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        this.halfScreenHeight = BaseUtils.getHeight(getContext()) / 2;
        this.halfScreenHeight = 0;
        this.screenHeight = BaseUtils.getHeight(getContext());
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int)ev.getRawX();
                downY = (int)ev.getRawY();
                moveY = (int)ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int)ev.getRawY();
                if(Math.abs(moveY - downY) > touchSlop){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(Math.abs(moveY - downY) > touchSlop){
                    return true;
                }
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(tag, "recycler scroll view action down..");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(onHomeScrollViewListener != null){
            onHomeScrollViewListener.onScrollChanged(l, t , oldl, oldt);
        }
        if(!isLoading()){
            /**
             * 判断滑动距离是否需要加载更多
             */
            if((t + halfScreenHeight + getMeasuredHeight()) > getChildAt(0).getMeasuredHeight()){
                if(onHomeScrollViewListener != null){
                    setIsLoading(true);
                    onHomeScrollViewListener.onScrollToEnd();
                }
            }
        }
        Log.d("HomeScrollView", "l: " + l + " ,t: " + t + " ,oldl: " + oldl + " ,oldt: " + oldt + " ,top: " + getTop() + " ,child measured  height: " + getChildAt(0).getMeasuredHeight() + " show height: " + (getMeasuredHeight() + t));
        setCurrentTop(t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        LogUtil.d("", "onlayout: " + "changed: " + changed + " ,l: " + l + " ,t:" + t + " ,r:" + r + " ,b:" + b )  ;
        super.onLayout(changed, l, t, r, b);
    }

    public interface OnRecyclerScrollViewListener {
        void onScrollToEnd();
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void loadComplite(){
        setIsLoading(false);
    }

    public void setOnRecyclerScrollViewListener(OnRecyclerScrollViewListener onHomeScrollViewListener) {
        this.onHomeScrollViewListener = onHomeScrollViewListener;
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    public int getCurrentTop() {
        return currentTop;
    }

    public void setCurrentTop(int currentTop) {
        this.currentTop = currentTop;
    }

    public boolean canRefresh(){
        return getCurrentTop() == 0;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }
}
