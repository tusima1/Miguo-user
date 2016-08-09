package com.tencent.qcloud.suixinbo.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.presents.IMUserInfoHelper;
import com.google.gson.Gson;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.applyRoom.ModelApplyRoom;
import com.miguo.live.model.applyRoom.ResultApplyRoom;
import com.miguo.live.model.applyRoom.RootApplyRoom;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.utils.SxbLog;

import java.util.List;
import java.util.TreeMap;

/**
 * 登录的数据处理类
 */
public class LoginHelper extends com.tencent.qcloud.suixinbo.presenters.Presenter {
    private Context mContext;
    private static final String TAG = LoginHelper.class.getSimpleName();
    private int RoomId = -1;
    private CallbackView mView;
    private IMUserInfoHelper imUserInfoHelper;
    private Activity mactivity;

    public LoginHelper(Context context) {
        mContext = context;
        imUserInfoHelper = new IMUserInfoHelper();
    }

    public LoginHelper(Activity activity) {
        this.mactivity = activity;
        imUserInfoHelper = new IMUserInfoHelper();
    }

    public LoginHelper(Context context, CallbackView mView) {
        this.mView = mView;
        mContext = context;
        imUserInfoHelper = new IMUserInfoHelper();
    }


    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig  用户签名
     */
    public void imLoginWithoutGetRoom(String identify, String userSig) {
        MySelfInfo.getInstance().setId(identify);
        MySelfInfo.getInstance().setUserSig(userSig);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mContext, "IMLogin fail ：" + i + " msg " + s, Toast.LENGTH_SHORT).show();
                        if (mView != null) {
                            mView.onFailue("IM 认证失败。");
                        }
                    }

                    @Override
                    public void onSuccess() {
                        imUserInfoHelper.setMyNickName("");
                        imUserInfoHelper.setMyAvator("");
                        App.getInstance().setImLoginSuccess(true);
                    }
                });
    }


    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig  用户签名
     */
    public void imLogin(String identify, String userSig, final MgCallback callback) {
        MySelfInfo.getInstance().setId(identify);
        MySelfInfo.getInstance().setUserSig(userSig);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mContext, "IMLogin fail ：" + i + " msg " + s, Toast.LENGTH_SHORT).show();
                        mView.onFailue("IM 认证失败。");
                        callback.onErrorResponse("", null);
                    }

                    @Override
                    public void onSuccess() {
                        imUserInfoHelper.setMyNickName("");
                        imUserInfoHelper.setMyAvator("");
                        App.getInstance().setImLoginSuccess(true);
                        startAVSDK();
                        getRoomNum();
                        App.getInstance().setAvStart(true);
                        callback.onSuccessResponse("");
                    }
                });
    }

    /**
     * 登录imsdk
     *
     * @param identify 用户id
     * @param userSig  用户签名
     */
    public void imLogin(String identify, String userSig) {
        MySelfInfo.getInstance().setId(identify);
        MySelfInfo.getInstance().setUserSig(userSig);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(mContext, "IMLogin fail ：" + i + " msg " + s, Toast.LENGTH_SHORT).show();
                        if (mView != null) {
                            mView.onFailue("IM 认证失败。");
                        }
                    }

                    @Override
                    public void onSuccess() {
                        startAVSDK();
                        imUserInfoHelper.setMyNickName("");
                        imUserInfoHelper.setMyAvator("");
                        App.getInstance().setImLoginSuccess(true);
                        App.getInstance().setAvStart(true);
                        if (MySelfInfo.getInstance().isCreateRoom() == true) {
                            getRoomNum();
                        }

                    }
                });
    }


    /*登录imsdk
    *
            * @param identify 用户id
    * @param userSig  用户签名
    */
    public void imLogin(String identify, String userSig, final MgCallback callback, final boolean host) {
        MySelfInfo.getInstance().setId(identify);
        MySelfInfo.getInstance().setUserSig(userSig);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {

                        mView.onFailue("IM 认证失败。");
                        if (callback != null) {
                            callback.onErrorResponse("IM 认证失败。", "400");
                        }
                    }

                    @Override
                    public void onSuccess() {
                        imUserInfoHelper.setMyNickName("");
                        imUserInfoHelper.setMyAvator("");
                        App.getInstance().setImLoginSuccess(true);
                        App.getInstance().setAvStart(true);
                        if (host) {
                            getRoomNum();
                        }

                        startAVSDK();
                        if (callback != null) {
                            callback.onSuccessResponse("");
                        }
                    }
                });
    }

    /**
     * 申请 房间号并进入房间。
     */
    public void getToRoomAndStartAV(MgCallback callback, boolean host) {
        boolean oldAvStart = App.getInstance().isAvStart();
        if (App.getInstance().isImLoginSuccess()) {
            App.getInstance().setAvStart(true);
            if (host) {
                getRoomNum();
            }
            if(!oldAvStart) {
                startAVSDK();
            }
            callback.onSuccessResponse("");
        } else {
            callback.onErrorResponse("", null);
            SDToast.showToast("未进行IM 注册 。");
        }
    }

    /**
     * 腾讯登录 。
     *
     * @param identify
     * @param userSig
     */
    public void imLogin(String identify, String userSig, TIMCallBack callBack) {
        MySelfInfo.getInstance().setId(identify);
        MySelfInfo.getInstance().setUserSig(userSig);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                callBack);
    }

    /**
     * 进入主播页。
     */
    private void goToLive() {
        UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
        MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
        Intent intent = new Intent(mContext, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        mContext.startActivity(intent);
    }

    /**
     * 退出imsdk
     * <p/>
     * 退出成功会调用退出AVSDK
     */
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                SxbLog.e(TAG, "IMLogout fail ：" + i + " msg " + s);
            }

            @Override
            public void onSuccess() {
                SxbLog.i(TAG, "IMLogout succ !");
                //清除本地缓存
                MySelfInfo.getInstance().clearCache(mContext);
                //反向初始化avsdk
                stopAVSDK();
            }
        });

    }

    /**
     * 向用户服务器获取自己房间号
     */
    private void getRoomNum() {

        MgCallback mgCallback = new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Gson gson = new Gson();
                RootApplyRoom rootApplyRoom = gson.fromJson(responseBody, RootApplyRoom.class);
                List<ResultApplyRoom> resultApplyRooms = rootApplyRoom.getResult();
                if (SDCollectionUtil.isEmpty(resultApplyRooms)) {
                    SDToast.showToast("申请房间号失败");
                    mView.onFailue("获取房间号错误!");
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
                        MGToast.showToast("获取房间号错误!");
                        mView.onFailue("获取房间号错误!");
                        return;
                    }
                    if (roomId == -1) {
                        MGToast.showToast("获取房间号错误!");
                        mView.onFailue("获取房间号错误!");
                        return;
                    } else {
                        MySelfInfo.getInstance().setMyRoomNum(roomId);
                        MySelfInfo.getInstance().writeToCache(mContext.getApplicationContext());

                       // goToLive();
                        mView.onSuccess("");

                        Log.e("live", "room_id:" + room_id);
                    }
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        };

        applyRoom(CurLiveInfo.modelShop.getId(), mgCallback);
    }

    /**
     * 申请直播房间ID
     *
     * @param shop_id
     */
    public void applyRoom(String shop_id, MgCallback mgCallback) {
        String token = App.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
            SDToast.showToast("token为空。");
            return;
        }

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("shop_id", shop_id);
        params.put("method", LiveConstants.APPLY_ROOM);

        OkHttpUtils.getInstance().get(null, params, mgCallback);

    }


    /**
     * 进入直播间
     */
    private void createAvRoom() {
//        如果是自己
        UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
        MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
        Intent intent = new Intent(mContext, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        mContext.startActivity(intent);
    }


    /**
     * 初始化AVSDK
     */
    public void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign = MySelfInfo.getInstance().getUserSig();
        int appId = Constants.SDK_APPID;

        int ccType = Constants.ACCOUNT_TYPE;
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();

        Log.e("live", "初始化AVSDK");
    }

    /**
     * 反初始化AVADK
     */
    public void stopAVSDK() {
        QavsdkControl.getInstance().stopContext();
    }


    @Override
    public void onDestory() {
        mContext = null;
    }

}
