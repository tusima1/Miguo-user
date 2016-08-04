//package com.tencent.qcloud.suixinbo.views.customviews;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Rect;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.fanwe.o2o.miguo.R;
//import com.miguo.utils.DisplayUtil;
//import com.tencent.TIMMessage;
//import com.tencent.TIMTextElem;
//import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.regex.Pattern;
//
//
///**
// * 文本输入框
// */
//public class InputTextMsgDialog extends Dialog {
//    private TextView confirmBtn;
//    private EditText messageTextView;
//    private static final String TAG = InputTextMsgDialog.class.getSimpleName();
//    private Context mContext;
//    private LiveHelper mLiveControlHelper;
//    private Activity mVideoPlayActivity;
//    private InputMethodManager imm;
//    private RelativeLayout rlDlg;
//    private int mLastDiff = 0;
//    private final String reg = "[`~@#$%^&*()-_+=|{}':;,/.<>￥…（）—【】‘；：”“’。，、]";
//    private Pattern pattern = Pattern.compile(reg);
//
//    public InputTextMsgDialog(Context context, int theme, LiveHelper presenter, Activity activity) {
//        super(context, theme);
//        mContext = context;
//        mLiveControlHelper = presenter;
//        mVideoPlayActivity = activity;
//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.input_text_dialog, null);
//        setContentView(inflate,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//        messageTextView = (EditText) findViewById(R.id.input_message);
//        confirmBtn = (TextView) findViewById(R.id.confrim_btn);
////        rlDlg = (RelativeLayout) findViewById(R.id.rl_dlg);
//        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        confirmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (messageTextView.getText().length() > 0) {
//                    sendText("" + messageTextView.getText());
//                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                    dismiss();
//                } else {
//                    Toast.makeText(mContext, "input can not be empty!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        messageTextView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() != KeyEvent.ACTION_UP) {   // 忽略其它事件
//                    return false;
//                }
//
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_ENTER:
//                        if (messageTextView.getText().length() > 0) {
//                            sendText("" + messageTextView.getText());
//                            imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
//                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                            dismiss();
//                        } else {
//                            Toast.makeText(mContext, "input can not be empty!", Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//
//        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
//        rldlgview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (v instanceof RelativeLayout){
//                    //触摸空白位置消失对话框
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                    dismiss();
//                }
//                return false;
//            }
//        });
//        //输入法到底部的间距(按需求设置)
//        final int paddingBottom = DisplayUtil.dp2px(mContext, 5);
//        rldlgview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
////                Rect r = new Rect();
////                //获取当前界面可视部分
////                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
////                //获取屏幕的高度
////                int screenHeight =  getWindow().getDecorView().getRootView().getHeight();
////                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
////                int heightDifference = screenHeight - r.bottom;
////
////                if (heightDifference <= 0 && mLastDiff > 0){
////                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
////                    dismiss();
////                }
////                mLastDiff = heightDifference;
//
//                Rect r = new Rect();
//                rldlgview.getWindowVisibleDisplayFrame(r);
//                //r.top 是状态栏高度
//                int screenHeight = rldlgview.getRootView().getHeight();
//                int softHeight = screenHeight - r.bottom ;
//                if (softHeight>100){//当输入法高度大于100判定为输入法打开了
//                    rldlgview.scrollTo(0, softHeight+paddingBottom);
//                    Log.e("test","开启");
//                }else {//否则判断为输入法隐藏了
//                    Log.e("test","关闭");
//                    rldlgview.scrollTo(0, paddingBottom);
//                }
//            }
//        });
//        rldlgview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                dismiss();
//            }
//        });
//    }
//
//    /**
//     * add message text
//     */
//    public void setMessageText(String strInfo) {
//        messageTextView.setText(strInfo);
//        messageTextView.setSelection(strInfo.length());
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
////        mVideoPlayActivity.refreshViewAfterDialog();
//    }
//
//    @Override
//    public void cancel() {
//        super.cancel();
//    }
//
//
//    private void sendText(String msg) {
//        if (msg.length() == 0)
//            return;
//        try {
//            byte[] byte_num = msg.getBytes("utf8");
//            if (byte_num.length > 160) {
//                Toast.makeText(mContext, "input message too long", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return;
//        }
//        TIMMessage Nmsg = new TIMMessage();
//        TIMTextElem elem = new TIMTextElem();
//        elem.setText(msg);
//        if (Nmsg.addElement(elem) != 0) {
//            return;
//        }
//        mLiveControlHelper.sendGroupText(Nmsg);
//    }
//
//    @Override
//    public void show() {
//        super.show();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            public void run() {
//                InputMethodManager inputManager = (InputMethodManager) messageTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(messageTextView, 0);
//            }
//
//        }, 500);
//    }
//}
