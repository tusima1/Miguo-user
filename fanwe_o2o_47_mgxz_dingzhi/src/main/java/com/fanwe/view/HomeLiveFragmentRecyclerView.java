package com.fanwe.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.miguo.live.views.utils.BaseUtils;

/**
 * Created by Administrator on 2016/9/23.
 */
public class HomeLiveFragmentRecyclerView extends RecyclerView{

    int downX = 0;
    int downY = 0;
    int touchSlop = 0;
    int moveY = 0;
    int halfScreenHeight = 0;
    int screenHeight = 0;

    public HomeLiveFragmentRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public HomeLiveFragmentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeLiveFragmentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.halfScreenHeight = BaseUtils.getHeight(getContext()) / 2;
        this.screenHeight = BaseUtils.getHeight(getContext());
//        setVerticalScrollBarEnabled(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int)e.getRawX();
                downY = (int)e.getRawY();
                moveY = (int)e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int)e.getRawY();
                if(Math.abs(moveY - downY) > touchSlop){
                    getParent().requestDisallowInterceptTouchEvent(false);
//                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(Math.abs(moveY - downY) > touchSlop){
                    getParent().requestDisallowInterceptTouchEvent(false);
//                    return true;
                }
                break;

        }
        return true;
    }
}
