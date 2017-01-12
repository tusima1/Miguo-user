package com.fanwe.common.presenters;

import android.content.Context;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.OldCallbackHelper;
import com.fanwe.base.Root;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.createShareRecord.ModelCreateShareRecord;
import com.fanwe.common.model.createShareRecord.ResultCreateShareRecord;
import com.fanwe.common.model.createShareRecord.RootCreateShareRecord;
import com.fanwe.common.model.getCrashUpToken.ResultCrashUpToken;
import com.fanwe.common.model.getCrashUpToken.RootCrashUpToken;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.common.model.getHomeClassifyList.ResultHomeClassifyList;
import com.fanwe.common.model.getHomeClassifyList.RootHomeClassifyList;
import com.fanwe.common.model.getUpgradeVersion.ModelVersion;
import com.fanwe.common.model.getUpgradeVersion.ResultVersion;
import com.fanwe.common.model.getUpgradeVersion.RootVersion;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.shoppingcart.ShoppingCartconstants;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CommonHttpHelper extends OldCallbackHelper implements IHelper {

    private static final String TAG = CommonHttpHelper.class.getSimpleName();
    private Gson gson;
    private UserInfoNew userCurrentInfo;
    private CallbackView mView;
    private Context mContext;
    private String token;

    public static final String RESULT_OK = "no_body_but_is_ok";

    public CommonHttpHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getCurrentUser();
    }

    public String getToken() {
        return userCurrentInfo.getToken();
    }

    /**
     * 请求首页分类图标
     */
    public void getHomeClassifyList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", CommonConstants.HOME_CLASSIFY_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootHomeClassifyList root = gson.fromJson(responseBody, RootHomeClassifyList.class);
                List<ResultHomeClassifyList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, CommonConstants.HOME_CLASSIFY_LIST, null);
                    return;
                }
                List<ModelHomeClassifyList> items = result.get(0).getBody();
                onSuccess(mView, CommonConstants.HOME_CLASSIFY_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }


    public void getInterestingString(String city_id) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("city_id", city_id);
        params.put("method", CommonConstants.INTERESTING);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if (ShoppingCartconstants.RESULT_OK.equals(statusCode)) {
                    List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map = (HashMap<String, String>) validateBody(root);
                    if (map != null) {
                        datas.add(map);
                    }
                    onSuccess(mView, CommonConstants.INTERESTING, datas);
                } else {
                    mView.onFailue(message);
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });

    }


    public void getUpgradeAPK() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("dev_type", "android");
        params.put("version", SDPackageUtil.getCurrentPackageInfo().versionName);
        params.put("method", CommonConstants.UPGRADE_VERSION);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootVersion rootVersion = gson.fromJson(responseBody, RootVersion.class);
                if (rootVersion != null) {
                    ResultVersion result = rootVersion.getResult().get(0);
                    if (result != null) {
                        final List<ModelVersion> body = result.getBody();
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess(mView, CommonConstants.UPGRADE_VERSION, body);
                            }
                        });
                        return;
                    }
                }
                mView.onFailue(CommonConstants.UPGRADE_VERSION);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGLog.e(CommonConstants.UPGRADE_VERSION + " :" + message + errorCode);
            }
        });
    }

    /**
     * 获取当前用户的会员 等级。
     */

    public void getDistributionLevel() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", CommonConstants.USERDISTRIBUTIIONLEVEL);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                if ("200".equals(status)) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) validateBody(root);
                    if (hashMap != null) {
                        List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
                        datas.add(hashMap);
                        onSuccess(mView, CommonConstants.USERDISTRIBUTIIONLEVEL, datas);
                    } else {
                        mView.onFailue(CommonConstants.USERDISTRIBUTIIONLEVEL);
                    }
                } else {
                    mView.onFailue(CommonConstants.USERDISTRIBUTIIONLEVEL);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGLog.e(CommonConstants.UPGRADE_VERSION + " :" + message + errorCode);
            }
        });
    }

    /**
     * 通过钻石领取码获取shareid
     *
     * @param code
     */
    public void getShareIdByCode(String code) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("receive_code", code);
        params.put("method", CommonConstants.GETSHAREID);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                Type type = new TypeToken<Root<HashMap<String, String>>>() {
                }.getType();
                Gson gson = new Gson();
                Root<HashMap<String, String>> root = gson.fromJson(responseBody, type);
                String status = root.getStatusCode();
                if ("200".equals(status)) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) validateBody(root);
                    if (hashMap != null) {
                        List<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
                        datas.add(hashMap);
                        onSuccess(mView, CommonConstants.GETSHAREID, datas);
                    } else {
                        onFailure2(mView, CommonConstants.GETSHAREID);
                    }
                } else {
                    onFailure2(mView, CommonConstants.GETSHAREID);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                onFailure2(mView, CommonConstants.GETSHAREID);
            }
        });
    }

    /**
     * 生成分享记录
     */
    public void createShareRecord(String content_type, String content_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("lgn_user_id", App.getInstance().getCurrentUser().getUser_id());
        params.put("content_type", content_type);
        params.put("content_id", content_id);
        params.put("method", CommonConstants.CREATE_SHARE_RECORD);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                try {
                    RootCreateShareRecord root = gson.fromJson(responseBody, RootCreateShareRecord.class);
                    List<ResultCreateShareRecord> result = root.getResult();
                    if (SDCollectionUtil.isEmpty(result) || !"200".equals(root.getStatusCode())) {
                        onSuccess(mView, CommonConstants.CREATE_SHARE_RECORD, null);
                        return;
                    }
                    List<ModelCreateShareRecord> items = new ArrayList<>();
                    items.add(result.get(0).getBody());
                    onSuccess(mView, CommonConstants.CREATE_SHARE_RECORD, items);
                } catch (Exception e) {
                    Log.e("json", e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
            }
        });
    }

    /**
     * crash log上传时uptoken
     */
    public void getCrashUpToken() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", CommonConstants.CRASH_UPTOKEN);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                try {
                    RootCrashUpToken root = gson.fromJson(responseBody, RootCrashUpToken.class);
                    List<ResultCrashUpToken> result = root.getResult();
                    if (SDCollectionUtil.isEmpty(result) || !"200".equals(root.getStatusCode())) {
                        onSuccess(mView, CommonConstants.CRASH_UPTOKEN, null);
                        return;
                    }
                    onSuccess(mView, CommonConstants.CRASH_UPTOKEN, result.get(0).getBody());
                } catch (Exception e) {
                    Log.e("json", e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
            }
        });
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mView = null;
        gson = null;
        userCurrentInfo = null;
        App.getInstance().code = "";
    }
}
