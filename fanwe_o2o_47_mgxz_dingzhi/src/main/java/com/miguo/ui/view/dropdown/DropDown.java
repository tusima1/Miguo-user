package com.miguo.ui.view.dropdown;

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
        setBaseParams();
        addDivider();
        finalAddView();
    }

    /**
     * 添加外层layout 之间的分隔线
     */
    private void addDivider() {
        View dividerView =new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        addView(dividerView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
    }


    /**
     * 最终添加的view
     */
    private void finalAddView(){
        //TODO 这是两条线条
//        addDivider();
//        addDivider();
        addView(contentLayout, LayoutParams.MATCH_PARENT,contentHeight);

        contentLayout.setVisibility(GONE);
    }

    /**
     * 设置基本的参数
     */
    protected void setBaseParams(){
        contentHeight = dp2px(getContext(),355);

        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.VERTICAL);

//        titleTabLayout =new LinearLayout(getContext());
//        titleTabLayout.setOrientation(LinearLayout.HORIZONTAL);
//        titleTabLayout.setGravity(Gravity.CENTER_VERTICAL);
//        titleTabLayout.setMinimumHeight(dp2px(getContext(),36));

        contentLayout = new FrameLayout(getContext());
    }

    /**
     * 是否初始化失败,如果数据任何一点缺失即为失败,不响应任何点击事件,亦不产生任何的回调
     * @param initOk 默认ok
     */
    public void setInitOk(boolean initOk) {
        this.initOk = initOk;
    }

    /**
     * 获取分隔线
     * @return 分隔线 divider
     */
    protected View getDividerView(){
        View divider=new View(getContext());
        divider.setBackgroundColor(Color.parseColor("#EEEEEE"));
        return divider;
    }

    protected ViewGroup.LayoutParams getDividerParams(){
        return new ViewGroup.LayoutParams(2,dp2px(getContext(),19));
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
        contentLayout.removeAllViews();
        contentLayout.setVisibility(VISIBLE);

        contentLayout.addView(contentViewList.get(order),new FrameLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        if (!isShowing){
//            expand();
//        }
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
//        reverse();
        isShowing =false;
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
                }
            });
        }
        expandAnimator.start();
        isShowing=true;
    }

    @Override
    public void reverse() {
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
                }
            });
        }
        reverseAnimator.start();
    }
}
