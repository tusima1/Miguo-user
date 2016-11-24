package com.miguo.ui.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

    public AppBarLayout getAppbarLayout(){
//        ViewGroup viewGroup = (ViewGroup) getParent();
//        for(int i = 0; i<8; i++){
//            viewGroup = (ViewGroup) viewGroup.getParent();
//            if(viewGroup instanceof CoordinatorLayout){
//                return (AppBarLayout)(viewGroup).getChildAt(0);
//            }
//        }
        ;
        return (AppBarLayout) (((CoordinatorLayout)((HiHomeActivity)getContext()).findViewById(R.id.coorddinatorlayout)).getChildAt(0));
//        return null;
    }

    public int getMinHeight(){
        return  null != getAppbarLayout() ? BaseUtils.getHeight(getContext()) - getAppbarLayout().getMeasuredHeight() : 0;
    }

}
