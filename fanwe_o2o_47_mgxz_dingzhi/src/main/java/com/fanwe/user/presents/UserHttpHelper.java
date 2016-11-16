package com.fanwe.user.presents;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.OldCallbackHelper;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.seller.model.postShopComment.RootShopComment;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.getAttentionFans.ResultFans;
import com.fanwe.user.model.getAttentionFans.RootFans;
import com.fanwe.user.model.getAttentionFocus.ModelAttentionFocus;
import com.fanwe.user.model.getAttentionFocus.ResultAttentionFocus;
import com.fanwe.user.model.getAttentionFocus.RootAttentionFocus;
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
import com.fanwe.user.model.getPersonHomePage.ModelPersonHomePage;
import com.fanwe.user.model.getPersonHomePage.ResultPersonHomePage;
import com.fanwe.user.model.getPersonHomePage.RootPersonHomePage;
import com.fanwe.user.model.getPersonalHome.ModelPersonalHome;
import com.fanwe.user.model.getPersonalHome.ResultPersonalHome;
import com.fanwe.user.model.getPersonalHome.RootPersonalHome;
import com.fanwe.user.model.getProductList.ModelProductList;
import com.fanwe.user.model.getProductList.ResultProductList;
import com.fanwe.user.model.getProductList.RootProductList;
import com.fanwe.user.model.getShopAndUserCollect.ModelShopAndUserCollect;
import com.fanwe.user.model.getShopAndUserCollect.ResultShopAndUserCollect;
import com.fanwe.user.model.getShopAndUserCollect.RootShopAndUserCollect;
import com.fanwe.user.model.getUserAttention.ModelUserAttention;
import com.fanwe.user.model.getUserAttention.ResultUserAttention;
import com.fanwe.user.model.getUserAttention.RootUserAttention;
import com.fanwe.user.model.getUserChangeMobile.ModelUserChangeMobile;
import com.fanwe.user.model.getUserRedpackets.ResultUserRedPacket;
import com.fanwe.user.model.getUserRedpackets.RootUserRedPacket;
import com.fanwe.user.model.getUserUpgradeOrder.ModelGetUserUpgradeOrder;
import com.fanwe.user.model.getUserUpgradeOrder.ResultGetUserUpgradeOrder;
import com.fanwe.user.model.getUserUpgradeOrder.RootGetUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.ModelPostUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.ResultPostUserUpgradeOrder;
import com.fanwe.user.model.postUserUpgradeOrder.RootPostUserUpgradeOrder;
import com.fanwe.user.model.putAttention.ModelAttention;
import com.fanwe.user.model.putAttention.ResultAttention;
import com.fanwe.user.model.putAttention.RootAttention;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.getLiveListNew.ModelResultLive;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.model.getLiveListNew.ModelRootLive;
import com.miguo.live.model.getWallet.ModelMyWallet;
import com.miguo.live.model.getWallet.ResultMyWallet;
import com.miguo.live.model.getWallet.RootMyWallet;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/6.
 */
public class UserHttpHelper extends OldCallbackHelper implements IHelper {

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
                onSuccess(mView, UserConstants.USER_INFO_METHOD, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onFinish() {
                mView.onFinish(UserConstants.USER_INFO_METHOD);
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
                                    onSuccess(mView, UserConstants.PERSONALHOME, body);
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
                MGToast.showToast(message);
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
    public void getMyDistributionCorps(String type, String rank, int pageNum, int pageSize,
                                       String user_id) {
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
                RootMyDistributionCorps root = gson.fromJson(responseBody,
                        RootMyDistributionCorps.class);
                List<ResultMyDistributionCorps> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.MY_DISTRIBUTION_CROPS, null);
                    return;
                }
                onSuccess(mView, UserConstants.MY_DISTRIBUTION_CROPS, result);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
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
                                onSuccess(mView, modelUserChangeMobile.getMessage());
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
                                onSuccess(mView, UserConstants.USER_RED_PACKET_LIST, result);
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
                        if (mView != null) {
                            mView.onFinish(UserConstants.USER_RED_PACKET_LIST);
                        }
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
                RootGroupCoupon rootGroupCoupon = gson.fromJson(responseBody, RootGroupCoupon
                        .class);
                final List<ResultGroupCoupon> result = rootGroupCoupon.getResult();
                if (result != null && result.size() > 0) {
                    onSuccess(mView,UserConstants.GROUP_BUY_COUPON_LIST, result);
                }else {
                    onFailure2(mView,UserConstants.GROUP_BUY_COUPON_LIST);
                }

            }

