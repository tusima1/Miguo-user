package com.fanwe.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.base.BaseRelativeLayout;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Barry/狗蛋哥 on 2016/10/10.
 * 首页六个推荐列表
 */
public class HomeTuanTimeLimitView extends BaseRelativeLayout implements HomeTuanHorizontalScrollView.OnTimeLimitClickListener{

    HomeTuanHorizontalScrollView homeTuanHorizontalScrollView;
    LinearLayout top;
    public static final int TOP_ID = 0x4514651;

    TextView countDownTime;
    SpecialListModel.Result result;

    OnTimeLimitClickListener onTimeLimitClickListener;

    PtrFrameLayout parent;

    public HomeTuanTimeLimitView(Context context) {
        super(context);
        if(!isInEditMode()){
            init();
        }
    }

    public HomeTuanTimeLimitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            init();
        }
    }

    public HomeTuanTimeLimitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()){
            init();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (parent != null) {
//            parent.requestDisallowInterceptTouchEvent(true);
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (parent != null) {
//            parent.requestDisallowInterceptTouchEvent(true);
//        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(parent != null){
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(parent != null){
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return true;
    }

    protected void init(){
        homeTuanHorizontalScrollView = new HomeTuanHorizontalScrollView(getContext());
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(matchParent(), wrapContent());
        params.setMargins(0, dip2px(10), 0, 0);
        params.addRule(RelativeLayout.BELOW, TOP_ID);
        homeTuanHorizontalScrollView.setLayoutParams(params);
        homeTuanHorizontalScrollView.setHomeTuanTimeLimitView(this);
    }

    /**
     * 限时特惠+倒计时
     */
    private void initTopContent(){
        /**
         * 顶部容器
         */
        top = new LinearLayout(getContext());
        RelativeLayout.LayoutParams topParams = getRelativeLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        topParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        top.setLayoutParams(topParams);
        top.setGravity(Gravity.CENTER);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setId(TOP_ID);
        addView(top);

        /**
         * 限时特惠标题
         */
        TextView title = new TextView(getContext());
        LinearLayout.LayoutParams titleParams = getLinearLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(titleParams);
        title.setText("限时特惠");
        title.setTextSize(16);
        title.setTextColor(getColor(R.color.orange_main));
        top.addView(title);


        countDownTime = new TextView(getContext());
        LinearLayout.LayoutParams countDownTimeParams = getLinearLayoutParams(wrapContent(), wrapContent());
        countDownTimeParams.setMargins(dip2px(5), 0, 0, 0);
        countDownTime.setLayoutParams(countDownTimeParams);
        countDownTime.setBackgroundResource(R.drawable.shape_cricle_gray_solid_cccccc);
        countDownTime.setPadding(dip2px(5), 2, dip2px(5), 2);
        countDownTime.setTextColor(Color.WHITE);
        countDownTime.setTextSize(12);
        countDownTime.setText("12:12:05");

        top.addView(countDownTime);

    }

    private void initSaleDatas(){
        addView(homeTuanHorizontalScrollView);
        ArrayList arrayList = new ArrayList();
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        homeTuanHorizontalScrollView.init(result.getBody());
        homeTuanHorizontalScrollView.setOnTimeLimitClickListener(this);
    }

    public void init(SpecialListModel.Result result){
        removeAllViews();
        if(result.getBody() == null || result.getBody().size()==0){
            return;
        }
        setResult(result);
        initTopContent();
        initSaleDatas();
    }

    public interface OnTimeLimitClickListener{
        void onTimeLimitClick();
    }

    @Override
    public void onTimeLimitClick() {
        if(onTimeLimitClickListener != null){
            onTimeLimitClickListener.onTimeLimitClick();
        }
    }

    public SpecialListModel.Result getResult() {
        return result;
    }

    public void setResult(SpecialListModel.Result result) {
        this.result = result;
    }

    public void setOnTimeLimitClickListener(OnTimeLimitClickListener onTimeLimitClickListener) {
        this.onTimeLimitClickListener = onTimeLimitClickListener;
    }

    public void setParent(PtrFrameLayout parent) {
        this.parent = parent;
    }
}
