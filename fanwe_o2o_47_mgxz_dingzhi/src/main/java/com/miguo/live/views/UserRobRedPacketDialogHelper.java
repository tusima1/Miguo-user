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

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.IHelper;

import java.util.Random;

/**
 * Created by didik on 2016/8/2.
 */
public class UserRobRedPacketDialogHelper implements IHelper, View.OnClickListener {

    private Activity mActivity;
    private Dialog dialog;
    private TextView mTvCountDown;//5秒倒计时
    private TextView mTvTimeLift;//剩余时间(30s)
    private ImageView mIvClose;

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
        }
    };

    /**
     * 总共时间(改为需要的时间)
     */
    private CountDownTimer totalTimer = new CountDownTimer(10 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            dismiss();
        }
    };


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

                    break;

            }
        }
    };

    public UserRobRedPacketDialogHelper(Activity activity) {
        this.mActivity = activity;
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
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
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
    public void startTimeTask(){
        robLiftTimer.start();
        totalTimer.start();
    }
    /**
     * 关闭
     */
    private void clickClose() {
        dismiss();
    }

    /**
     * 点击倒计时
     */
    private void clickTimeCountDown() {

    }
}
