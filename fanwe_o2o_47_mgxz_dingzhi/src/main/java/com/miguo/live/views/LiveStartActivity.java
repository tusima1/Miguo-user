package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveStartBinding;
import com.fanwe.seller.views.MineShopActivity;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.miguo.live.model.DataBindingLiveStart;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.live.presenters.TencentHttpHelper;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity implements CallbackView {

    DataBindingLiveStart dataBindingLiveStart;
    private com.tencent.qcloud.suixinbo.presenters.LoginHelper mLoginHelper;
    private TencentHttpHelper tencentHttpHelper;
    private String token;
    /**
     * 签名后 的userid
     */
    String usersig;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();

        binding.setLive(dataBindingLiveStart);
        mLoginHelper = new com.tencent.qcloud.suixinbo.presenters.LoginHelper(this, this);
        tencentHttpHelper = new TencentHttpHelper(this);
        init();

    }

    public void gotoAuthActivity() {
        Intent intent = new Intent(LiveStartActivity.this, LiveAuthActivity.class);
        startActivity(intent);
        finish();
    }

    public void init() {
        token = App.getApplication().getToken();
        if (TextUtils.isEmpty(token)) {
            goToLoginActivity();
        } else {
            String is_host = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIs_host();
            if ("0".equals(is_host)) {
                SDToast.showToast("您还未成为主播");
                gotoAuthActivity();
            } else {
                dataBindingLiveStart.shopName.set("选择你的消费场所");
                dataBindingLiveStart.isLiveRight.set(true);
                userid = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
                usersig = App.getInstance().getUserSign();
                //注册腾讯并申请房间号。
                if (TextUtils.isEmpty(userid)) {
                    goToLoginActivity();
                }
                if (TextUtils.isEmpty(usersig)) {
                    getSign();
                }
            }
        }
    }

    /**
     * 取用户签名。
     */
    public void getSign() {
        MgCallback mgCallback = new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Gson gson = new Gson();
                RootGenerateSign rootGenerateSign = gson.fromJson(responseBody, RootGenerateSign.class);
                List<ResultGenerateSign> resultGenerateSigns = rootGenerateSign.getResult();
                if (SDCollectionUtil.isEmpty(resultGenerateSigns)) {
                    SDToast.showToast("获取用户签名失败。");
                    return;
                }
                ResultGenerateSign resultGenerateSign = resultGenerateSigns.get(0);
                List<ModelGenerateSign> modelGenerateSign = resultGenerateSign.getBody();

                if (modelGenerateSign != null && modelGenerateSign.size() > 0 && modelGenerateSign.get(0) != null) {
                    usersig = modelGenerateSign.get(0).getUsersig();
                    MySelfInfo.getInstance().setUserSig(usersig);
                    App.getInstance().setUserSign(usersig);
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast("获取用户签名失败。");
            }
        };
        tencentHttpHelper.getSign(token, mgCallback);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_shop_name:
                Intent intent = new Intent(LiveStartActivity.this, MineShopActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.iv_qq_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.QQ);
                break;
            case R.id.iv_weixin_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.WEIXIN);
                break;
            case R.id.iv_friend_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.FRIEND);
                break;
            case R.id.iv_sina_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.SINA);
                break;
            case R.id.iv_qqzone_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.QQZONE);
                break;
            case R.id.btn_start_live_start:
                startLive();
                break;
        }
    }

    boolean isShare;

    @Override
    protected void onResume() {
        super.onResume();
        if (isShare) {
            isShare = false;
            //创建房间号并进入主播页
            createAvRoom();
            finish();
        }
    }

    private void startLive() {
        if (dataBindingLiveStart.isLiveRight.get()) {
            isShare = true;
            SHARE_MEDIA platform = SHARE_MEDIA.QQ;
            //已认证的，去直播
            if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQ) {
                platform = SHARE_MEDIA.QQ;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.WEIXIN) {
                platform = SHARE_MEDIA.WEIXIN;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.FRIEND) {
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.SINA) {
                platform = SHARE_MEDIA.SINA;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQZONE) {
                platform = SHARE_MEDIA.QZONE;
            }
            UmengShareManager.share(platform, this, "", "直播开始分享", "http://www.mgxz.com/", UmengShareManager.getUMImage(this, "http://www.mgxz.com/pcApp/Common/images/logo2.png"), null);

        } else {
            //未认证的，去认证
            startActivity(new Intent(this, LiveAuthActivity.class));
        }
    }

    public void goToLoginActivity() {
        Intent intent = new Intent(LiveStartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 8888) {
            dataBindingLiveStart.shopName.set(CurLiveInfo.modelShop.getShop_name());
            return;
        }
    }

    /**
     * 进入直播Activity(创建直播)
     */

    public void createAvRoom() {
        boolean isAvStart = App.getInstance().isAvStart();
        boolean isImlogin = App.getInstance().isImLoginSuccess();
        if (!isImlogin) {
            mLoginHelper.imLogin(userid, usersig, new MgCallback() {
                @Override
                public void onErrorResponse(String message, String errorCode) {
                    SDToast.showToast("IM 注册失败，请重试");
                }

                @Override
                public void onSuccessResponse(String responseBody) {
                    super.onSuccessResponse(responseBody);
                    goToLive();
                }
            });
        } else if (!isAvStart) {
            mLoginHelper.getToRoomAndStartAV(new MgCallback() {
                @Override
                public void onErrorResponse(String message, String errorCode) {
                    SDToast.showToast("进入房间失败。");
                }

                @Override
                public void onSuccessResponse(String responseBody) {
                    super.onSuccessResponse(responseBody);
                    goToLive();
                }
            }, true);

        } else {
            goToLive();
        }


    }

    /**
     * 进入主播页。
     */
    private void goToLive() {
        UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
        MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);
        String nickName = "直播";
        if(userInfoNew!=null)
        {
            nickName = userInfoNew.getNick();
            if(TextUtils.isEmpty(nickName)||"null".equals(nickName)){
                nickName = userInfoNew.getUser_name();
            }
            if(TextUtils.isEmpty(nickName)||"null".equals(nickName)){
                nickName = userInfoNew.getUser_id();
            }
        }
        MySelfInfo.getInstance().setNickName(nickName);
        CurLiveInfo.setHostName(nickName);

        String avatar ="";
        if(App.getInstance().getmUserCurrentInfo()!=null){
            UserCurrentInfo currentInfo = App.getInstance().getmUserCurrentInfo();
            if(currentInfo.getUserInfoNew()!=null){
                avatar = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
            }
        }

        if(TextUtils.isEmpty(avatar)||"null".equals(avatar.trim())){
            MySelfInfo.getInstance().setAvatar(avatar);
        }
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        this.startActivity(intent);


    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

    }
}
