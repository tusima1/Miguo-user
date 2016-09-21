package com.miguo.live.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.tencent.rtmp.TXLivePlayer;

/**
 *
 * 点播进度条控制 。
 * Created by Administrator on 2016/9/20.
 */
public class PlayBackSeekBarView extends LinearLayout implements IViewGroup, View.OnClickListener {
    private Context mContext;
    /**
     * 播放与暂停按钮。
     */
    private ImageView      mBtnPlay;

    /**
     * 开始时间 。
     */
    private TextView      mTextStart;
    /**
     * 播放时长。
     */
    private TextView        mTextDuration;
    private SeekBar          mSeekBar;
    private boolean          mStartSeek = false;

    private SeekBar.OnSeekBarChangeListener  mOnSeekBarChangeListener;

    private OnClickListener  btnStartListener;

    public PlayBackSeekBarView(Context context) {
        this(context, null);
    }

    public PlayBackSeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public PlayBackSeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.play_back_foot, this);
        mBtnPlay =(ImageView) this.findViewById(R.id.btnPlay);

        mTextStart = (TextView) this.findViewById(R.id.play_start);
        mTextDuration = (TextView) this.findViewById(R.id.duration);


        mBtnPlay.setOnClickListener(btnStartListener);

        mSeekBar = (SeekBar) this.findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        mTextDuration.setTextColor(Color.rgb(255, 255, 255));
        mTextStart.setTextColor(Color.rgb(255, 255, 255));
//        //缓存策略
//        mBtnCacheStrategy = (Button)view.findViewById(R.id.btnCacheStrategy);
//        mLayoutCacheStrategy = (LinearLayout)view.findViewById(R.id.layoutCacheStrategy);
//        mBtnCacheStrategy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//        mLayoutCacheStrategy.setVisibility(mLayoutCacheStrategy.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//            }
//        });

//        this.setCacheStrategy(CACHE_STRATEGY_AUTO);


    }

    public SeekBar.OnSeekBarChangeListener getmOnSeekBarChangeListener() {
        return mOnSeekBarChangeListener;
    }

    public void setmOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = mOnSeekBarChangeListener;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onClick(View v) {

    }
}
