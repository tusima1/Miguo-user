package com.fanwe.customview.google;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;

/**
 * Created by Aspsine on 2015/11/5.
 */
public class GoogleRingLoadMoreFooterView extends FrameLayout implements SwipeTrigger, SwipeLoadMoreTrigger {
    private ImageView ivLoadMore;

    private int mTriggerOffset;

    private RingProgressDrawable ringProgressDrawable;
    public GoogleRingLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public GoogleRingLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleRingLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ringProgressDrawable = new RingProgressDrawable(context);
        ivLoadMore =new ImageView(context);
        Resources res = getResources();
        ringProgressDrawable.setColors(
                res.getColor(R.color.google_blue),
                res.getColor(R.color.google_red),
                res.getColor(R.color.google_yellow),
                res.getColor(R.color.google_green));
        mTriggerOffset = DisplayUtil.dp2px(context,80);
        FrameLayout.LayoutParams params=new LayoutParams(DisplayUtil.dp2px(context,40),DisplayUtil.dp2px(context,40), Gravity.CENTER);
        addView(ivLoadMore,params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivLoadMore.setBackgroundDrawable(ringProgressDrawable);
    }

    @Override
    public void onLoadMore() {
        ringProgressDrawable.start();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete){
            ringProgressDrawable.setPercent(-y / (float) mTriggerOffset);
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
