package com.fanwe.seller.views.customize;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.fanwe.seller.adapters.TypeHorizontalScrollViewAdapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhouhy on 2017/1/6.
 */

public class TypeHorizontalScrollView extends HorizontalScrollView implements
        View.OnClickListener {

    private int lastPosition = -1;
    private View oldView = null;

    /**
     * 一级类别滚动时的回调接口
     *
     * @author zhy
     */
    public interface CurrentTypeChangeListener {
        void onCurrentTypeChanged(View oldView, int position, View viewIndicator);
    }

    /**
     * 条目点击时的回调
     *
     * @author zhy
     */
    public interface OnItemClickListener {
        void onClick(View lastView, View view, int pos);
    }

    private CurrentTypeChangeListener mListener;

    private OnItemClickListener mOnClickListener;

    private static final String TAG = "TypeHorizontal";

    /**
     * HorizontalListView中的LinearLayout
     */
    private LinearLayout mContainer;

    /**
     * 子元素的宽度
     */
    private int mChildWidth;
    /**
     * 子元素的高度
     */
    private int mChildHeight;
    /**
     * 当前最后一张图片的index
     */
    private int mCurrentIndex;
    /**
     * 当前第一张图片的下标
     */
    private int mFristIndex;
    /**
     * 当前第一个View
     */
    private View mFirstView;
    /**
     * 数据适配器
     */
    private TypeHorizontalScrollViewAdapter mAdapter;
    /**
     * 每屏幕最多显示的个数
     */
    private int mCountOneScreen;
    /**
     * 屏幕的宽度
     */
    private int mScreenWitdh;


    /**
     * 保存View与位置的键值对
     */
    private Map<View, Integer> mViewPos = new HashMap<View, Integer>();
    private Map<Integer,View> mPosView=new HashMap<>();

    public TypeHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获得屏幕宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWitdh = outMetrics.widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
        mContainer = (LinearLayout) getChildAt(0);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = View.MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = View.MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = View.MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    /**
     * 加载下一张图片
     */
    protected void loadNextImg() {
        // 数组边界值计算
        if (mCurrentIndex == mAdapter.getCount() - 1) {
            return;
        }
        //移除第一张图片，且将水平滚动位置置0
        scrollTo(17, 0);
        mViewPos.remove(mContainer.getChildAt(0));
        mContainer.removeViewAt(0);

        //获取下一张图片，并且设置onclick事件，且加入容器中
        View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
        view.setOnClickListener(this);
        mContainer.addView(view);
        mViewPos.put(view, mCurrentIndex);
        mPosView.put(mCurrentIndex,view);

        //当前第一张图片小标
        mFristIndex++;
        //如果设置了滚动监听则触发
        if (mListener != null) {
            notifyCurrentImgChanged();
        }

    }

    /**
     * 加载前一张图片
     */
    protected void loadPreImg() {
        //如果当前已经是第一张，则返回
        if (mFristIndex == 0)
            return;
        //获得当前应该显示为第一张图片的下标
        int index = mCurrentIndex - mCountOneScreen;
        if (index >= 0) {
//			mContainer = (LinearLayout) getChildAt(0);
            //移除最后一张
            int oldViewPos = mContainer.getChildCount() - 1;
            mViewPos.remove(mContainer.getChildAt(oldViewPos));
            mContainer.removeViewAt(oldViewPos);

            //将此View放入第一个位置
            View view = mAdapter.getView(index, null, mContainer);
            mViewPos.put(view, index);
            mPosView.put(index,view);
            mContainer.addView(view, 0);
            view.setOnClickListener(this);
            //水平滚动位置向左移动view的宽度个像素
            scrollTo(mChildWidth, 0);
            //当前位置--，当前第一个显示的下标--
            mCurrentIndex--;
            mFristIndex--;
            //回调
            if (mListener != null) {
                notifyCurrentImgChanged();

            }
        }
    }

    /**
     * 滑动时的回调
     */
    public void notifyCurrentImgChanged() {
        //先清除所有的背景色，点击时会设置为蓝色
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        initOldView();
        mListener.onCurrentTypeChanged(oldView, mFristIndex, mContainer.getChildAt(0));

    }

    /**
     * 初始化数据，设置数据适配器
     *
     * @param mAdapter
     */
    public void initDatas(TypeHorizontalScrollViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mContainer = (LinearLayout) getChildAt(0);
        // 获得适配器中第一个View
        final View view = mAdapter.getView(0, null, mContainer);
        mContainer.addView(view);

        // 强制计算当前View的宽和高
        if (mChildWidth == 0 && mChildHeight == 0) {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = 57 + 26;
            Log.d(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
            mChildHeight = view.getMeasuredHeight();
            // 计算每次加载多少个View
            mCountOneScreen = (mScreenWitdh - 17) / mChildWidth + 2;

            Log.e(TAG, "mCountOneScreen = " + mCountOneScreen
                    + " ,mChildWidth = " + mChildWidth);
        }
        //初始化第一屏幕的元素
        initFirstScreenChildren(mCountOneScreen);
    }

    /**
     * 加载第一屏的View
     *
     * @param mCountOneScreen
     */
    public void initFirstScreenChildren(int mCountOneScreen) {
        mContainer = (LinearLayout) getChildAt(0);
        mContainer.removeAllViews();
        mViewPos.clear();
        int count = mAdapter.getCount();

        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i, null, mContainer);
            view.setOnClickListener(this);
            mContainer.addView(view);
            mViewPos.put(view, i);
            mPosView.put(i,view);
            mCurrentIndex = i;
        }
        oldView =mContainer.getChildAt(mFristIndex);
        if (mListener != null) {
            notifyCurrentImgChanged();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
//			Log.e(TAG, getScrollX() + "");

                int scrollX = getScrollX();
                // 如果当前scrollX为view的宽度，加载下一张，移除第一张
                if (scrollX >= mChildWidth) {
                    loadNextImg();
                }
                // 如果当前scrollX = 0， 往前设置一张，移除最后一张
                if (scrollX == 0) {
                    loadPreImg();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void initOldView() {
        if (oldView == null) {
            Iterator iter = mViewPos.entrySet().iterator();
            if (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                oldView = (View) key;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
            initOldView();

            mOnClickListener.onClick(oldView, v, mViewPos.get(v));
            lastPosition = mViewPos.get(v);
            oldView = v;
        }
    }

    /**
     * 滚动到某一位置。
     * @param index
     */
    public void scrollToIndex(int index){
        if(mPosView!=null&&mPosView.get(index)!=null){
            onClick(mPosView.get(index));
        }
    }
    public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setCurrentTypeChangeListener(
            CurrentTypeChangeListener mListener) {
        this.mListener = mListener;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getmFristIndex() {
        return mFristIndex;
    }

    public void setmFristIndex(int mFristIndex) {
        this.mFristIndex = mFristIndex;
    }
}