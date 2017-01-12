package com.fanwe.seller.views.customize;

/**
 * Created by Administrator on 2017/1/11.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.fanwe.o2o.miguo.R;

import java.lang.reflect.Field;


/**
 * Created by whiskeyfei on 15-7-22.
 */
public class PointView extends LinearLayout implements ViewPager.OnPageChangeListener{
    private static final String TAG = "PointView";
    private Context mContext;
    private int mCount;
    private int mCurrentPos = -1;
    private ViewPager.OnPageChangeListener mPointViewChangeListener;
    private ViewPager mViewPager;

    public PointView(Context context) {
        super(context);
        init(context);
    }

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    public void setParams(ViewPager viewPager,int count) {
        mViewPager = viewPager;
        mPointViewChangeListener = getOnPageChangeListener(mViewPager);
        mViewPager.setOnPageChangeListener(this);
        mCount = mViewPager.getAdapter().getCount();
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        removeAllViews();
        ImageView icon = null;
        for (int i = 0; i < mCount; i++){
            icon = new ImageView(mContext);
            icon.setImageResource(R.drawable.bg_global_search_textbox_gray);
            LayoutParams lp = new LayoutParams(20, 20);
            lp.leftMargin = 5;
            lp.rightMargin = 5;
            addView(icon,lp);
        }
        Log.d(TAG,">>>>>"+mViewPager.getCurrentItem());
        updatePoint(mViewPager.getCurrentItem());
    }

    private void updatePoint(int position) {
        Log.d(TAG,">>>mCurrentPos"+mCurrentPos);
        if (mCurrentPos != position) {
            if (mCurrentPos == -1){
                ((ImageView) getChildAt(position)).setImageResource(R.drawable.bg_global_search_textbox_home);
                mCurrentPos = position;
                return;
            }
            ((ImageView) getChildAt(mCurrentPos)).setImageResource(R.drawable.bg_global_search_textbox_gray);
            ((ImageView) getChildAt(position)).setImageResource(R.drawable.bg_global_search_textbox_home);
            mCurrentPos = position;
        }
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener(ViewPager pager) {
        try {
            Field f = pager.getClass().getDeclaredField("mOnPageChangeListener");
            f.setAccessible(true);
            return (ViewPager.OnPageChangeListener) f.get(pager);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPointViewChangeListener!=null)
            mPointViewChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        updatePoint(position);
        Log.d(TAG,">>>pos"+position);
        if (mPointViewChangeListener!=null)
            mPointViewChangeListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPointViewChangeListener!=null)
            mPointViewChangeListener.onPageScrollStateChanged(state);
    }
}

