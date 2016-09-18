package com.fanwe.user.presents;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.getDistrInfo.ModelDistrInfo;
import com.fanwe.user.model.getDistrInfo.ResultDistrInfo;
import com.fanwe.user.model.getDistrInfo.RootDistrInfo;
import com.fanwe.user.model.getGroupBuyCoupon.ResultGroupCoupon;
import com.fanwe.user.model.getGroupBuyCoupon.RootGroupCoupon;
import com.fanwe.user.model.getMyDistributionCorps.ResultMyDistributionCorps;
import com.fanwe.user.model.getMyDistributionCorps.RootMyDistributionCorps;
import com.fanwe.user.model.getNameCardQR.ModelNameCardQR;
import com.fanwe.user.model.getNameCardQR.ResultNameCardQR;
import com.fanwe.user.model.getNameCardQR.RootNameCardQR;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.model.getPersonalHome.ResultPersonalHome;
import com.fanwe.user.model.getPersonalHome.RootPersonalHome;
import com.fanwe.user.model.getUserChangeMobile.ModelUserChangeMobile;
import com.fanwe.user.model.getUserRedpackets.ResultUserRedPacket;
import com.fanwe.user.model.getUserRedpackets.RootUserRedPacket;
import com.fanwe.user.model.getUserUpgradeOrder.ModelGetUserUpgradeOrder;
import com.fanwe.user.model.getUserUpgradeOrder.ResultGetUserUpgradeOrder;
import com.fanwe.user.model.getUserUpgradeOrder.RootGetUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.ModelPostUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.ResultPostUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.RootPostUserUpgradeOrder;
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
    public void getMyDistributionCorps(String type, String rank, int pageNum, int pageSize, String user_id) {
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

    /**
     * 团购券列表
     *
     * @param tag      (1 即将过期 /2 未使用 /3 已失效) 不传默认全部
     * @param tuan_id  团购id
     * @param order_id 用于团购订单支付完成后的团购券列表
     */
    public void getGroupBuyCouponList(String tag, String tuan_id, String order_id, int page) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.GROUP_BUY_COUPON_LIST);
        params.put("page", page + "");
        if (!TextUtils.isEmpty(tag) && !"0".equals(tag)) {
            params.put("tag", tag);
        }
        if (!TextUtils.isEmpty(tuan_id)) {
            params.put("tuan_id", tuan_id);
        }
        if (!TextUtils.isEmpty(order_id)) {
            params.put("order_id", order_id);
        }
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
//                Log.e("test", "团购券 :" + responseBody);
                RootGroupCoupon rootGroupCoupon = gson.fromJson(responseBody, RootGroupCoupon
                        .class);
                final List<ResultGroupCoupon> result = rootGroupCoupon.getResult();
                if (result != null && result.size() > 0) {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onSuccess(UserConstants.GROUP_BUY_COUPON_LIST, result);
                        }
                    });
                }

            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFinish(UserConstants.GROUP_BUY_COUPON_LIST);
                    }
                });
            }
        });
    }

    /**
     * 我的分销小店基本信息
     */
    public void getDistrInfo() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", UserConstants.DISTR_INFO);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootDistrInfo root = gson.fromJson(responseBody, RootDistrInfo.class);
                List<ResultDistrInfo> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onSuccess(UserConstants.DISTR_INFO, null);
                        }
                    });
                    return;
                }
                final List<ModelDistrInfo> items = result.get(0).getBody();
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onSuccess(UserConstants.DISTR_INFO, items);
                    }
                });
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });
    }

    /**
     * 获取二维码名片
     */
    public void getMyShopNameCard() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.QR_SHOP_CARD);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                List<ResultNameCardQR> result = gson.fromJson(responseBody, RootNameCardQR.class)
                        .getResult();
                if (result != null && result.size() > 0) {
                    ResultNameCardQR resultNameCardQR = result.get(0);
                    if (resultNameCardQR != null) {
                        final List<ModelNameCardQR> body = resultNameCardQR.getBody();
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.onSuccess(UserConstants.QR_SHOP_CARD, body);
                            }
                        });
                    }
                } else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onFailue(UserConstants.QR_SHOP_CARD);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFinish(UserConstants.QR_SHOP_CARD);
                    }
                });
            }
        });
    }

    /**
     * 获取用户升级信息
     */
    public void getUserUpgradeOrder() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("method", UserConstants.USER_UPGRADE_ORDER);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGetUserUpgradeOrder root = gson.fromJson(responseBody, RootGetUserUpgradeOrder.class);
                List<ResultGetUserUpgradeOrder> results = root.getResult();
                if (SDCollectionUtil.isEmpty(results)) {
                    mView.onSuccess(UserConstants.USER_UPGRADE_ORDER_GET, null);
                    return;
                }
                List<ModelGetUserUpgradeOrder> items = results.get(0).getBody();
                mView.onSuccess(UserConstants.USER_UPGRADE_ORDER_GET, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });
    }

    /**
     * 提交用户升级信息
     */
    public void postUserUpgradeOrder(String payment_id, String order_id, int is_use_account_money) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("payment_id", payment_id);
        params.put("order_id", order_id);
        params.put("is_use_account_money", is_use_account_money + "");
        params.put("method", UserConstants.USER_UPGRADE_ORDER);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootPostUserUpgradeOrder root = gson.fromJson(responseBody, RootPostUserUpgradeOrder.class);
                List<ResultPostUserUpgradeOrder> results = root.getResult();
                if (SDCollectionUtil.isEmpty(results)) {
                    mView.onSuccess(UserConstants.USER_UPGRADE_ORDER_POST, null);
                    return;
                }
                List<ModelPostUserUpgradeOrder> items = results.get(0).getBody();
                mView.onSuccess(UserConstants.USER_UPGRADE_ORDER_POST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });
    }

    /**
     * 用户修改密码
     *
     * @param mobile
     * @param newpwd
     * @param captcha
     */
    public void userChangePwd(String mobile, String newpwd, String captcha) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("mobile", mobile);
        params.put("newpwd", newpwd);
        params.put("captcha", captcha);
        params.put("method", UserConstants.USER_CHANGE_PWD);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(UserConstants.USER_CHANGE_PWD, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 添加用户的建议
     * @param advice
     */
    public void advice(String advice) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("advice", advice);
        params.put("method", UserConstants.ADVICE);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                mView.onSuccess(UserConstants.ADVICE, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 获取粉丝页面
     * @param page 1
     * @param pageSize 10
     */
    public void getAttentionFans(int page,int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("page", page+"");
        params.put("page_size", pageSize+"");
        params.put("method", UserConstants.ATTENTION_Fans);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                MGLog.e("test:"+responseBody);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    @Override
    public void onDestroy() {
        mView = null;
        gson = null;
    }
}
