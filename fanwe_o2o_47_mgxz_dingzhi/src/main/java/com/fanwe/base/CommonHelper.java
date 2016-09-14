package com.fanwe.base;

import android.content.Context;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;

import java.util.TreeMap;

/**
 * 公共的请求接口
 * Created by Administrator on 2016/7/26.
 */
public class CommonHelper extends Presenter {

    private Context mContext;
    private static final String TAG = CommonHelper.class.getSimpleName();
    private CallbackView mView;
    /**
     * 获取验证码。
     */
    private static String SEND_CAPTCHA = "SendCaptcha";

    public CommonHelper(Context context) {
        mContext = context;
    }

    public CommonHelper(Context context, CallbackView mView) {
        mContext = context;
        this.mView = mView;
    }


    /**
     * 获取验证码接口。
     *
     * @param mobile 手机号
     * @param type 1为用户注册，2通过验证码修改密码，3为申请提现 4 快捷登录和第三方绑定手机号。
     */
    public void doGetCaptcha(String mobile, int type) {
        doGetCaptcha(mobile, type, null);
    }

    public void doGetCaptcha(String mobile, int type, MgCallback callback) {
        MgCallback mgCallback = null;
        if (callback != null) {
            mgCallback = callback;

        } else {
            mgCallback = new MgCallback() {

                @Override
                public void onSuccessResponse(String body) {
                    mView.onSuccess(body);
                }

                @Override
                public void onErrorResponse(String message, String errorCode) {

                }
            };
        }

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", mobile);
        params.put("type", type + "");
        params.put("method", SEND_CAPTCHA);
        OkHttpUtils.getInstance().get(null, params, mgCallback);
    }

    /**
     * 验证手机号是否已经存在。
     *
     * @param mobile
     */
    public void doCheckMobileExist(String mobile, MgCallback callback) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", mobile);
        params.put("method", UserConstants.USER_CHECK_EXIST);
        OkHttpUtils.getInstance().get(null, params, callback);
    }

    @Override
    public void onDestory() {
        mView = null;
    }
}
