package com.miguo.live.views.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by didik on 2016/7/25.
 * 高度最大
 */
public class MaxHeightGridView extends GridView {
    public MaxHeightGridView(Context context) {
        super(context);
    }

    public MaxHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
