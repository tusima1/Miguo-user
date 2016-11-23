package com.tencent.qcloud.suixinbo.presenters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.fanwe.constant.ServerUrl;
import com.fanwe.o2o.miguo.R;
import com.tencent.TIMCallBack;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMUserStatusListener;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.utils.SxbLog;

import tencent.tls.platform.TLSAccountHelper;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSRefreshUserSigListener;
import tencent.tls.platform.TLSUserInfo;


/**
 * 初始化
 * 包括imsdk等
 */
public class InitBusinessHelper {
    private static String TAG = "InitBusinessHelper";

    private InitBusinessHelper() {
    }

    public static TLSLoginHelper getmLoginHelper() {
        return mLoginHelper;
    }

    public static TLSAccountHelper getmAccountHelper() {
        return mAccountHelper;
    }

    private static TLSLoginHelper mLoginHelper;
    private static TLSAccountHelper mAccountHelper;
    private static String appVer = "1.0";


    /**
     * 初始化App
     */
    public static void initApp(final Context context) {
        //初始化avsdk imsdk
        QavsdkControl.initQavsdk(context);
        TIMManager.getInstance().disableBeaconReport();
        MySelfInfo.getInstance().getCache(context);
        switch(MySelfInfo.getInstance().getLogLevel()){
        case OFF:
            TIMManager.getInstance().setLogLevel(TIMLogLevel.OFF);
            break;
        case WARN:
            TIMManager.getInstance().setLogLevel(TIMLogLevel.WARN);
            break;
        case DEBUG:
            TIMManager.getInstance().setLogLevel(TIMLogLevel.DEBUG);
            break;
        case INFO:
            TIMManager.getInstance().setLogLevel(TIMLogLevel.INFO);
            break;
        default:
            break;
        }
        TIMManager.getInstance().init(context);

        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                SxbLog.w(TAG, "onForceOffline->entered!");
                Toast.makeText(context, context.getString(R.string.tip_force_offline), Toast.LENGTH_SHORT).show();
                context.sendBroadcast(new Intent(Constants.BD_EXIT_APP));
            }

            @Override
            public void onUserSigExpired() {
                SxbLog.w(TAG, "onUserSigExpired->entered!");
                refreshSig();
            }
        });

        //QAL初始化
        //初始化TLS
        initTls(context);

        //初始化CrashReport系统
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(context);

    }

    /**
     * 重新登陆IM
     * @param identify
     * @param userSig
     */
    private static void reLoginIM(String identify, String userSig){
        TIMUser user = new TIMUser();

        int appId = Constants.SDK_APPID;
        int ccType = Constants.ACCOUNT_TYPE;
        if(ServerUrl.DEBUG){
            appId = Constants.SDK_APPID_TEST;
            ccType = Constants.ACCOUNT_TYPE_Test;
        }

        user.setAccountType(String.valueOf(ccType));
        user.setAppIdAt3rd(String.valueOf(appId));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                appId,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        SxbLog.e(TAG, "reLoginIM fail ：" + i + "|" + s);
                    }

                    @Override
                    public void onSuccess() {
                        SxbLog.i(TAG, "reLoginIM IMLogin succ !");
                    }
                });
    }

    /**
     * 更新票据
     */
    private static void refreshSig(){
        final String userId = MySelfInfo.getInstance().getId();
        if (TextUtils.isEmpty(userId)){
            SxbLog.w(TAG, "refreshSig->with empty identifier");
            return;
        }

        // 更新票据
        mLoginHelper.TLSRefreshUserSig(MySelfInfo.getInstance().getId(), new TLSRefreshUserSigListener() {
            @Override
            public void OnRefreshUserSigSuccess(TLSUserInfo tlsUserInfo) {
                reLoginIM(userId, mLoginHelper.getUserSig(userId));
            }

            @Override
            public void OnRefreshUserSigFail(TLSErrInfo tlsErrInfo) {
                SxbLog.w(TAG, "OnRefreshUserSigFail->"+tlsErrInfo.ErrCode+"|"+tlsErrInfo.Msg);
            }

            @Override
            public void OnRefreshUserSigTimeout(TLSErrInfo tlsErrInfo) {
                SxbLog.w(TAG, "OnRefreshUserSigTimeout->"+tlsErrInfo.ErrCode+"|"+tlsErrInfo.Msg);
            }
        });
    }


    /**
     * 初始化TLS登录模块
     *
     * @param context
     */
    public static void initTls(Context context) {
        int appId = Constants.SDK_APPID;
        int ccType = Constants.ACCOUNT_TYPE;
        if(ServerUrl.DEBUG){
            appId = Constants.SDK_APPID_TEST;
            ccType = Constants.ACCOUNT_TYPE_Test;
        }
        mLoginHelper = TLSLoginHelper.getInstance().init(context, appId, ccType, appVer);
        mLoginHelper.setTimeOut(5000);
        mAccountHelper = TLSAccountHelper.getInstance().init(context, appId, ccType, appVer);
        mAccountHelper.setTimeOut(5000);
    }

}
