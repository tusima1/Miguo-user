package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveEndBinding;
import com.fanwe.umeng.UmengShareManager;
import com.miguo.live.model.DataBindingLiveEnd;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.stopLive.ModelStopLive;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.utils.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主播退出
 * Created by Administrator on 2016/7/28.
 */
public class LiveEndActivity extends Activity implements CallbackView {
    private DataBindingLiveEnd dataBindingLiveEnd;
    private boolean isShare;
    private ModelStopLive modelStopLive;
    private LiveHttpHelper mLiveHttphelper;
    private CircleImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        /*set it to be no title*/
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        /*set it to be full screen*/
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActLiveEndBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_end);
//
//        this.findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dataBindingLiveEnd = new DataBindingLiveEnd();
        preData();
        binding.setLive(dataBindingLiveEnd);
    }

    private void preData() {
        mLiveHttphelper = new LiveHttpHelper(this, this);
        mLiveHttphelper.stopLive(MySelfInfo.getInstance().getMyRoomNum() + "");

        dataBindingLiveEnd.hostName.set(App.getInstance().getUserNickName());
        dataBindingLiveEnd.shopName.set(CurLiveInfo.modelShop.getShop_name());

        ivIcon = (CircleImageView) findViewById(R.id.iv_portrait_live_end);
        ImageLoader.getInstance().displayImage(App.getApplication().getUserIcon(), ivIcon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShare) {
            isShare = false;
            finish();
        }
    }

    public void getValue() {
        Intent intent = getIntent();
        LiveInfoJson liveInfoJson = (LiveInfoJson) intent.getSerializableExtra(LiveConstants.LIVEINFOJSON);
        if (liveInfoJson != null) {
            if (!TextUtils.isEmpty(liveInfoJson.getUsetime())) {
                Integer seconds = Integer.valueOf(liveInfoJson.getUsetime());
                String timeStr = TimeUtils.secondToHHMMSS(seconds);
                dataBindingLiveEnd.timeLive.set(timeStr);
                String count = liveInfoJson.getWatch_count();
                if (!TextUtils.isEmpty(count)) {
                    dataBindingLiveEnd.numViewers.set(count);
                    dataBindingLiveEnd.countMoney.set("");
                    dataBindingLiveEnd.countGood.set("");
                    dataBindingLiveEnd.countMi.set("");
                }
            }

        }
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_shop_name:
                break;
            case R.id.iv_qq_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.QQ);
                break;
            case R.id.iv_weixin_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.WEIXIN);
                break;
            case R.id.iv_friend_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.FRIEND);
                break;
            case R.id.iv_sina_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.SINA);
                break;
            case R.id.iv_qqzone_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.QQZONE);
                break;
            case R.id.btn_submit_live_end:
                isShare = true;
                endLive();
                break;
        }
    }

    private void endLive() {
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        //已认证的，去直播
        if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.QQ) {
            platform = SHARE_MEDIA.QQ;
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.WEIXIN) {
            platform = SHARE_MEDIA.WEIXIN;
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.FRIEND) {
            platform = SHARE_MEDIA.WEIXIN_CIRCLE;
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.SINA) {
            platform = SHARE_MEDIA.SINA;
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.QQZONE) {
            platform = SHARE_MEDIA.QZONE;
        }
        UmengShareManager.share(platform, this, "分享", "直播结束分享", "http://www.mgxz.com/", UmengShareManager.getUMImage(this, "http://www.mgxz.com/pcApp/Common/images/logo2.png"), null);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.STOP_LIVE.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                modelStopLive = (ModelStopLive) datas.get(0);
                dataBindingLiveEnd.numViewers.set(modelStopLive.getWatch_count());
                dataBindingLiveEnd.timeLive.set(TimeUtils.millisecondToHHMMSS(Long.valueOf(modelStopLive.getUsetime())));
                dataBindingLiveEnd.countMoney.set(modelStopLive.getRed_packets_total());
                dataBindingLiveEnd.countGood.set(modelStopLive.getSell_total());
            }
        }
        MySelfInfo.getInstance().setMyRoomNum(-1);

    }

    @Override
    public void onFailue(String responseBody) {

    }
}
