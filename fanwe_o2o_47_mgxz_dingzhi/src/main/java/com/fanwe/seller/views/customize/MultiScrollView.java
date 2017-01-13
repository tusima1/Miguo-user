package com.fanwe.seller.views.customize;

/**
 * Created by Administrator on 2017/1/11.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ScrollView;

/**
 * 自定义复杂的scrollview 里面可以潜逃horizontalscrollview  layout悬浮。。。
 * Created by zhouhy on 2017/1/9.
 */

public class MultiScrollView  extends ScrollView {

    View mTopView;
    View mFlowView;


    public MultiScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("MultiScrollView"," onInterceptTouchEvent hehe");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("MultiScrollView"," onTouchEvent hehe"+ev.getAction());
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        Log.d("MultiScrollView","l:"+l +" t:"+t+"oldl:"+oldl+"oldt:"+oldt);
        Log.d("MultiScrollView"," onScrollChanged mTopView.getHeight()："+mTopView.getHeight() );

        super.onScrollChanged(l, t, oldl, oldt);
        if (mTopView != null && mFlowView != null) {
            int value = mTopView.getHeight();
            if (t >= value) {
                mFlowView.setVisibility(View.VISIBLE);
            } else {
                mFlowView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 监听浮动view的滚动状态
     *
     * @param topView  顶部区域view，即当ScrollView滑动的高度要大于等于哪个view的时候隐藏floatview
     * @param flowView 浮动view，即要哪个view停留在顶部
     */
    public void listenerFlowViewScrollState(View topView, View flowView) {

        mTopView = topView;
        mFlowView = flowView;
    }
}