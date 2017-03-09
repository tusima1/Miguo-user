package com.miguo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.didikee.uilibs.utils.StatusBarUtil;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.createShareRecord.ModelCreateShareRecord;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.IntentKey;
import com.miguo.entity.OnlinePayOrderPaymentBean;
import com.miguo.entity.StatusBean;
import com.miguo.ui.view.customviews.ArcDrawable;
import com.miguo.utils.BaseUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.TreeMap;

public class RedPacketOpenResultActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.tv_money)
    private TextView tvMoney;

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.iv_weixin_friend)
    private ImageView ivWeiXinFriend;

    @ViewInject(R.id.iv_weixin)
    private ImageView ivWeiXin;

    @ViewInject(R.id.iv_weibo)
    private ImageView ivWeiBo;

    @ViewInject(R.id.iv_qq)
    private ImageView ivQQ;

    @ViewInject(R.id.redPacket_view)
    private View redPacketView;


    private SHARE_MEDIA platform;
    private CommonHttpHelper commonHttpHelper;
    private String shareRecordId = "";//分享id
    private String money = "";
    private String order_id = "";
    private String face_icon = "";
    private String showContent = "";
    private OnlinePayOrderPaymentBean.Result.Body.Share share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_red_packet_open_result);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(this, Color.parseColor("#D23838"),0);
        setListener();
        initView();
        getRecordId();
        bindData();
    }

    private void bindData() {
        tvMoney.setText(money);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent==null){
            finish();
            return;
        }
        money = intent.getStringExtra(IntentKey.MONEY);
        order_id = intent.getStringExtra("order_id");
        face_icon = intent.getStringExtra(IntentKey.ICON);
        showContent = intent.getStringExtra(IntentKey.DESC);
        share = (OnlinePayOrderPaymentBean.Result.Body.Share) intent.getSerializableExtra("share");

        if (TextUtils.isEmpty(money) || TextUtils.isEmpty(order_id) ||share == null){
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void initView() {
        redPacketView.setBackground(new ArcDrawable());
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivWeiXinFriend.setOnClickListener(this);
        ivWeiXin.setOnClickListener(this);
        ivWeiBo.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack){
            onBackPressed();
            return;
        }
        if (v == ivWeiXinFriend){
            onWeiXinFriendClick();
            return;
        }
        if (v == ivWeiXin){
            onWeiXinClick();
            return;
        }
        if ( v == ivWeiBo){
            onWeiBoClick();
            return;
        }
        if (v== ivQQ){
            onQQClick();
            return;
        }
    }

    private void onQQClick() {
        platform = SHARE_MEDIA.QQ;
        doShare(platform);
    }

    private void onWeiBoClick() {
        platform = SHARE_MEDIA.SINA;
        doShare(platform);
    }

    private void onWeiXinClick() {
        platform = SHARE_MEDIA.WEIXIN;
        doShare(platform);
    }

    private void onWeiXinFriendClick() {
        platform = SHARE_MEDIA.WEIXIN_CIRCLE;
        doShare(platform);
    }

    private void doShare(SHARE_MEDIA platform){
        if(TextUtils.isEmpty(shareRecordId)){
            Toast.makeText(this, "分享失败!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (share!=null){
            onShareClick();

            String clickUrl = share.getClickurl();
//            if (TextUtils.isEmpty(clickUrl)) {
//                clickUrl = ServerUrl.getAppH5Url();
//            } else {
//                if (!clickUrl.contains("/share_record_id/")) {
//                    clickUrl = clickUrl + "/share_record_id/" + shareRecordId;
//                } else if (!TextUtils.isEmpty(shareRecordId) && !clickUrl.contains(shareRecordId)) {
//                    int i = clickUrl.indexOf("/share_record_id/");
//                    String temp = clickUrl.substring(0, i);
//                    clickUrl = temp + "/share_record_id/" + shareRecordId;
//                }
//            }
            share.setClickurl(getShareRecordIdUrl(clickUrl));

            UmengShareManager.share(platform, this,
                    getTitle(share.getTitle()),
                    getSummary(share.getSummary()),
                    getClickUrl(share.getClickurl()), UmengShareManager.getUMImage(this, share.getImageurl()), umShareListener);
        }
    }

    private String getTitle(String title){
        return isEmpty(title) ? "米果小站" : title;
    }

    private String getSummary(String summary){
        return isEmpty(summary) ? share.getTitle() : summary;
    }

    private String getShareRecordIdUrl(String clickUrl){
        if(isEmpty(clickUrl)){
            return ServerUrl.getAppH5Url();
        }

        if(!clickUrl.contains("mgxz")){
            return clickUrl;
        }

        if (!clickUrl.contains("/share_record_id/")) {
            return clickUrl + "/share_record_id/" + shareRecordId;
        }

        if(!isEmpty(shareRecordId) && !clickUrl.contains(shareRecordId)){
            int i = clickUrl.indexOf("/share_record_id/");
            String temp = clickUrl.substring(0, i);
            return temp + "/share_record_id/" + shareRecordId;
        }

        return ServerUrl.getAppH5Url();
    }

    private String getClickUrl(String clickUrl){
        return isEmpty(clickUrl) ? "https://m.mgxz.com" : (clickUrl.contains("http://") || clickUrl.contains("https://")) ? clickUrl : "http://" + clickUrl;
    }

    private boolean isEmpty(Object o){
        return null == o || "".equals(o.toString());
    }

    private UMShareListener umShareListener=new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            startRedPacketResultActivity();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            startRedPacketResultActivity();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            startRedPacketResultActivity();
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private boolean isRequestOk = false;

    private void onShareClick(){
        TreeMap<String,String> params = new TreeMap<>();
        params.put("order_id",order_id);
        params.put("token", App.getInstance().getToken());
        params.put("method","OnlinePayShareProfit");
        OkHttpUtil.getInstance().get(params, new HttpCallback<StatusBean>() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccessWithBean(StatusBean statusBean) {
                if (statusBean!=null ){
                    if ("200".equalsIgnoreCase(statusBean.getStatusCode())){
                        isRequestOk = true;
                    }

                }
            }
        });
    }

    private void startRedPacketResultActivity(){
        if (isRequestOk){
            Intent intent =new Intent(this,RedPacketResultActivity.class);
            intent.putExtra(IntentKey.ICON,face_icon);
            intent.putExtra(IntentKey.MONEY,money);
            intent.putExtra(IntentKey.DESC,showContent);
            startActivity(intent);
            finish();
        }else {
            // api request error
        }
    }
    private void getRecordId() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(this, new CallbackView() {
                @Override
                public void onSuccess(String responseBody) {

                }

                @Override
                public void onSuccess(String method, List datas) {
                    if (CommonConstants.CREATE_SHARE_RECORD.equals(method)) {
                        if (!SDCollectionUtil.isEmpty(datas)) {
                            ModelCreateShareRecord bean = (ModelCreateShareRecord) datas.get(0);
                            shareRecordId = bean.getId();
                        }
                    }
                }

                @Override
                public void onFailue(String responseBody) {

                }

                @Override
                public void onFinish(String method) {

                }
            });
        }
        commonHttpHelper.createShareRecord(Constant.ShareType.GOODS, "");
    }
}
