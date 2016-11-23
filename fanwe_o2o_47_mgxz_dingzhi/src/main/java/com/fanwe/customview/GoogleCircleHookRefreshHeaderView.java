package com.fanwe.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.customview.google.GoogleCircleProgressView;
import com.fanwe.o2o.miguo.R;

/**
 * Created by didikee.
 */
public class GoogleCircleHookRefreshHeaderView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private GoogleCircleProgressView progressView;

    private int mTriggerOffset;

    private int mFinalOffset;

    public GoogleCircleHookRefreshHeaderView(Context context) {
        this(context, null);
    }

    public GoogleCircleHookRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleCircleHookRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTriggerOffset = DisplayUtil.dp2px(context,100);
        mFinalOffset = DisplayUtil.dp2px(context,150);

        progressView =new GoogleCircleProgressView(context);
//        android:id="@+id/googleProgress"
//        app:gcp_show_arrow="true"
//        app:gcp_progress_stoke_width="2dp"
//        app:gcp_arrow_height="6dp"
//        app:gcp_arrow_width="8dp"
//        android:layout_width="40dp"
//        android:layout_height="40dp"
//        android:layout_gravity="center"
        progressView.setShowArrow(true);
        progressView.setProgressStokeWidth(DisplayUtil.dp2px(context,2));
        progressView.setArrowWidth(DisplayUtil.dp2px(context,8));
        progressView.setArrowHeight(DisplayUtil.dp2px(context,6));
        FrameLayout.LayoutParams params=new LayoutParams(DisplayUtil.dp2px(context,40),DisplayUtil.dp2px(context,40), Gravity.CENTER);
        addView(progressView,params);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressView.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_red,
                R.color.google_yellow,
                R.color.google_green);
        progressView.setStartEndTrim(0, (float) 0.75);
    }

    @Override
    public void onRefresh() {
        progressView.start();
    }

    @Override
    public void onPrepare() {
        progressView.setStartEndTrim(0, (float) 0.75);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        float alpha = y / (float) mTriggerOffset;
        ViewCompat.setAlpha(progressView, alpha);
        if (!isComplete) {
            progressView.setProgressRotation(y / (float) mFinalOffset);
        }
    }

    @Override
    public void onRelease() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void onComplete() {
        progressView.animate().scaleX(0).scaleY(0).setDuration(300);
    }

    @Override
    public void onReset() {
        progressView.stop();
        ViewCompat.setAlpha(progressView, 1f);
        ViewCompat.setScaleX(progressView, 1f);
        ViewCompat.setScaleY(progressView, 1f);
    }

}
