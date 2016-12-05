package com.fanwe.customview.google;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.didikee.uilibs.utils.DisplayUtil;

/**
 * Created by aspsine on 15/9/10.
 */
public class GoogleRefreshHeaderView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private ImageView ivRefresh;

    private int mTriggerOffset;

    private RingProgressDrawable ringProgressDrawable;

    public GoogleRefreshHeaderView(Context context) {
        this(context, null);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ringProgressDrawable = new RingProgressDrawable(context);
        ringProgressDrawable.setColors(Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN);
        mTriggerOffset = DisplayUtil.dp2px(context,100);
        int size = DisplayUtil.dp2px(context,40);
        ivRefresh=new ImageView(context);
        ivRefresh.setBackground(ringProgressDrawable);
        FrameLayout.LayoutParams params=new LayoutParams(size,size, Gravity.CENTER);
        addView(ivRefresh,params);
    }

//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
//        ivRefresh.setBackgroundDrawable(ringProgressDrawable);
//    }

    @Override
    public void onRefresh() {
        ringProgressDrawable.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ringProgressDrawable.setPercent(y / (float) mTriggerOffset);
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        ringProgressDrawable.stop();
    }

    @Override
    public void onReset() {

    }
}
