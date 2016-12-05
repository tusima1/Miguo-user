package com.miguo.ui.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.BaseActivity;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiHomeActivity;
import com.miguo.live.views.utils.BaseUtils;

/**
 * Created by Administrator on 2016/11/22.
 */

public class HiLiveListFragmentViewPager extends  ViewPager{

    String tag = "HiLiveListFragmentViewPager";

    public HiLiveListFragmentViewPager(Context context) {
        super(context);
    }

    public HiLiveListFragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        height = height < getMinHeight() ? getMinHeight() : height;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public LinearLayout getTopTextLinearLayout(){
        return (LinearLayout) ((HiHomeActivity)getContext()).findViewById(R.id.top_text_layout);
    }

    public BarryTab getTab(){
        return (BarryTab) ((HiHomeActivity)getContext()).findViewById(R.id.tab);
    }

    public int getTabHeight(){
        return null != getTab() ? getTab().getMeasuredHeight() : 0;
    }

    public int getTopTextLinearLayoutHeight(){
        return null != getTopTextLinearLayout() ? getTopTextLinearLayout().getMeasuredHeight() : 0;
    }

    public int getMinHeight(){
        return  BaseUtils.getHeight(getContext()) - getTopTextLinearLayoutHeight() - getTabHeight();
    }

}
