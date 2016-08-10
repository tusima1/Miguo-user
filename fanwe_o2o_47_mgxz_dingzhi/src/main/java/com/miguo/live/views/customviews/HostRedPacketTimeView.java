package com.miguo.live.views.customviews;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/8/10.
 * 主播界面的红包倒计时,小的那个,左上那个
 */
public class HostRedPacketTimeView extends RelativeLayout {
    private Context mContext;
    private TextView mTv_CountTime;

    public HostRedPacketTimeView(Context context) {
        super(context);
        init(context);
    }

    public HostRedPacketTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HostRedPacketTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.act_live_host_redpacket_countdowntimer,
                this);

        //init view
        mTv_CountTime = ((TextView) findViewById(R.id.tv_time));


    }

    /**
     * 设置时间
     * @param time
     */
    public void setTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            mTv_CountTime.setText(time);
        }
    }

}
