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

public class MultiScrollView extends ScrollView {

    View mTopView;
    View mFlowView;
    View mFakeFlowView;
    View childView;
    View.OnTouchListener childOnTouchListener;

    public MultiScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean consume = false;
        if(childOnTouchListener!=null){
            consume = childOnTouchListener.onTouch(childView,ev);
        }
        return consume&super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mTopView != null && mFlowView != null) {
            int value = mTopView.getHeight();
            if (t >= value) {
                mFlowView.setVisibility(View.VISIBLE);
                mFakeFlowView.setVisibility(View.INVISIBLE);
            } else {
                mFlowView.setVisibility(View.INVISIBLE);
                mFakeFlowView.setVisibility(View.VISIBLE);
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 监听浮动view的滚动状态
     *
     * @param topView  顶部区域view，即当ScrollView滑动的高度要大于等于哪个view的时候隐藏floatview
     * @param flowView 浮动view，即要哪个view停留在顶部
     */
    public void listenerFlowViewScrollState(View topView, View flowView, View mFakeFlowView) {

        this.mTopView = topView;
        this.mFlowView = flowView;
        this.mFakeFlowView = mFakeFlowView;
    }

    /**
     * 当滑动发生变化的时候显示或者隐藏对应的VIEW.
     */
    public interface ScrollChangedFlowViewListener {
        public void ifShowFlowView(boolean show);
    }

    public OnTouchListener getChildOnTouchListener() {
        return childOnTouchListener;
    }

    public void setChildOnTouchListener(OnTouchListener childOnTouchListener) {
        this.childOnTouchListener = childOnTouchListener;
    }

    public View getChildView() {
        return childView;
    }

    public void setChildView(View childView) {
        this.childView = childView;
    }
}
