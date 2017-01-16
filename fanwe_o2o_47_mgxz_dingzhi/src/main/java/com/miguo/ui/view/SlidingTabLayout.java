package com.miguo.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.utils.BaseUtils;


/**
 * Created by 狗蛋哥 on 2016/11/22.
 * 跟随Viewpager滚动的Tab View
 */
public class SlidingTabLayout extends HorizontalScrollView {
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS_TB = 8; //tab padding值：原始值 15
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 14; //tab中字体大小
    private int titleOffset;

    private int tabViewLayoutId;
    private int tabViewTextViewId;

    private ViewPager viewPager;
    private SlidingTabView tabStrip;
    private int width ;
    private String tag = "SlidingTabLayout";

    int singleTabWidth = 73;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        tabStrip = new SlidingTabView(context);
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        titleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);
        addView(tabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        width = BaseUtils.getWidth();
    }

    public void setViewPager(ViewPager viewPager,int pageNum) {
        tabStrip.removeAllViews();
        this.viewPager = viewPager;
        setTabWidth(pageNum);
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }
    @SuppressLint("NewApi")
    private void populateTabStrip() {
        final PagerAdapter adapter = viewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        /*通过 viewPager 的 item 来确定tab 的个数*/
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (tabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(tabViewLayoutId, tabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(tabViewTextViewId);
            }

            if (tabView == null) {
                /*创建textView*/
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
//          tabView.setBackgroundResource(R.drawable.item_selector_bg);
            /*把 textView 放入到自定义的 tab 栏中*/
            tabStrip.addView(tabView);
        }
    }

    @SuppressLint("NewApi")
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            textView.setAllCaps(true);
        }

        /**
         * 设置TabTextViewpadding top and bottom
         */
        int paddingTB = (int) (TAB_VIEW_PADDING_DIPS_TB * getResources().getDisplayMetrics().density);
        textView.setPadding(0, paddingTB, 0, paddingTB);
//     textView.setTextColor(0xff666666); //设置紫日颜色
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_a6));
        Log.d(tag, "sing tab width: " + singleTabWidth);
//        int width = (int) (singleTabWidth * getResources().getDisplayMetrics().density); //singleTabWidth: 这里设置TextView的宽度 defalut 100
        int width = (int) singleTabWidth; //singleTabWidth: 这里设置TextView的宽度 defalut 100
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT) ;
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 这个方法是关键
     * 滚动 scrollview
     * @param tabIndex
     * @param positionOffset
     */
    @SuppressLint("NewApi")
    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = tabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        /*获取当前选中的 item*/
        View selectedChild = tabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            /*获取当前 item 的偏移量*/
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            /*item 的宽度*/
            int width = selectedChild.getWidth();
            /*item 距离正中间的偏移量*/
            titleOffset = (int) ((this.width - width)/2.0f);

            if (tabIndex > 0 || positionOffset > 0) {
                /*计算出正在的偏移量*/
                targetScrollX -= titleOffset;
            }
//            Log.v(tag,"this.width： " + this.width) ;
            /*这个时候偏移的量就是屏幕的正中间*/
            scrollTo(targetScrollX, 0);
        }
    }

    private int mScrollState;
    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = tabStrip.getChildCount();

            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            tabStrip.viewPagerChange(position, positionOffset);

            View selectedTitle = tabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0;
            scrollToTab(position, extraOffset);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(tag, "view pager state: " + state);
            mScrollState = state;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                tabStrip.viewPagerChange(position, 0f);
                scrollToTab(position, 0);
            }
        }
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                if (v == tabStrip.getChildAt(i)) {
                    viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public int getPagerState(){
        return mScrollState;
    }

    private void setTabWidth(int num){
        int screenWidth = BaseUtils.getWidth();
        int width = screenWidth / num;
        setSingleTabWidth(width);
    }

    public void setSingleTabWidth(int width){
        this.singleTabWidth = width;
    }


    /**
     * 设置默认字体颜色
     * R.color.white
     * @param color
     */
    public void setNormalTextColor(int color){
        tabStrip.setNormalTextColor(color);
    }

    /**
     * 设置选中字体颜色
     * R.id.color
     * @param color
     */
    public void setSelectTextColor(int color){
        tabStrip.setSelectTextColor(color);
    }

    /**
     * 设置跟随滚动Divider线颜色
     * R.id.color
     * @param color
     */
    public void setDividerColor(int color){
        tabStrip.setDividerColor(color);
    }

    /**
     * 设置底下长线颜色
     * R.id.color
     * @param color
     */
    public void setLineColor(int color){
        tabStrip.setLineColor(color);
    }
}