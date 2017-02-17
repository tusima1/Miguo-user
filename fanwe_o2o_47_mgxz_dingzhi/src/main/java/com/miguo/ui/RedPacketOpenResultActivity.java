package com.miguo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.didikee.uilibs.utils.StatusBarUtil;
import com.fanwe.PayActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.createShareRecord.ModelCreateShareRecord;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.entity.StatusBean;
import com.miguo.ui.view.customviews.ArcDrawable;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.TreeMap;

import static com.miguo.live.views.LiveAuthActivity.datas;

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

    private ShareInfoBean shareInfo;
    private String order_id = "";

    private SHARE_MEDIA platform;
    private CommonHttpHelper commonHttpHelper;
    private String shareRecordId = "";//分享id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_red_packet_open_result);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(this, Color.parseColor("#D23838"),0);
        setListener();
        initView();
        shareInfo = new ShareInfoBean();
        shareInfo.setClickurl("https://github.com/didikee");
        shareInfo.setImageurl("http://files.softicons.com/download/game-icons/super-mario-icons-by-sandro-pereira/png/48/Mushroom - 1UP.png");
        shareInfo.setSummary("hoisiodso就努力开发建设");
        shareInfo.setTitle("测试测试");
    }

    private void getIntentData() {
//        Intent intent = getIntent();
//        if (intent==null){
//            finish();
//            return;
//        }
//        order_id = intent.getStringExtra("order_id");
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
        if (shareInfo!=null){
            onShareClick();
            UmengShareManager.share(platform, this,
                    shareInfo.getTitle(),
                    shareInfo.getSummary(),
                    shareInfo.getClickurl(), UmengShareManager.getUMImage(this, shareInfo.getImageurl()), umShareListener);
        }
    }

    private UMShareListener umShareListener=new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            startRedPacketResultActivity();
            Toast.makeText(RedPacketOpenResultActivity.this, ""+share_media.toString()+"成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            startRedPacketResultActivity();
            Toast.makeText(RedPacketOpenResultActivity.this, ""+share_media.toString()+"失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            startRedPacketResultActivity();
            Toast.makeText(RedPacketOpenResultActivity.this, ""+share_media.toString()+"取消", Toast.LENGTH_SHORT).show();
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
        params.put("order_id","");
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
            startActivity(new Intent(this,RedPacketResultActivity.class));
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
        if (buyItem > 1) {
            commonHttpHelper.createShareRecord(Constant.ShareType.WEB_HOME, "");
        } else {
            commonHttpHelper.createShareRecord(Constant.ShareType.GOODS, "");
        }
    }
}
