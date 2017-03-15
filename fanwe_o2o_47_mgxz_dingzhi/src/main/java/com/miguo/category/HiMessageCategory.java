package com.miguo.category;

import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.listener.HiMessageListener;
import com.miguo.utils.NotificationsUtils;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/6.
 */

public class HiMessageCategory extends Category {

    @ViewInject(R.id.system_message_layout)
    RelativeLayout systemMessageLayout;

    @ViewInject(R.id.amount_message_layout)
    RelativeLayout amountMessageLayout;

    @ViewInject(R.id.open)
    TextView openPushMessage;

    @ViewInject(R.id.close)
    ImageView close;

    @ViewInject(R.id.prompt_column_layout)
    RelativeLayout promptColumnLayout;

    public HiMessageCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {

    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiMessageListener(this);
    }

    @Override
    protected void setThisListener() {
        close.setOnClickListener(listener);
        openPushMessage.setOnClickListener(listener);
        systemMessageLayout.setOnClickListener(listener);
        amountMessageLayout.setOnClickListener(listener);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        checkPermission();
    }

    private void checkPermission(){
        if(NotificationsUtils.isNotificationEnabled(getActivity())){
            promptColumnLayout.setVisibility(View.GONE);
        }
    }

    public void clickClose(){
        hidePromptColumnLayout();
        moveBottomLayout();
    }

    private void hidePromptColumnLayout(){
        promptColumnLayout.setVisibility(View.GONE);
        TranslateAnimation animation = new TranslateAnimation(0, -getScreenWidth(), 0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        promptColumnLayout.startAnimation(animation);
    }

    private void moveBottomLayout(){
        TranslateAnimation animation = new TranslateAnimation(0, 0, dip2px(50), 0);
        animation.setDuration(300);
        animation.setStartOffset(302);
        systemMessageLayout.startAnimation(animation);
        amountMessageLayout.startAnimation(animation);
    }

    @Override
    public HiMessageListener getListener() {
        return (HiMessageListener)super.getListener();
    }

}
