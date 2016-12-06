package com.miguo.presenters.impl;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.constant.ServerUrl;
import com.fanwe.user.model.UserInfoNew;
import com.miguo.dao.IMLoginDao;
import com.miguo.dao.IMUserInfoDao;
import com.miguo.dao.TencentSignDao;
import com.miguo.dao.impl.IMLoginDaoImpl;
import com.miguo.dao.impl.IMUserInfoDaoImpl;
import com.miguo.dao.impl.TencentSignDaoImpl;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.presenters.TencentIMBindPresenter;
import com.miguo.view.BaseView;
import com.miguo.view.IMLoginView;
import com.miguo.view.IMUserInfoView;
import com.miguo.view.TencentIMBindPresenterView;
import com.miguo.view.TencentSignView;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

/**
 * Created by zlh on 2016/12/5.
 */

public class TencentIMBindPresenterImpl extends BasePresenterImpl implements TencentIMBindPresenter, TencentSignView, IMLoginView, IMUserInfoView {

    /** 以下接口在登录成功后被调用 */
    /**
     * 腾讯获取签名
     */
    TencentSignDao tencentSignDao;
    /**
     * IM登录接口
     */
    IMLoginDao imLoginDao;
    /**
     * 绑定用户信息到IM接口
     */
    IMUserInfoDao imUserInfoDao;
    /** 以上接口在登录成功后被调用 */

    public TencentIMBindPresenterImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    protected void initModels() {
        /** 以下接口在登录成功后被调用 */
        tencentSignDao = new TencentSignDaoImpl(this);
        imLoginDao = new IMLoginDaoImpl(this);
        imUserInfoDao = new IMUserInfoDaoImpl(this);
    }

    @Override
    public void tencentIMBinding() {
        if(TextUtils.isEmpty(App.getApplication().getToken())){
            getListener().tencentIMBindFinish();
            return;
        }
        handlerTencentSign(getApp().getToken());
    }

    /**
     * 获取腾讯签名
     *
     * @param token
     */
    private void handlerTencentSign(String token) {
        if (TextUtils.isEmpty(token)) {
            Log.d(tag, "handler tencent sign token is null...");
            return;
        }
        /**
         * {@link #getTencentSignSuccess(ModelGenerateSign)}
         * {@link #getTencentSignError()}
         */
        tencentSignDao.getTencentSign(token);
    }

    /**
     * IM登录
     * @param userId
     * @param usersig
     */
    private void handlerIMLogin(String userId, String usersig) {
        /**
         * {@link #imLoginSuccess()}
         * {@link #imLoginError(String)}
         */
        imLoginDao.imLogin(userId, usersig);
    }

    /**
     * 获取腾讯签名
     * 获取成功后需要调用IM登录
     */
    @Override
    public void getTencentSignSuccess(ModelGenerateSign sign) {
        if (null == sign) {
            return;
        }
        String usersig = sign.getUsersig();
        App.getInstance().setUserSign(usersig);
        MySelfInfo.getInstance().setUserSig(usersig);
        App.getInstance().setUserSign(usersig);
        String userId = MySelfInfo.getInstance().getId();

        if (TextUtils.isEmpty(userId)) {
            UserInfoNew currentInfo = App.getInstance().getCurrentUser();
            if (currentInfo != null) {
                userId = currentInfo.getUser_id();
            } else {
                return;
            }
        }
        handlerIMLogin(userId, usersig);
    }

    @Override
    public void getTencentSignError() {
        getListener().tencentIMBindFinish();
        Log.d(tag, "get tencent sign error..");
    }

    /**
     * IM登陆回调
     */
    @Override
    public void imLoginError(String message) {
        Log.d(tag, "im login error and the message is: " + message);
        getListener().tencentIMBindFinish();
    }

    /**
     * IM登录成功
     * 登录成功后要将用户名和头像绑定到IM
     */
    @Override
    public void imLoginSuccess() {
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            /**
             * 不需要回调
             */
            imUserInfoDao.updateTencentNickName(App.getInstance().getCurrentUser().getNick());
            imUserInfoDao.updateTencentAvatar(App.getInstance().getCurrentUser().getIcon());
        }
        /**
         * 开始直播AVSDK
         */
        startAVSDK();
        App.getInstance().setImLoginSuccess(true);
        getListener().tencentIMBindFinish();
    }

    /**
     * 初始化AVSDK
     */
    public void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign = MySelfInfo.getInstance().getUserSig();
        int appId = Constants.SDK_APPID;
        int ccType = Constants.ACCOUNT_TYPE;
        if (ServerUrl.DEBUG) {
            appId = Constants.SDK_APPID_TEST;
            ccType = Constants.ACCOUNT_TYPE_Test;
        }
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();
    }


    @Override
    public TencentIMBindPresenterView getListener() {
        return (TencentIMBindPresenterView)baseView;
    }
}
