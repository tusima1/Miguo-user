package com.miguo.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.UserRedPacketInfo;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;
import java.util.Random;

/**
 * Created by didik on 2016/8/2.
 */
public class UserRobRedPacketDialogHelper implements IHelper, View.OnClickListener, CallbackView {

    private Activity mActivity;
    private Dialog dialog;
    private TextView mTvCountDown;//5秒倒计时
    private TextView mTvTimeLift;//剩余时间(30s)
    private ImageView mIvClose;
    private LiveHttpHelper mLiveHttpHelper;
    /**
     * 红包批次号。
     */
    private String red_packet_key;
    /**
     * 持续时间。
     */
    private int duration;

    /**
     * 是否能点击。
     */
    private boolean ifClickable = true;
    private String resultMessage = "";

    private CallbackView mcallbackView;

    private boolean isRobFinished = false;//抢的5秒是否已经结束

    private List<UserRedPacketInfo> robResult;


    /**
     * 抢红包的dialog
     */
    private CountDownTimer robLiftTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

            Message msg = Message.obtain();
            msg.what = 2;
            float v = millisUntilFinished * 1.0f / 1000f;
            int round = Math.round(v);
            msg.arg1 = round;
            Log.e("live", millisUntilFinished + "--" + round + "==" + v);
            mHandler.sendMessage(msg);
            if (round == 2) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        msg.what = 2;
                        msg.arg1 = 1;
                        mHandler.sendMessage(msg);
                    }
                }, 1000);
            }
        }

        @Override
        public void onFinish() {
            mTvCountDown.setText("");
            mTvCountDown.setBackgroundResource(R.drawable.selector_bg_rob_redpacket);
            mTvCountDown.setClickable(true);

            if (resultMessage != null) {
                MGToast.showToast(resultMessage, Toast.LENGTH_LONG);
            }
        }
    };

    /**
     * 总共时间(改为需要的时间)
     */
    private CountDownTimer totalTimer;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mTvCountDown.setText(new Random().nextInt(9) + "");
                    break;
                case 1:
                    break;
                case 2:
                    int arg1 = msg.arg1;
                    mTvCountDown.setText(arg1 + "");
                    break;
                case 3:
                    int arg2 = msg.arg1;
                    mTvTimeLift.setText(arg2 + "s 后结束");
                    break;

            }
        }
    };

    public UserRobRedPacketDialogHelper(Activity activity, String red_packet_key, int duration, CallbackView mcallbackView) {
        this.red_packet_key = red_packet_key;
        this.duration = duration;
        this.mActivity = activity;
        this.mcallbackView = mcallbackView;
        initCount(duration);
    }

    public void createDialog() {
        dialog = new Dialog(mActivity, R.style.floag_dialog);
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout
                .dialog_live_rob_red_packet, null);
        mTvCountDown = ((TextView) contentView.findViewById(R.id.tv_count_down));
        mTvTimeLift = ((TextView) contentView.findViewById(R.id.tv_time_lift));
        mIvClose = ((ImageView) contentView.findViewById(R.id.iv_cancle));

        mTvCountDown.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvCountDown.setClickable(false);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mLiveHttpHelper = new LiveHttpHelper(mActivity, this);
    }

    public void initCount(int duration) {
        duration = duration * 1000;
        if (duration < 10000) {
            duration = 10000;
        }
        totalTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Message msg = Message.obtain();
                msg.what = 3;
                float v = millisUntilFinished * 1.0f / 1000f;
                int round = Math.round(v);
                msg.arg1 = round;
                Log.e("live", millisUntilFinished + "--" + round + "==" + v);
                mHandler.sendMessage(msg);
                if (round == 2) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            msg.what = 3;
                            msg.arg1 = 1;
                            mHandler.sendMessage(msg);
                        }
                    }, 1000);
                }

            }

            @Override
            public void onFinish() {
                ifClickable = true;
                dismiss();

                //抢到后30s展示
                isRobFinished = true;
                if (robResult != null) {
                    //为null表示没有推送过
                    mcallbackView.onSuccess(LiveConstants.GET_PACKET_RESULT, robResult);
                }
            }
        };
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            endTimeTask();
            ifClickable = true;
            dialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        ifClickable = true;
        dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvCountDown) {
            clickTimeCountDown();
        } else if (v == mIvClose) {
            clickClose();
        }
    }

    /**
     * 开启计时器。
     */
    public void startTimeTask() {
        isRobFinished = false;
        robLiftTimer.start();
        totalTimer.start();
    }

    /**
     * 结束计时器。
     */
    public void endTimeTask() {
        robLiftTimer.cancel();
        totalTimer.cancel();
    }

    /**
     * 关闭
     */
    private void clickClose() {
        ifClickable = true;
        dismiss();
    }

    /**
     * 点击倒计时
     */
    private void clickTimeCountDown() {
        if (mLiveHttpHelper != null && ifClickable) {
            mLiveHttpHelper.getRedPackets(App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id(), red_packet_key);
            ifClickable = false;
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case LiveConstants.GET_RED_PACKETS:
                if (isRobFinished) {
                    mcallbackView.onSuccess(LiveConstants.GET_PACKET_RESULT, datas);
                } else {
                    robResult = datas;
                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onFailue(String responseBody) {
        resultMessage = "没有抢到，下回再努力!";
    }

    public String getRed_packet_key() {
        return red_packet_key;
    }

    public void setRed_packet_key(String red_packet_key) {
        this.red_packet_key = red_packet_key;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
