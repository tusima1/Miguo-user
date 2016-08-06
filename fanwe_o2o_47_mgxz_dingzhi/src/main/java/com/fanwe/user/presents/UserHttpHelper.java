package com.fanwe.user.presents;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.home.model.ResultLive;
import com.fanwe.home.model.Room;
import com.fanwe.home.model.RootLive;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.LiveConstants;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/6.
 */
public class UserHttpHelper implements IHelper {

    private static final String TAG = UserHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public UserHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        if (!TextUtils.isEmpty(token)) {
            return token;
        } else {
            token = userCurrentInfo.getToken();
            return token;
        }
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(String key, String value) {
        getToken();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put(key, value);
        params.put("method", UserConstants.USER_INFO_METHOD);

        OkHttpUtils.getInstance().put(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(UserConstants.USER_INFO_METHOD, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        }, null);

    }

    @Override
    public void onDestroy() {

    }
}
