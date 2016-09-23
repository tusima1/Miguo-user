package com.fanwe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.miguo.live.views.utils.BaseUtils;

/**
 * Created by zlh on 2016/9/23.
 * 解决RecyclerView和PullToRefresh滑动冲突
 */
public class RecyclerPullToRefreshScrollView extends PullToRefreshScrollView {

    int downX = 0;
    int downY = 0;
    int touchSlop = 0;
    int moveY = 0;
    int halfScreenHeight = 0;
    int screenHeight = 0;

    public RecyclerPullToRefreshScrollView(Context context) {
        super(context);
    }

    public RecyclerPullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerPullToRefreshScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public RecyclerPullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    private void init(Context context){
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.halfScreenHeight = BaseUtils.getHeight(getContext()) / 2;
        this.screenHeight = BaseUtils.getHeight(getContext());
        setVerticalScrollBarEnabled(false);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
////        return super.onInterceptTouchEvent(ev);
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downX = (int)ev.getRawX();
//                downY = (int)ev.getRawY();
//                moveY = (int)ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveY = (int)ev.getRawY();
//                if(Math.abs(moveY - downY) > touchSlop){
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if(Math.abs(moveY - downY) > touchSlop){
//                    return true;
//                }
//                break;
//
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
