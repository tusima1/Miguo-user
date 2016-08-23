package com.fanwe.user.presents;

import android.content.Context;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.model.getPersonalHome.ResultPersonalHome;
import com.fanwe.user.model.getPersonalHome.RootPersonalHome;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.utils.MGUIUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/6.
 */
public class UserHttpHelper implements IHelper {

    private static final String TAG = UserHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView2 mView;
    private Context mContext;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public UserHttpHelper(Context mContext, CallbackView2 mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
    }

    public String getToken() {
        return userCurrentInfo.getToken();
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(String key, String value) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
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
        });

    }

    /**
     * 我的界面数据展示
     */
    public void getPersonalHome() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.PERSONALHOME);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Log.e("test", "responseBody PERSONALHOME:" + responseBody);
                //200为正常的返回 。
                Gson gson = new Gson();
                RootPersonalHome rootPersonalHome = gson.fromJson(responseBody, RootPersonalHome
                        .class);
                if (rootPersonalHome != null) {
                    List<ResultPersonalHome> result = rootPersonalHome.getResult();
                    if (result != null && result.size() > 0) {
                        final List<ModelPersonalHome> body = result.get(0).getBody();
                        if (body != null && body.size() > 0) {
                            MGUIUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.onSuccess(UserConstants.PERSONALHOME, body);
                                }
                            });
                        }
                    }
                } else {
                    mView.onFailue(responseBody);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFinish(UserConstants.PERSONALHOME);
                    }
                });
            }
        });
    }

    /**
     * 我的战队
     */
    public void getMyDistributionCorps(String type, String rank, int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("type", type);
        params.put("rank", rank);
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("method", UserConstants.MY_DISTRIBUTION_CROPS);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(UserConstants.MY_DISTRIBUTION_CROPS, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    @Override
    public void onDestroy() {

    }
}
