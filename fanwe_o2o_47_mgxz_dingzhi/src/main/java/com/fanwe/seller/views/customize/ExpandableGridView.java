package com.fanwe.seller.views.customize;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by zhouhy on 2017/1/6.
 */

/**
 * Created by Phil on 2016/8/18.
 */
public class ExpandableGridView extends GridView {

    boolean expanded = true;

    public boolean isExpanded() {
        return expanded;
    }

    public ExpandableGridView(Context context) {
        super(context);
    }

    public ExpandableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded()) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("ExpandableGridView","去噗好吃户口呃呃呃");
        return super.onTouchEvent(ev);
    }
}
