package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getHostAuthTime.ModelHostAuthTime;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LiveStartAuthActivity extends Activity implements CallbackView {
    private ImageView ivGif;
    private LinearLayout layoutStart, layoutWait;
    private String pageType;
    private Chronometer chronometer;
    private LiveHttpHelper liveHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_auth_start);
        preParam();
        preView();
    }

    private void preParam() {
        if (getIntent() != null) {
            pageType = getIntent().getStringExtra("pageType");
        }
        if ("wait".equals(pageType)) {
            liveHttpHelper = new LiveHttpHelper(this, this);
            liveHttpHelper.getHostAuthTime();
        }
    }

    private void preView() {
        ivGif = (ImageView) findViewById(R.id.iv_gif_start_auth);
        Glide.with(this).load(R.drawable.gif_auth).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivGif);
        layoutStart = (LinearLayout) findViewById(R.id.layout_auth_start);
        layoutWait = (LinearLayout) findViewById(R.id.layout_auth_wait);
        if ("start".equals(pageType)) {
            layoutStart.setVisibility(View.VISIBLE);
            layoutWait.setVisibility(View.GONE);
        } else if ("wait".equals(pageType)) {
            layoutStart.setVisibility(View.GONE);
            layoutWait.setVisibility(View.VISIBLE);
        } else {
            layoutStart.setVisibility(View.GONE);
            layoutWait.setVisibility(View.GONE);
        }
        chronometer = (Chronometer) findViewById(R.id.tv_time_auth_wait);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_close_start_auth:
                finish();
                break;
            case R.id.btn_start_live_start:
                Intent intent = new Intent(this, LiveAuthActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    long tempTime;
    ModelHostAuthTime modelHostAuthTime;

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.HOST_AUTH_TIME.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                long currTime = System.currentTimeMillis();
                modelHostAuthTime = (ModelHostAuthTime) datas.get(0);
                tempTime = currTime - Long.valueOf(modelHostAuthTime.getInsert_time());
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if ("1".equals(modelHostAuthTime.getIs_host())) {
                        //已经是主播
                        startActivity(new Intent(LiveStartAuthActivity.this, LiveStartActivity.class));
                        finish();
                    } else {
                        // 设置开始讲时时间
                        chronometer.setBase(SystemClock.elapsedRealtime() - tempTime);
                        // 开始记时
                        chronometer.start();
                    }
                    break;
            }
        }
    };
}
