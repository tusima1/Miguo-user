package com.miguo.ui.view.notify;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/14.
 * 应用内弹通知
 */
public class NotifyMessage {

    Context mContext;

    WindowManager mWindowManager;

    WindowManager.LayoutParams mParams;

    NotifyContentView mNotifyContentView;

    public NotifyMessage(Context context) {
        this.mContext = context;
        init();
    }

    protected void init(){
        initWindowManager();
        initViews();
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
    protected void initViews(){
        mNotifyContentView = new NotifyContentView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mNotifyContentView.setLayoutParams(params);
        mWindowManager.addView(mNotifyContentView, mParams);
    }

}
