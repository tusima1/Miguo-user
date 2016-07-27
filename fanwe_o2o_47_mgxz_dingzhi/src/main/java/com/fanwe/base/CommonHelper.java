package com.fanwe.base;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.fanwe.fragment.LoginFragment;
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
    private static String  SEND_CAPTCHA = "SendCaptcha";
    public CommonHelper(Context context) {
        mContext = context;
    }

    public CommonHelper(Context context, CallbackView mView) {
        mContext = context;
        this.mView = mView;
    }


    /**
     *获取验证码接口。
     * @param mobile 手机号
     * @param type 1为用户注册，2通过验证码修改密码，3为申请提现
     */
    public void doGetCaptcha(String mobile,  int  type) {
        doGetCaptcha(mobile,type,null);
    }

    public void doGetCaptcha(String mobile,int type,MgCallback callback){
        MgCallback mgCallback = null;
        if(callback!=null){
            mgCallback = callback;

      }else{
            mgCallback = new MgCallback(){
                @Override
                public void onSuccessResponse(String responseBody) {
                    Log.d("responseBody:",responseBody);
                    mView.onSuccess(responseBody);
                }
            };
        }

        TreeMap<String, String> params = new TreeMap<String,String>();
        params.put("mobile",mobile);
        params.put("type",1+"");
        params.put("method", SEND_CAPTCHA);
        OkHttpUtils.getInstance().get(null,params,mgCallback);
    }
    @Override
    public void onDestory() {
        mView = null;
    }
}
