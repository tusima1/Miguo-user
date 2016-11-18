package com.fanwe.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by didik on 2016/11/18.
 */

public class SPullToRefreshSScrollView extends PullToRefreshScrollView {
    public SPullToRefreshSScrollView(Context context) {
        super(context);
    }

    public SPullToRefreshSScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SPullToRefreshSScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public SPullToRefreshSScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        SScrollView sScrollView=new SScrollView(context);
        sScrollView.setId(com.handmark.pulltorefresh.library.R.id.scrollview);
        return sScrollView;
    }
}
