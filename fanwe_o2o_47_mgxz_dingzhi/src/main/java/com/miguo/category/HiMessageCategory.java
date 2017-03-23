package com.miguo.category;

import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.UnReadMessageCountDao;
import com.miguo.dao.impl.UnReadMessageCountDaoImpl;
import com.miguo.entity.UnReadMessageCountBean;
import com.miguo.listener.HiMessageListener;
import com.miguo.utils.NotificationsUtils;
import com.miguo.view.UnReadMessageCountView;

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

    UnReadMessageCountDao unReadMessageCountDao;

    @ViewInject(R.id.system_message_count)
    TextView systemMessageCount;

    @ViewInject(R.id.amount_message_count)
    TextView amountMessageCount;

    public HiMessageCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        initUnReadMessageCountDao();
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

    private void initUnReadMessageCountDao(){
        unReadMessageCountDao = new UnReadMessageCountDaoImpl(new UnReadMessageCountView() {
            @Override
            public void getUnReadMessageCountSuccess(UnReadMessageCountBean.Result.Body count) {
                updateSystemMessageCount(count.getSystem_message_count());
                updateCommissionMessageCount(count.getMoney_message_count());
            }

            @Override
            public void getUnReadMessageCountError(String message) {
                handleGetUnReadMessageCountError();
            }
        });
    }

    public void initMessageCount(){
        String token = isEmpty(App.getInstance().getToken()) ? "" : App.getInstance().getToken();
        unReadMessageCountDao.getUnReadMessageCount(token);
    }

    private void handleGetUnReadMessageCountError(){
        systemMessageCount.setVisibility(View.GONE);
        amountMessageCount.setVisibility(View.GONE);
    }

    private void updateSystemMessageCount(int count){
        systemMessageCount.setText(count >= 100 ? "99+" : count + "");
        systemMessageCount.setVisibility(count <= 0 ? View.GONE : View.VISIBLE);
    }

    private void updateCommissionMessageCount(int count){
        amountMessageCount.setText(count >= 100 ? "99+" : count + "");
        amountMessageCount.setVisibility(count <= 0 ? View.GONE : View.VISIBLE);
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
