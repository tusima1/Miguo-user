package com.fanwe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.miguo.live.views.base.BaseRelativeLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Barry/狗蛋哥 on 2016/10/10.
 * 首页六个推荐列表
 */
public class HomeTuanTimeLimitView extends BaseRelativeLayout implements HomeTuanHorizontalScrollView.OnTimeLimitClickListener, HomeTuanHorizontalScrollView.HomeTuanHorizontalScrollViewOnTouchListener{

    HomeTuanHorizontalScrollView homeTuanHorizontalScrollView;
    LinearLayout top;
    public static final int TOP_ID = 0x4514651;

    TextView countDownTime;
    SpecialListModel.Result result;

    OnTimeLimitClickListener onTimeLimitClickListener;

    TimeLimitedOnTouchListener timeLimitedOnTouchListener;

    PtrFrameLayout parent;
    CountDown timer;

    TextView rightText;


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
//                Log.d(tag, "action down ");
                if(parent != null){
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d(tag, "action move ");
                if(parent != null){
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                Log.d(tag, "action cancel ");
                onActionCancel(ev);
                if(parent != null){
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return true;
    }

    @Override
    public void onActionDown(MotionEvent ev) {
        if(getTimeLimitedOnTouchListener() != null){
            getTimeLimitedOnTouchListener().onActionDown(ev);
        }
    }

    @Override
    public void onActionMove(MotionEvent ev) {
        if(getTimeLimitedOnTouchListener() != null){
            getTimeLimitedOnTouchListener().onActionMove(ev);
        }
    }

    @Override
    public void onActionCancel(MotionEvent ev) {
        if(getTimeLimitedOnTouchListener() != null){
            getTimeLimitedOnTouchListener().onActionCancel(ev);
        }
    }

    public interface TimeLimitedOnTouchListener{
        void onActionDown(MotionEvent ev);
        void onActionMove(MotionEvent ev);
        void onActionCancel(MotionEvent ev);
    }

    protected void init(){
        homeTuanHorizontalScrollView = new HomeTuanHorizontalScrollView(getContext());
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(matchParent(), wrapContent());
        params.setMargins(0, dip2px(10), 0, 0);
        params.addRule(RelativeLayout.BELOW, TOP_ID);
        homeTuanHorizontalScrollView.setLayoutParams(params);
        homeTuanHorizontalScrollView.setHomeTuanTimeLimitView(this);
        homeTuanHorizontalScrollView.setHomeTuanHorizontalScrollViewOnTouchListener(this);
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
        title.setTextColor(getColor(R.color.text_base_color));
        top.addView(title);


        countDownTime = new TextView(getContext());
        LinearLayout.LayoutParams countDownTimeParams = getLinearLayoutParams(wrapContent(), wrapContent());
        countDownTimeParams.setMargins(dip2px(3), 0, 0, 0);
        countDownTime.setLayoutParams(countDownTimeParams);
//        countDownTime.setBackgroundResource(R.drawable.shape_cricle_gray_solid_cccccc);
//        countDownTime.setPadding(dip2px(5), 2, dip2px(5), 2);
        countDownTime.setTextColor(getColor(R.color.orange_f5));
        countDownTime.setTextSize(12);
        countDownTime.setText("12:12:05");
        top.addView(countDownTime);
        countDownTime.setVisibility(result.getCount_down().equals("0") ? View.GONE : View.VISIBLE);


        rightText = new TextView(getContext());
        LinearLayout.LayoutParams rightTextParams = getLinearLayoutParams(wrapContent(), wrapContent());
        rightTextParams.setMargins(dip2px(3), 0, 0, 0);
        rightText.setLayoutParams(rightTextParams);
        rightText.setText(getRightString());
        rightText.setTextColor(getColor(R.color.gray_cc));
        rightText.setTextSize(12);
        top.addView(rightText);
    }

    private String getRightString(){
        return DataFormat.toLong(result.getCount_down()) < 0  ? "后开始" : (result.getCount_down().equals("0") ? "已结束" : "后结束");
    }

    private void initSaleDatas(){
        addView(homeTuanHorizontalScrollView);
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
        initTimer();
        initSaleDatas();
    }

    private void initTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        long time = Math.abs(DataFormat.toLong(result.getCount_down()));
        timer = new CountDown(time, 1000);
        timer.start();
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

    class CountDown extends android.os.CountDownTimer{

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            countDownTime.setText(getTimeText(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            countDownTime.setVisibility(View.GONE);
            rightText.setText("已结束");
            cancel();
        }
    }

    public TimeLimitedOnTouchListener getTimeLimitedOnTouchListener() {
        return timeLimitedOnTouchListener;
    }

    public void setTimeLimitedOnTouchListener(TimeLimitedOnTouchListener timeLimitedOnTouchListener) {
        this.timeLimitedOnTouchListener = timeLimitedOnTouchListener;
    }

    private String getTimeText(long millisUntilFinished){
        int day = (int)(millisUntilFinished / 1000 / 3600 / 24);
        int hour = (int)(millisUntilFinished / 1000 / 3600 % 24);
//        int hour = (int)(millisUntilFinished / 1000 / 3600);
        int lastMin = (int)(millisUntilFinished / 1000 % 3600);
        int min = lastMin / 60;
        int sec = lastMin % 60;
        return ((day < 1) ? "" : day + "天") + (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
    }

}
