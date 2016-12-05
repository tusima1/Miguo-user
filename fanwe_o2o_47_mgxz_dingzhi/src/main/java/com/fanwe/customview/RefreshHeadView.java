package com.fanwe.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.customview.google.MaterialProgressDrawable;

/**
 * Created by didik on 2016/11/21.
 */

public class RefreshHeadView extends FrameLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    private MaterialProgressDrawable materialProgressDrawable;
    private int height;

    public RefreshHeadView(Context context) {
        super(context);
    }

    public RefreshHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CircleImageView circleImageView=new CircleImageView(context,Color.TRANSPARENT,20);
        materialProgressDrawable = new MaterialProgressDrawable(context,this);
        materialProgressDrawable.setBackgroundColor(Color.TRANSPARENT);
        materialProgressDrawable.setColorSchemeColors(Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN);
        height= DisplayUtil.dp2px(context,100);
        circleImageView.setImageDrawable(materialProgressDrawable);
        addView(circleImageView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onPrepare() {

    }


    @Override
    public void onMove(int i, boolean isComplete, boolean b1) {
        if (!isComplete) {
//            materialProgressDrawable.setRotation(i/height);
        }
    }

    @Override
    public void onRelease() {
        materialProgressDrawable.start();
    }

    @Override
    public void onComplete() {
        materialProgressDrawable.stop();
    }

    @Override
    public void onReset() {

    }
}
