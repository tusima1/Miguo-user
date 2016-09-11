package com.miguo.live.views.Manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by didik on 2016/9/11.
 * 禁止水平或者垂直滑动的GridLayoutManager
 */
public class FalseScrollGridLayoutManager extends GridLayoutManager {

    private boolean mScrollHorizontally;
    private boolean mScrollVertically;

    public FalseScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FalseScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FalseScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollHorizontally(boolean scrollHorizontally){
        this.mScrollHorizontally=scrollHorizontally;
    }

    public void setScrollVertically(boolean scrollVertically){
        this.mScrollVertically=scrollVertically;
    }

    @Override
    public boolean canScrollHorizontally() {
        return mScrollHorizontally && super.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return mScrollVertically && super.canScrollVertically();
    }
}
