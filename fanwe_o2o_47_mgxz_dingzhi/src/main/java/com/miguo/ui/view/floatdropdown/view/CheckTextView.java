package com.miguo.ui.view.floatdropdown.view;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.floatdropdown.interf.OnCheckChangeListener;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class CheckTextView extends FrameLayout implements Checkable, View.OnClickListener {

    private boolean isChecked = false;
    protected TextView textView;
    private View actView;
    private int duration = 150;

    private float locationX;
    private float locationY;

    private int checkedTextColor = Color.WHITE;
    private int uncheckedTextColor;

    private Object holdSome;
    private OnCheckChangeListener checkChangeListener;

    public CheckTextView(Context context) {
        this(context, null);
    }

    public CheckTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBaseParams();
        setBackgroundDrawable(getResources().getDrawable(R.drawable
                .shape_cricle_5_solid_white_stroke_cc));
    }


    private void setBaseParams() {
        uncheckedTextColor = Color.parseColor("#CCCCCC");

        textView = setTextView();
        actView = setActView();
        addView(actView);
        addView(textView);
        actView.setVisibility(GONE);
        setOnClickListener(this);
    }

    protected TextView setTextView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(12);
        textView.setText("--");
        textView.setTextColor(uncheckedTextColor);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    protected View setActView() {
        View view = new View(getContext());
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable
                .shape_cricle_5_solid_f5b830));
        return view;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            toggle();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            locationX = event.getX();
            locationY = event.getY();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        isChecked = !isChecked;
        if (isAttachedToWindow() && Build.VERSION.SDK_INT >= Build
                .VERSION_CODES.LOLLIPOP) {
            startAnimate();
        } else {
            actView.setVisibility(isChecked ? VISIBLE : GONE);
        }
        textView.setTextColor(isChecked ? checkedTextColor : uncheckedTextColor);
        if (checkChangeListener!=null){
            checkChangeListener.changed(v,isChecked);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startAnimate() {
        int width = getWidth();
        int height = getHeight();
        float radius = (float) (Math.sqrt((Math.pow(width, 2) + Math.pow(height, 2))) / 2);
        if (isChecked) {
            Animator open = ViewAnimationUtils.createCircularReveal(actView, (int) locationX,
                    (int) locationY, 0, radius);
            open.setDuration(duration);
            open.setInterpolator(new AccelerateInterpolator());
            open.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    actView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            open.start();
        } else {
            Animator reveal = ViewAnimationUtils.createCircularReveal(actView, (int) locationX,
                    (int) locationY, radius, 0);
            reveal.setDuration(duration);
            reveal.setInterpolator(new DecelerateInterpolator());
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    actView.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            reveal.start();
        }
        Log.e("test", "x: " + locationX + "y: " + locationY);
    }

    /**
     * 放点东西
     * @return 一些数据
     */
    public Object getHold() {
        return holdSome;
    }

    public void setHold(Object some) {
        this.holdSome = some;
    }

    public String getText() {
        if (textView != null) {
            return textView.getText().toString();
        }
        return null;
    }
    public void setText(CharSequence charSequence) {
        textView.setText(charSequence);
    }

    public void setOnCheckChangeListener(OnCheckChangeListener checkChangeListener){
        this.checkChangeListener=checkChangeListener;
    }
}
