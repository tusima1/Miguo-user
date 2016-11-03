package com.fanwe.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by didik on 2016/10/19.
 * <p>必须调用此方法setScrollView(ScrollView mParentScrollView)<p/>
 * <p>否则,请使用原生的ScrollView<p/>
 */
public class ListViewForScrollView extends ListView{

    private ScrollView mParentScrollView;
    private boolean mEnable=false;

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnable){
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setParentScrollAble(false);//当手指触到listView的时候，让父ScrollView交出onTouch权限，也就是让父scrollview 停住不能滚动
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setParentScrollAble(true);//当手指松开时，让父ScrollView重新拿到onTouch权限
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        if (mParentScrollView==null){
            Log.e("ListViewForScrollView","mParentScrollView is null,is scrollView inflate?");
            return;
        }
        mParentScrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listView外面的那个scrollview
    }

    public void setScrollView(ScrollView mParentScrollView){
        this.mParentScrollView=mParentScrollView;
    }

    public void setInterceptTouchEventEnable(boolean isEnable){
        this.mEnable=isEnable;
    }
}
