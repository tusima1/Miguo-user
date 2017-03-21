package com.miguo.ui.view.notify;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.entity.JpushMessageBean;
import com.miguo.live.views.base.BaseRelativeLayout;
import com.miguo.utils.BaseUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/14.
 */
public class NotifyContentView extends BaseRelativeLayout {

    private static final int TITLE_ID = 0x000010;
    private static final int TIME_ID = 0x000020;
    private static final int CONTENT_ID = 0x000040;
    private static final int LINE_ID = 0x000080;

    RelativeLayout root;

    TextView mTitle;

    TextView mTime;

    TextView mContent;

    OnNotifyViewStateChangeListener onNotifyViewStateChangeListener;

    Handler handler;

    Timer timer;

    boolean cancelForAction;

    public NotifyContentView(Context context) {
        super(context);
    }

    public NotifyContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        initParams();
        initHandler();
        initRoot();
        initTitle();
        initTime();
        initLine();
        initContent();
        initShadow();
        initListener();
    }

    private void initParams(){
        cancelForAction = false;
    }

    private void initTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               handler.sendEmptyMessage(0);
            }
        }, 3000);
    }

    private void initHandler(){
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                hide();
                return true;
            }
        });
    }

    private void initRoot(){
        root = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(params);
        root.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_cricle_bg_white_10));
        addView(root);
    }

    private void initTitle(){
        mTitle = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(dip2px(17), dip2px(10), dip2px(5), 0);
        params.addRule(RelativeLayout.LEFT_OF, TIME_ID);
        mTitle.setLayoutParams(params);
        mTitle.setTextSize(14);
        mTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.c_9B9B9B));
        mTitle.setText("佣金通知");
        mTitle.setId(TITLE_ID);
        root.addView(mTitle);
    }

    private void initTime(){
        mTime = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dip2px(10), dip2px(17), 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mTime.setLayoutParams(params);
        mTime.setTextSize(14);
        mTime.setTextColor(ContextCompat.getColor(getContext(), R.color.c_595959));
        mTime.setText("12:00");
        mTime.setId(TIME_ID);
        root.addView(mTime);
    }

    private void initLine(){
        View line = new View(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, BaseUtils.dip2px(1));
        params.addRule(RelativeLayout.BELOW, TITLE_ID);
        params.setMargins(0, dip2px(10), 0, 0);
        line.setLayoutParams(params);
        line.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.c_0C000000));
        line.setId(LINE_ID);
        root.addView(line);
    }

    private void initContent(){
        mContent = new TextView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(dip2px(17), dip2px(13), dip2px(17), dip2px(21) - 50);
        params.addRule(RelativeLayout.BELOW, LINE_ID);
        mContent.setLayoutParams(params);
        mContent.setTextSize(14);
        mContent.setTextColor(ContextCompat.getColor(getContext(), R.color.c_4a4a4a));
        mContent.setText("你买到了喜欢的东西并做了分享，到账245元现金…");
        mContent.setId(CONTENT_ID);
        mContent.setMaxLines(2);
        mContent.setEllipsize(TextUtils.TruncateAt.END);
        root.addView(mContent);
    }

    private void initShadow(){
        ImageView shadow = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 50);
        params.addRule(RelativeLayout.BELOW, CONTENT_ID);
        shadow.setBackgroundResource(R.drawable.shadow_messages);
        shadow.setLayoutParams(params);
        root.addView(shadow);
    }

    private void initListener(){
        setOnClickListener(new NotifyMessageViewListener());
        root.setOnClickListener(new NotifyMessageViewListener());
    }

    class NotifyMessageViewListener implements View.OnClickListener{

        public NotifyMessageViewListener() {
        }

        @Override
        public void onClick(View v) {
            if(v instanceof NotifyContentView){
                cancel();
                return;
            }
            if(v instanceof RelativeLayout){
                if(onNotifyViewStateChangeListener != null){
                    cancelForAction = true;
                    cancel();
                }
            }
        }
    }

    public void update(JpushMessageBean bean){
        mTitle.setText(bean.getTitle());
        mContent.setText(bean.getMessage());
        mTime.setText(bean.getTime());
    }

    public void show(JpushMessageBean bean){
        update(bean);
        root.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0, 0 , -dip2px(85), 0);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                initTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        root.startAnimation(animation);
    }

    public void hide(){
        TranslateAnimation animation = new TranslateAnimation(0, 0 , 0, -dip2px(85));
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if(onNotifyViewStateChangeListener != null){
                    cancelTimer();
                    onNotifyViewStateChangeListener.onCancel();
                    setVisibility(View.GONE);
                    if(cancelForAction){
                        onNotifyViewStateChangeListener.action();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        root.startAnimation(animation);
    }

    private void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }

    private void cancel(){
        cancelTimer();
        hide();
    }

    public void setOnNotifyViewStateChangeListener(OnNotifyViewStateChangeListener onNotifyViewStateChangeListener) {
        this.onNotifyViewStateChangeListener = onNotifyViewStateChangeListener;
    }

    public interface OnNotifyViewStateChangeListener{
        void onCancel();
        void action();
    }

}
