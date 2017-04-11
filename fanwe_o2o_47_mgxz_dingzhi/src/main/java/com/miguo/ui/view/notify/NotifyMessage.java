package com.miguo.ui.view.notify;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.fanwe.InitAdvsMultiActivity;
import com.fanwe.WithdrawLogActivity;
import com.fanwe.constant.ServerUrl;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.user.view.InviteActivity;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.MyOrderListActivity;
import com.fanwe.user.view.RedPacketListActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.MessageType;
import com.miguo.definition.RequestCode;
import com.miguo.entity.JpushMessageBean;
import com.miguo.factory.ClassNameFactory;
import com.miguo.factory.MessageTypeFactory;
import com.miguo.utils.AppStateUtil;
import com.miguo.utils.BaseUtils;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/14.
 * 应用内弹通知
 */
public class NotifyMessage {

    Context mContext;

    WindowManager mWindowManager;

    WindowManager.LayoutParams mParams;

    public NotifyMessage(Context context) {
        this.mContext = context;
        init();
    }

    protected void init(){
        initWindowManager();
    }

    /**
     * 初始化windowmanager
     */
    protected void initWindowManager(){
        mWindowManager = (WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mParams.format = PixelFormat.RGBA_8888;
    }

    /**
     * 添加view到windowmanager
     */
    public void show(JpushMessageBean bean){
        final NotifyContentView notifyContentView = new NotifyContentView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        notifyContentView.setLayoutParams(params);
        mWindowManager.addView(notifyContentView, mParams);
        notifyContentView.show(bean);
        notifyContentView.setOnNotifyViewStateChangeListener(new NotifyContentView.OnNotifyViewStateChangeListener() {
            @Override
            public void onCancel() {
                mWindowManager.removeView(notifyContentView);
                mWindowManager = null;
            }

            @Override
            public void action(JpushMessageBean bean) {
                notifyAction(bean);
            }
        });
    }

    private void notifyAction(JpushMessageBean bean){
        if(!AppStateUtil.isAppRunning(mContext)){
            Intent intent = new Intent(mContext, InitAdvsMultiActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("type", bean.getPush_jump_target());
            intent.putExtra("value", bean.getPush_jump_paramater());
            intent.putExtra("system_message_id", bean.getSystem_message_id());
            BaseUtils.jumpToNewActivity(mContext, intent);
            return;
        }
        MessageTypeFactory.jump(mContext, bean);
    }


}
