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
import com.miguo.live.model.applyRoom.ModelApplyRoom;
import com.miguo.live.model.applyRoom.ResultApplyRoom;
import com.miguo.live.model.applyRoom.RootApplyRoom;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.live.presenters.TencentHttpHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.definetion.IntentKey;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity implements CallbackView {

    private DataBindingLiveStart dataBindingLiveStart;
    private com.tencent.qcloud.suixinbo.presenters.LoginHelper mLoginHelper;
    private TencentHttpHelper tencentHttpHelper;
//    private String token;
    /**
     * 签名后 的userid
     */
    private String usersig;
    private String userid;
    /**
     * 是否分享。
     */
    private boolean isShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();
        isShare = false;
        binding.setLive(dataBindingLiveStart);
        mLoginHelper = new com.tencent.qcloud.suixinbo.presenters.LoginHelper(this, this);
        tencentHttpHelper = new TencentHttpHelper(this);
        CurLiveInfo.modelShop = null;
        init();

    }

    public void init() {
//        token = App.getApplication().getToken();
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            goToLoginActivity();
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
        tencentHttpHelper.getSign(App.getInstance().getToken(), mgCallback);
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
                if (CurLiveInfo.modelShop == null || TextUtils.isEmpty(CurLiveInfo.modelShop.getId())) {
                    SDToast.showToast("请选择你的消费场所");
                    return;
                } else {
                    if (!TextUtils.isEmpty(usersig)) {
                        startLive();
                    } else {
                        goToLoginActivity();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isShare){
             createAvRoom();
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
//            createAvRoom();
            if(isShare){
//            createAvRoom();
            }
            isShare = true;
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
     * 分享
     */
    private void clickShare() {
        UmengShareManager.share(this, "", "用户直播分享", "http://www.mgxz.com/", UmengShareManager.getUMImage(this, "http://www.mgxz.com/pcApp/Common/images/logo2.png"), null);
    }

    /**
     * 进入直播Activity(创建直播)
     */

    public void createAvRoom() {
        boolean isAvStart = App.getInstance().isAvStart();
        boolean isImlogin = App.getInstance().isImLoginSuccess();
        if (!isImlogin) {
            //
            mLoginHelper.imLogin(userid, usersig, new MgCallback() {
                @Override
                public void onErrorResponse(String message, String errorCode) {
                    SDToast.showToast("IM 注册失败，请重试");
                }

                @Override
                public void onSuccessResponse(String responseBody) {
                    super.onSuccessResponse(responseBody);

                }
            });
        } else {
            if (!TextUtils.isEmpty(CurLiveInfo.modelShop.getId()) && !"null".equals(CurLiveInfo.modelShop.getId())) {
                //------向业务服务器申请房间号-------------------------------------
                MgCallback mgCallback = new MgCallback() {
                    @Override
                    public void onSuccessResponse(String responseBody) {
                        Gson gson = new Gson();
                        RootApplyRoom rootApplyRoom = gson.fromJson(responseBody, RootApplyRoom.class);
                        List<ResultApplyRoom> resultApplyRooms = rootApplyRoom.getResult();
                        if (SDCollectionUtil.isEmpty(resultApplyRooms)) {
                            SDToast.showToast("申请房间号失败");
                            return;
                        }
                        ResultApplyRoom resultApplyRoom = resultApplyRooms.get(0);
                        List<ModelApplyRoom> modelApplyRooms = resultApplyRoom.getBody();
                        if (modelApplyRooms != null && modelApplyRooms.size() > 0 && modelApplyRooms.get(0) != null) {
                            String room_id = modelApplyRooms.get(0).getRoom_id();
                            Integer roomId = -1;
                            try {
                                roomId = Integer.valueOf(room_id);
                            } catch (Exception e) {
                                SDToast.showToast("获取房间号错误!");
                                return;
                            }
                            if (roomId == -1) {
                                SDToast.showToast("获取房间号错误!");
                                return;
                            } else {
                                App.getInstance().setAvStart(true);
                                MySelfInfo.getInstance().setMyRoomNum(roomId);
                                MySelfInfo.getInstance().writeToCache(getApplicationContext());

                                goToLive();
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(String message, String errorCode) {
                        SDToast.showToast(message);
                    }
                };
                mLoginHelper.applyRoom(CurLiveInfo.modelShop.getId(), mgCallback);
                //--------------------向业务服务器申请房间号-------------------------
            } else {
                SDToast.showToast("请先选择一个直播场所。");
            }
        }


    }

    /**
     * 进入主播页。
     */
    private void goToLive() {
        //判断网络环境
        boolean connected = NetWorkStateUtil.isConnected(this);
        if (!connected){
            MGToast.showToast("没有网络,请检测网络环境!");
            return;
        }
        UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
        MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
        intent.putExtra(IntentKey.IS_ANCHOR, true);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);
        String nickName = "直播";
        if (userInfoNew != null) {
            nickName = userInfoNew.getNick();
            if (TextUtils.isEmpty(nickName) || "null".equals(nickName)) {
                nickName = userInfoNew.getUser_name();
            }
            if (TextUtils.isEmpty(nickName) || "null".equals(nickName)) {
                nickName = userInfoNew.getUser_id();
            }
        }
        MySelfInfo.getInstance().setNickName(nickName);
        CurLiveInfo.setHostName(nickName);

        String avatar = "";
        if (App.getInstance().getmUserCurrentInfo() != null) {
            UserCurrentInfo currentInfo = App.getInstance().getmUserCurrentInfo();
            if (currentInfo.getUserInfoNew() != null) {
                avatar = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
            }
        }
        MySelfInfo.getInstance().setAvatar(avatar);
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        this.startActivity(intent);
        finish();


    }

    @Override
    public void onSuccess(String responseBody) {
        goToLive();
    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {
        SDToast.showToast(responseBody);
    }
}