            @Override
            public void onFinish() {
                onFinish2(mView,UserConstants.GROUP_BUY_COUPON_LIST);
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
                if (mView == null) {
                    return;
                }
                RootDistrInfo root = gson.fromJson(responseBody, RootDistrInfo.class);
                List<ResultDistrInfo> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(mView, UserConstants.DISTR_INFO, null);
                        }
                    });
                    return;
                }
                final List<ModelDistrInfo> items = result.get(0).getBody();
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(mView, UserConstants.DISTR_INFO, items);
                    }
                });
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
            }

            @Override
            public void onFinish() {
                if (mView != null) {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onFinish(UserConstants.DISTR_INFO);
                        }
                    });
                }
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
                                onSuccess(mView, UserConstants.QR_SHOP_CARD, body);
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
                RootGetUserUpgradeOrder root = gson.fromJson(responseBody,
                        RootGetUserUpgradeOrder.class);
                List<ResultGetUserUpgradeOrder> results = root.getResult();
                if (SDCollectionUtil.isEmpty(results)) {
                    onSuccess(mView, UserConstants.USER_UPGRADE_ORDER_GET, null);
                    return;
                }
                List<ModelGetUserUpgradeOrder> items = results.get(0).getBody();
                onSuccess(mView, UserConstants.USER_UPGRADE_ORDER_GET, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
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
                RootPostUserUpgradeOrder root = gson.fromJson(responseBody,
                        RootPostUserUpgradeOrder.class);
                List<ResultPostUserUpgradeOrder> results = root.getResult();
                if (SDCollectionUtil.isEmpty(results)) {
                    onSuccess(mView, UserConstants.USER_UPGRADE_ORDER_POST, null);
                    return;
                }
                List<ModelPostUserUpgradeOrder> items = results.get(0).getBody();
                onSuccess(mView, UserConstants.USER_UPGRADE_ORDER_POST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
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
                RootShopComment root = gson.fromJson(responseBody, RootShopComment.class);
                if (!"212".equals(root.getStatusCode())) {
                    //错误
                    List<RootShopComment> roots = new ArrayList<>();
                    roots.add(root);
                    onSuccess(mView, UserConstants.USER_CHANGE_PWD, roots);
                    return;
                }
                onSuccess(mView, UserConstants.USER_CHANGE_PWD, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });

    }

    /**
     * 用户忘记密码
     *
     * @param mobile
     * @param pwd
     * @param captcha
     */
    public void userForget(String mobile, String pwd, String captcha) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", mobile);
        params.put("pwd", pwd);
        params.put("captcha", captcha);
        params.put("method", UserConstants.USER_FORGOT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootShopComment root = gson.fromJson(responseBody, RootShopComment.class);
                if (!"212".equals(root.getStatusCode())) {
                    //错误
                    List<RootShopComment> roots = new ArrayList<>();
                    roots.add(root);
                    onSuccess(mView, UserConstants.USER_FORGOT, roots);
                    return;
                }
                onSuccess(mView, UserConstants.USER_FORGOT, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });

    }


    /**
     * 添加用户的建议
     *
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
                onSuccess(mView, UserConstants.ADVICE, null);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onFinish() {
                if (mView == null) {
                    return;
                }
                mView.onFinish(UserConstants.ADVICE);
            }
        });

    }

    /**
     * 获取粉丝页面
     *
     * @param page     1
     * @param pageSize 10
     */
    public void getAttentionFans(int page, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("page", page + "");
        params.put("page_size", pageSize + "");
        params.put("method", UserConstants.ATTENTION_Fans);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                final List<ResultFans> result = gson.fromJson(responseBody, RootFans.class)
                        .getResult();
                if (result != null && result.size() > 0) {
                    final ResultFans resultFans = result.get(0);
                    if (resultFans != null) {
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess(mView, UserConstants.ATTENTION_Fans, resultFans.getBody());
                            }
                        });
                        return;
                    }
                }
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFailue(UserConstants.ATTENTION_Fans);
                    }
                });

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFinish(UserConstants.ATTENTION_Fans);
                    }
                });
            }
        });

    }

    /**
     * 获取用户关注的人的一览接口
     *
     * @param pageNum
     * @param pageSize
     */
    public void getAttentionFocus(int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("method", UserConstants.ATTENTION_FOCUS);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootAttentionFocus root = gson.fromJson(responseBody, RootAttentionFocus.class);
                List<ResultAttentionFocus> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.ATTENTION_FOCUS, null);
                    return;
                }
                List<ModelAttentionFocus> items = result.get(0).getBody();
                onSuccess(mView, UserConstants.ATTENTION_FOCUS, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onFinish() {
                mView.onFinish(UserConstants.ATTENTION_FOCUS);
            }
        });
    }

    /**
     * 我的收藏
     *
     * @param pageNum
     * @param pageSize
     */
    public void getShopAndUserCollect(int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("method", UserConstants.SHOP_AND_USER_COLLECT);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootShopAndUserCollect root = gson.fromJson(responseBody, RootShopAndUserCollect.class);
                List<ResultShopAndUserCollect> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.SHOP_AND_USER_COLLECT, null);
                    return;
                }
                List<ModelShopAndUserCollect> items = result.get(0).getBody();
                onSuccess(mView, UserConstants.SHOP_AND_USER_COLLECT, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onFinish() {
                mView.onFinish(UserConstants.SHOP_AND_USER_COLLECT);
            }
        });
    }

    /**
     * 粉丝页面关注
     *
     * @param focus_user_id    必须	 关注，取关对象的id(user_id)
     * @param attention_status 必须 如果要进行关注操作，传1，取消关注传非1即可
     */
    public void putAttention(String focus_user_id, String attention_status) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("focus_user_id", focus_user_id);
        params.put("attention_status", attention_status);
        params.put("method", UserConstants.ATTENTION);

        OkHttpUtils.getInstance().put(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                List<ResultAttention> result = gson.fromJson(responseBody, RootAttention.class)
                        .getResult();
                if (result != null && result.size() > 0) {
                    ResultAttention resultAttention = result.get(0);
                    if (resultAttention != null) {
                        final List<ModelAttention> body = resultAttention.getBody();
                        if (body != null && body.size() > 0) {
                            MGUIUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onSuccess(mView, UserConstants.ATTENTION, body);
                                }
                            });
                        }
                        return;
                    }
                }
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFailue(UserConstants.ATTENTION);
                    }
                });
            }
        });
    }

    /**
     * 获取钱包页面数据
     */
    public void getMyWallet() {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.MY_WALLET);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                ResultMyWallet resultMyWallet = gson.fromJson(responseBody, RootMyWallet.class)
                        .getResult().get(0);
                if (resultMyWallet != null) {
                    final List<ModelMyWallet> body = resultMyWallet.getBody();
                    if (body != null && body.size() > 0) {
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess(mView, UserConstants.MY_WALLET, body);
                            }
                        });
                        return;
                    }
                }
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFailue(UserConstants.MY_WALLET);
                    }
                });
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onFinish(UserConstants.MY_WALLET);
                    }
                });
            }
        });

    }

    /**
     * 查询用户主页
     *
     * @param user_id
     */
    public void getPersonHomePage(String user_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("user_id", user_id);
        params.put("method", UserConstants.PERSON_HOME_PAGE);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootPersonHomePage root = gson.fromJson(responseBody, RootPersonHomePage.class);
                List<ResultPersonHomePage> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.PERSON_HOME_PAGE, null);
                    return;
                }
                List<ModelPersonHomePage> items = result.get(0).getBody();
                onSuccess(mView, UserConstants.PERSON_HOME_PAGE, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获得TA的最爱
     *
     * @param user_id
     */
    public void getProductList(String user_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("user_id", user_id);
        params.put("method", UserConstants.GET_PRODUCT_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootProductList root = gson.fromJson(responseBody, RootProductList.class);
                List<ResultProductList> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.GET_PRODUCT_LIST, null);
                    return;
                }
                List<ModelProductList> items = result.get(0).getBody();
                onSuccess(mView, UserConstants.GET_PRODUCT_LIST, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获得网红的直播场次
     *
     * @param user_id
     */
    public void getSpokePlay(String user_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("user_id", user_id);
        params.put("method", UserConstants.GET_SPOKE_PLAY);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                ModelRootLive root = gson.fromJson(responseBody, ModelRootLive.class);
                List<ModelResultLive> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.GET_SPOKE_PLAY, null);
                    return;
                }
                List<ModelRoom> items = result.get(0).getBody();
                onSuccess(mView, UserConstants.GET_SPOKE_PLAY, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    /**
     * 获得关注状态
     *
     * @param user_id
     */
    public void getUserAttention(String user_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", getToken());
        params.put("user_id", user_id);
        params.put("method", UserConstants.USER_ATTENTION);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootUserAttention root = gson.fromJson(responseBody, RootUserAttention.class);
                List<ResultUserAttention> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    onSuccess(mView, UserConstants.USER_ATTENTION, null);
                    return;
                }
                List<ModelUserAttention> items = result.get(0).getBody();
                onSuccess(mView, UserConstants.USER_ATTENTION, items);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        mView = null;
        gson = null;
    }
}
