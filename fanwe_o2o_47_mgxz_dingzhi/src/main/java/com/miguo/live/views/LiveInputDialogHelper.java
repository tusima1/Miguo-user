package com.miguo.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.miguo.utils.DisplayUtil;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * Created by didik on 2016/8/4.
 * 直播的对话框
 */
public class LiveInputDialogHelper {
    private TextView confirmBtn;
    private EditText messageEditView;
    private Context mContext;
    private LiveHelper mLiveControlHelper;
    private InputMethodManager imm;
    private final String reg = "[`~@#$%^&*()-_+=|{}':;,/.<>￥…（）—【】‘；：”“’。，、]";
    private Pattern pattern = Pattern.compile(reg);
    private Dialog dialog;
    private LinearLayout mInputLayout;
    private RelativeLayout rootLayout;

    public LiveInputDialogHelper(LiveHelper presenter, Activity activity) {
        this.mContext=activity;
        this.mLiveControlHelper=presenter;
        createDialog();
    }

    private void createDialog() {
        dialog = new Dialog(mContext, R.style.inputdialog);
        dialog.setContentView(R.layout.input_text_dialog);
        mInputLayout = (LinearLayout) dialog.findViewById(R.id.rl_inputdlg_view);
        rootLayout = ((RelativeLayout) dialog.findViewById(R.id.rootView));//跟布局

        messageEditView = (EditText) dialog.findViewById(R.id.input_message);
        confirmBtn = (TextView) dialog.findViewById(R.id.confrim_btn);


        imm = (InputMethodManager) mContext.getSystemService(Context
                .INPUT_METHOD_SERVICE);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageEditView.getText().length() > 0) {
                    sendText("" + messageEditView.getText());
                    imm.showSoftInput(messageEditView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageEditView.getWindowToken(), 0);
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext, "input can not be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        messageEditView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_UP) {   // 忽略其它事件
                    return false;
                }

                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageEditView.getText().length() > 0) {
                            sendText("" + messageEditView.getText());
                            imm.showSoftInput(messageEditView, InputMethodManager.SHOW_FORCED);
                            imm.hideSoftInputFromWindow(messageEditView.getWindowToken(), 0);
                            dismiss();
                        } else {
                            Toast.makeText(mContext, "input can not be empty!", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });


        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof RelativeLayout){
                    //触摸空白位置消失对话框
                    imm.hideSoftInputFromWindow(messageEditView.getWindowToken(), 0);
                    dismiss();
                }
                return false;
            }
        });
        //输入法到底部的间距(按需求设置)
        final int paddingBottom = DisplayUtil.dp2px(mContext, 5);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight =  getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//
//                if (heightDifference <= 0 && mLastDiff > 0){
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                    dismiss();
//                }
//                mLastDiff = heightDifference;

                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                //r.top 是状态栏高度
                int screenHeight = rootLayout.getRootView().getHeight();
                int softHeight = screenHeight - r.bottom ;
                if (softHeight>100){//当输入法高度大于100判定为输入法打开了
                    rootLayout.scrollTo(0, softHeight+paddingBottom);
                    Log.e("test","开启");
                }else {//否则判断为输入法隐藏了
                    Log.e("test","关闭");
                    rootLayout.scrollTo(0, paddingBottom);
                }
            }
        });

    }

    /**
     * 发送消息
     * @param msg
     */
    private void sendText(String msg) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(mContext, "input message too long", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        TIMMessage Nmsg = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(msg);
        if (Nmsg.addElement(elem) != 0) {
            return;
        }
        mLiveControlHelper.sendGroupText(Nmsg);
    }

    /*展示 代替dialog的show*/
    public void show(){
        if (dialog!=null){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) messageEditView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(messageEditView, 0);
                }

            }, 500);
            dialog.show();
        }
    }

    /**
     * add message text
     */
    public void setMessageText(String strInfo) {
        messageEditView.setText(strInfo);
        messageEditView.setSelection(strInfo.length());
    }

    public void dismiss(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }

}
