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
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.createShareRecord.ModelCreateShareRecord;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveStartBinding;
import com.fanwe.seller.views.MineShopActivity;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.utils.MGDictUtil;
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
import com.miguo.utils.BaseUtils;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
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
    private boolean clickEnable = true;
    private CommonHttpHelper commonHttpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();
        binding.setLive(dataBindingLiveStart);
        mLoginHelper = new com.tencent.qcloud.suixinbo.presenters.LoginHelper(this, this);
        tencentHttpHelper = new TencentHttpHelper(this);
        CurLiveInfo.modelShop = null;
        init();
        getRecordId();
    }

    public void init() {
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
                    MGToast.showToast("获取用户签名失败。");
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
                MGToast.showToast("获取用户签名失败。");
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
                if (clickEnable) {
                    if (CurLiveInfo.modelShop == null || TextUtils.isEmpty(CurLiveInfo.modelShop.getId())) {
                        MGToast.showToast("请选择你的消费场所");
                        return;
                    } else {
                        if (!TextUtils.isEmpty(usersig)) {
                            startLive();
                        } else {
                            goToLoginActivity();
                        }
                    }
                }
                break;
            case R.id.iv_close_live_start:
                finish();
                break;
        }
    }

    private String shareRecordId;

    private void startLive() {
        if (dataBindingLiveStart.isLiveRight.get()) {
            getRecordId();
            SHARE_MEDIA platform = SHARE_MEDIA.QQ;
            //已认证的，去直播
            if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQ) {
                if (!BaseUtils.isQQClientAvailable(this)) {
                    MGToast.showToast("未安装QQ");
                    return;
                }
                platform = SHARE_MEDIA.QQ;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.WEIXIN) {
                if (!BaseUtils.isWeixinAvilible(this)) {
                    MGToast.showToast("未安装微信");
                    return;
                }
                platform = SHARE_MEDIA.WEIXIN;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.FRIEND) {
                if (!BaseUtils.isWeixinAvilible(this)) {
                    MGToast.showToast("未安装微信");
                    return;
                }
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.SINA) {
                platform = SHARE_MEDIA.SINA;
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQZONE) {
                if (!BaseUtils.isQQClientAvailable(this)) {
                    MGToast.showToast("未安装QQ");
                    return;
                }
                platform = SHARE_MEDIA.QZONE;
            }
            //点击按钮后，锁住按钮
            clickEnable = false;

            String imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
            if (!TextUtils.isEmpty(App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon())) {
                imageUrl = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
            } else if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                imageUrl = MGDictUtil.getShareIcon();
            }
            String title = "送你钻石，看直播，拿优惠";
            String nick = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getNick();
            String content = "钻石、红包免费拿，吃喝玩乐优惠领不停，米果小站，分享你身边的精彩生活，来陪我吧[" + nick + "]正在直播中";
            if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                //朋友圈
                title = content;
            }
            UmengShareManager.share(platform, this, title, content, ServerUrl.getAppH5Url() + "share/live/uid/"
                            + App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id() + "/share_record_id/" + shareRecordId,
                    UmengShareManager.getUMImage(this, imageUrl), shareResultCallback);
        } else {
            //未认证的，去认证
            startActivity(new Intent(this, LiveAuthActivity.class));
        }
    }

    private UMShareListener shareResultCallback = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            MGToast.showToast("分享成功");
            createAvRoom();
            clickEnable = true;
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            MGToast.showToast("分享失败");
            createAvRoom();
            clickEnable = true;
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            MGToast.showToast("分享取消");
            createAvRoom();
            clickEnable = true;
        }
    };


    public void goToLoginActivity() {
        Intent intent = new Intent(LiveStartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 8888) {
            dataBindingLiveStart.shopName.set(CurLiveInfo.modelShop.getShop_name());
            CurLiveInfo.setShopID(CurLiveInfo.modelShop.getId());
            return;
        }
    }


    /**
     * 进入直播Activity(创建直播)
     */
    public void createAvRoom() {
        boolean isImlogin = App.getInstance().isImLoginSuccess();
        if (!isImlogin) {
            mLoginHelper.imLogin(userid, usersig, new MgCallback() {
                @Override
                public void onErrorResponse(String message, String errorCode) {
                    MGToast.showToast("IM 注册失败，请重试");
                }

                @Override
                public void onSuccessResponse(String responseBody) {

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
                            MGToast.showToast("申请房间号失败");
                            return;
                        }
                        ResultApplyRoom resultApplyRoom = resultApplyRooms.get(0);
                        List<ModelApplyRoom> modelApplyRooms = resultApplyRoom.getBody();
                        if (modelApplyRooms != null && modelApplyRooms.size() > 0 && modelApplyRooms.get(0) != null) {
                            String room_id = modelApplyRooms.get(0).getRoom_id();
                            Integer roomId;
                            try {
                                roomId = Integer.valueOf(room_id);
                            } catch (Exception e) {
                                MGToast.showToast("获取房间号错误!");
                                return;
                            }
                            if (roomId == -1) {
                                MGToast.showToast("获取房间号错误!");
                                return;
                            } else {
                                App.getInstance().setAvStart(true);
                                App.getInstance().addLiveRoomIdList(roomId + "");
                                MySelfInfo.getInstance().setMyRoomNum(roomId);

                                MySelfInfo.getInstance().writeToCache(getApplicationContext());

                                goToLive();
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(String message, String errorCode) {
                        MGToast.showToast(message);
                    }
                };
                mLoginHelper.applyRoom(CurLiveInfo.modelShop.getId(), mgCallback);
                //--------------------向业务服务器申请房间号-------------------------
            } else {
                MGToast.showToast("请先选择一个直播场所。");
            }
        }


    }


    /**
     * 进入主播页。
     */
    private void goToLive() {
        //判断网络环境
        boolean connected = NetWorkStateUtil.isConnected(this);
        if (!connected) {
            MGToast.showToast("没有网络,请检测网络环境!");
            return;
        }
        if (QavsdkControl.getInstance().getAVContext() == null) {
            App.getInstance().startAVSDK();

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
        switch (method) {
            case CommonConstants.CREATE_SHARE_RECORD:
                if (!SDCollectionUtil.isEmpty(datas)) {
                    ModelCreateShareRecord bean = (ModelCreateShareRecord) datas.get(0);
                    shareRecordId = bean.getId();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {
        MGToast.showToast(responseBody);
    }

    @Override
    public void onFinish(String method) {

    }

    private void getRecordId() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(LiveStartActivity.this, this);
        }
        commonHttpHelper.createShareRecord(Constant.ShareType.LIVE, "");
    }
}
