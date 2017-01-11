package com.miguo.ui.view.dropdown;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.dropdown.interf.ExpandReverse;
import com.miguo.ui.view.dropdown.interf.PopupWindowLike;
import com.miguo.ui.view.dropdown.view.TitleTab;

import java.util.ArrayList;

/**
 * Created by didik 
 * Created time 2017/1/5
 * Description: 
 */

public class DropDownMenu extends LinearLayout implements View.OnClickListener,PopupWindowLike,ExpandReverse {

    private final int titleTabNum=4;//默认的item数量
    private String[] titleStr=new String[]{
            "附近",
            "全部",
            "智能排序",
            "筛选"
    };
    private int contentHeight;//内容的高度(规定的)

    private LinearLayout titleTabLayout;//titleTab 的容器
    private FrameLayout contentLayout;//主内容 的容器
    private View fakeView;//底部的伪装view
    private boolean isShowing;//是否在展示中
    private int preClickId=-10;//之前点击的id

    private ValueAnimator expandAnimator;
    private ValueAnimator reverseAnimator;

    private int expandDuration = 250;
    private int reverseDuration = 300;

    private TitleTab preClickTab;//之前点击的tab

    private final SparseArray<View> contentViewList=new SparseArray<>();
    private int fakeHeight;//底下view的高度

    private boolean initOk=true;

    public DropDownMenu(Context context) {
        this(context,null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setBaseParams();
        addTitleTab(true);
        addDivider();
        finalAddView();
    }

    /**
     * 添加外层layout 之间的分隔线
     */
    private void addDivider() {
        View dividerView =new View(getContext());
        dividerView.setBackgroundColor(Color.GRAY);
        addView(dividerView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
    }

    private void addFakeView(){
        fakeView =new View(getContext());
        fakeView.setBackgroundColor(Color.parseColor("#66000000"));
        fakeView.setOnClickListener(this);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        final int heightPixels = displayMetrics.heightPixels;

        titleTabLayout.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                titleTabLayout.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];

                int titleHeight = titleTabLayout.getHeight();

                fakeHeight = heightPixels - (y + titleHeight + contentHeight);
                addView(fakeView, LayoutParams.MATCH_PARENT, fakeHeight);
            }
        });
    }

    /**
     * 最终添加的view
     */
    private void finalAddView(){
        addDivider();
        addView(titleTabLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addDivider();
        addView(contentLayout, LayoutParams.MATCH_PARENT,0);

        contentLayout.setVisibility(GONE);
        addFakeView();
        fakeView.setVisibility(GONE);
    }

    /**
     * 设置基本的参数
     */
    protected void setBaseParams(){
        contentHeight = dp2px(getContext(),355);

        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.VERTICAL);

        titleTabLayout =new LinearLayout(getContext());
        titleTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        titleTabLayout.setGravity(Gravity.CENTER_VERTICAL);
        titleTabLayout.setMinimumHeight(dp2px(getContext(),36));

        contentLayout = new FrameLayout(getContext());
    }

    /**
     * 添加 TitleTab ,根据数量 和 分隔线
     * @param show 是否显示分隔线
     */
    protected void addTitleTab(boolean show){
        LinearLayout.LayoutParams layoutParams;
        int weight=0;
        int id= R.id.title_tab_1;
        for (int i = 0; i < titleTabNum; i++) {
            switch (i){
                case 0:
                    weight=183;
                    id=R.id.title_tab_1;
                    break;
                case 1:
                    weight=148;
                    id=R.id.title_tab_2;
                    break;
                case 2:
                    weight=200;
                    id=R.id.title_tab_3;
                    break;
                case  3:
                    weight=183;
                    id=R.id.title_tab_4;
                    break;
                default:
                    weight=183;
                    break;
            }
            layoutParams =new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,weight);
//            titleTabLayout.setGravity(Gravity.CENTER);
            layoutParams.gravity = Gravity.CENTER;

            TitleTab titleTab=new TitleTab(getContext());
            titleTab.setText(titleStr[i]);
            titleTab.setId(id);
            titleTab.setBackgroundDrawable(getResources().getDrawable(R.drawable.md_ripple_white));
            titleTab.setOnClickListener(this);
            titleTabLayout.addView(titleTab,layoutParams);
            if (show && (i!=titleTabNum-1)){
                titleTabLayout.addView(getDividerView(),getDividerParams());
            }

        }
    }

    /**
     * 是否初始化失败,如果数据任何一点缺失即为失败,不响应任何点击事件,亦不产生任何的回调
     * @param initOk 默认ok
     */
    public void setInitOk(boolean initOk) {
        this.initOk = initOk;
    }

    /**
     * 默认显示的标题描述文字
     * @param titleStr
     */
    public void setTitleStr(String[] titleStr) {
        this.titleStr = titleStr;
        updateTitleText();
    }

    private void updateTitleText() {
        if (titleStr == null || titleStr.length <=0){
            return;
        }
        for (int i = 0; i < titleStr.length; i++) {
            setTitleTabText(i,titleStr[i]);
        }
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

    @Override
    public void onClick(View v) {
        if (!initOk){
            return;//初始化失败
        }
        int id =v.getId();
        if (preClickTab!=null && preClickTab!=v){
            preClickTab.start();
        }
        if (v instanceof TitleTab){
            ((TitleTab) v).start();
            preClickTab = (TitleTab) v;
        }
        if (id == preClickId && isShowing){
            dismiss();
            return;
        }
        if (id == R.id.title_tab_1){
            clickOrder(1,id);
            return;
        }
        if (id == R.id.title_tab_2){
            clickOrder(2,id);
            return;
        }
        if (id == R.id.title_tab_3){
            clickOrder(3,id);
            return;
        }
        if (id == R.id.title_tab_4){
            clickOrder(4,id);
            return;
        }

        if (v == fakeView){
            dismiss();
        }
    }

    /**
     * 点击
     * @param order
     * @param id
     */
    private void clickOrder(int order,int id){
        contentLayout.removeAllViews();
        contentLayout.setVisibility(VISIBLE);

        contentLayout.addView(contentViewList.get(order),new FrameLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (!isShowing){
            expand();
        }
        preClickId = id;
    }

    public int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 设置标题的文字
     * @param index from 0 ~~
     * @param text nullable
     */
    public void setTitleTabText(int index,String text){
        if (index > titleTabNum-1){
            return;
        }
        text = TextUtils.isEmpty(text) ? titleStr[index] : text;
        try {
            ((TitleTab)titleTabLayout.getChildAt(index)).setText(text);
        } catch (Exception e) {
            Log.e("test","index error!");
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void dismiss() {
        preClickTab.reverse();
        reverse();
        preClickId=-10;
        isShowing =false;
        preClickTab=null;
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
            expandAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                        fakeView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

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
            reverseAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                        fakeView.setVisibility(GONE);
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
        }
        reverseAnimator.start();
    }
}
