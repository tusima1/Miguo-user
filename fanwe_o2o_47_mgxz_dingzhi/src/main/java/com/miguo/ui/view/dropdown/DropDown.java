package com.miguo.ui.view.dropdown;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.miguo.ui.view.dropdown.interf.ExpandReverse;
import com.miguo.ui.view.dropdown.interf.PopupWindowLike;

import java.util.ArrayList;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class DropDown extends LinearLayout implements PopupWindowLike,ExpandReverse {

    private int contentHeight;//内容的高度(规定的)

    private FrameLayout contentLayout;//主内容 的容器
    private boolean isShowing;//是否在展示中

    private ValueAnimator expandAnimator;
    private ValueAnimator reverseAnimator;

    private int expandDuration = 250;
    private int reverseDuration = 300;

    private boolean isAnimating = false;

    private final SparseArray<View> contentViewList=new SparseArray<>();

    private boolean initOk=false;

    public DropDown(Context context) {
        this(context,null);
    }

    public DropDown(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        finalAddView();
    }

    /**
     * 最终添加的view
     */
    private void finalAddView(){
        setOrientation(LinearLayout.VERTICAL);
        contentHeight = dp2px(getContext(),355);
        contentLayout = new FrameLayout(getContext());
        contentLayout.setBackgroundColor(Color.WHITE);
        addView(contentLayout, LayoutParams.MATCH_PARENT,0);
        contentLayout.setVisibility(GONE);
    }

    /**
     * 是否初始化失败,如果数据任何一点缺失即为失败,不响应任何点击事件,亦不产生任何的回调
     * @param initOk 默认ok
     */
    public void setInitOk(boolean initOk) {
        this.initOk = initOk;
    }

    public void onClick(int index) {
        if (!initOk){
            return;//初始化失败
        }
        clickOrder(index);
    }

    /**
     * 点击
     * @param order
     */
    private void clickOrder(int order){
        if (contentViewList==null || contentViewList.size()!=4){
            return;
        }
        contentLayout.removeAllViews();
        contentLayout.setVisibility(VISIBLE);

        contentLayout.addView(contentViewList.get(order),new FrameLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (!isShowing){
            expand();
        }
    }

    public int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void show() {

    }

    @Override
    public void dismiss() {
        reverse();
    }

    public boolean isShowing(){
        return isShowing;
    }

    /**
     * 准备要展示的ContentLayout 里面的View
     */
    public void prepareContentView(ArrayList<View> contentItemLayout){
        if (contentItemLayout==null || contentItemLayout.size()!=4){
            Log.e("test","item view 少了....");
            return;
        }
        contentViewList.put(1,contentItemLayout.get(0));
        contentViewList.put(2,contentItemLayout.get(1));
        contentViewList.put(3,contentItemLayout.get(2));
        contentViewList.put(4,contentItemLayout.get(3));
    }

    public SparseArray<View> getContentViewList(){
        return contentViewList;
    }

    @Override
    public void expand() {
        if (expandAnimator == null){
            expandAnimator = ValueAnimator.ofInt(0,contentHeight);
            expandAnimator.setTarget(contentLayout);
            expandAnimator.setDuration(expandDuration);
            expandAnimator.setInterpolator(new DecelerateInterpolator());
            expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                    layoutParams.height = animatedValue;
                    contentLayout.setLayoutParams(layoutParams);
                    float alpha = (float) ((animatedValue * 1.0) / contentHeight);
                    contentLayout.setAlpha(alpha);
                }
            });
        }
        expandAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (dismissFinishListener!=null){
                    dismissFinishListener.startExpand();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (dismissFinishListener!=null){
                    dismissFinishListener.endExpand();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        expandAnimator.start();
        isShowing=true;
    }

    @Override
    public void reverse() {
        if (isAnimating){
            return;
        }
        if (reverseAnimator == null){
            reverseAnimator = ValueAnimator.ofInt(contentHeight,0);
            reverseAnimator.setTarget(contentLayout);
            reverseAnimator.setDuration(reverseDuration);
            reverseAnimator.setInterpolator(new AccelerateInterpolator());
            reverseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                    layoutParams.height = animatedValue;
                    contentLayout.setLayoutParams(layoutParams);
                    float alpha = (float) ((animatedValue * 1.0) / contentHeight);
                    contentLayout.setAlpha(alpha);
                }
            });
        }
        reverseAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
                if (dismissFinishListener!=null){
                    dismissFinishListener.startReverse();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                if (dismissFinishListener!=null){
                    dismissFinishListener.endReverse();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reverseAnimator.start();
        isShowing =false;
    }

    public interface OnInnerAnimateListener {
        void startExpand();
        void endExpand();
        void startReverse();
        void endReverse();
    }
    private OnInnerAnimateListener dismissFinishListener;

    public void setDismissFinishListener(OnInnerAnimateListener dismissFinishListener) {
        this.dismissFinishListener = dismissFinishListener;
    }
}
