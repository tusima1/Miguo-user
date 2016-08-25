package com.fanwe.user.presents;

import android.content.Context;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.model.getMyDistributionCorps.RootMyDistributionCorps;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.model.getPersonalHome.ResultPersonalHome;
import com.fanwe.user.model.getPersonalHome.RootPersonalHome;
import com.fanwe.user.model.getUserChangeMobile.ModelUserChangeMobile;
import com.fanwe.user.model.getUserRedpackets.ResultUserRedPacket;
import com.fanwe.user.model.getUserRedpackets.RootUserRedPacket;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;
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
    public void getMyDistributionCorps(String type, String rank, int pageNum, int pageSize,String user_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("type", type);
        params.put("rank", rank);
        params.put("user_id", user_id);
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("method", UserConstants.MY_DISTRIBUTION_CROPS);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootMyDistributionCorps root = gson.fromJson(responseBody, RootMyDistributionCorps.class);
                List<ResultMyDistributionCorps> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    mView.onSuccess(UserConstants.MY_DISTRIBUTION_CROPS, null);
                    return;
                }
                mView.onSuccess(UserConstants.MY_DISTRIBUTION_CROPS, result);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });
    }

    /**
     * 修改手机号
     *
     * @param mobile 需要修改成的最终手机号
     */
    public void getUserChangeMobile(final String mobile) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("mobile", mobile);
        params.put("method", UserConstants.USER_CHANGE_MOBILE);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {

            }

            @Override
            public void onSuccessResponse(String responseBody) {
                final ModelUserChangeMobile modelUserChangeMobile = gson.fromJson(responseBody,
                        ModelUserChangeMobile.class);
                if (modelUserChangeMobile != null) {
                    String statusCode = modelUserChangeMobile.getStatusCode();
                    if ("315".equals(statusCode)) {
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.onFailue("此手机号被占用!");
                            }
                        });
                    } else if ("200".equals(statusCode)) {
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.onSuccess(modelUserChangeMobile.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取红包列表,用户红包列表页面专用
     */
    public void getUserRedPackets() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.USER_RED_PACKET_LIST);
//        params.put("page","1");
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                MGLog.e("红包列表" + responseBody);
                RootUserRedPacket rootUserRedPacket = gson.fromJson(responseBody,
                        RootUserRedPacket.class);

                if (rootUserRedPacket != null) {
                    final List<ResultUserRedPacket> result = rootUserRedPacket.getResult();
                    if (result != null && result.size() > 0) {
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.onSuccess(UserConstants.USER_RED_PACKET_LIST, result);
                            }
                        });
                    }
                } else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onFailue("没有请求到数据!");
                        }
                    });
                }

            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFinish(UserConstants.USER_RED_PACKET_LIST);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        mView = null;
        gson = null;
    }
}
