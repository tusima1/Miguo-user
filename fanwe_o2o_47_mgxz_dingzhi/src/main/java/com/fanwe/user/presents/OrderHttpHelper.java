package com.fanwe.user.presents;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getOrderInfo.ResultOrderInfo;
import com.fanwe.user.model.getOrderInfo.RootOrderInfo;
import com.fanwe.user.model.getRefundPage.ResultRefundPage;
import com.fanwe.user.model.getRefundPage.RootRefundPage;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/8/23.
 */
public class OrderHttpHelper implements IHelper {

    private String methodName="";
    private Gson gson;
    private CallbackView2 mView2;

    public OrderHttpHelper(CallbackView2 view2) {
        this.mView2=view2;
        this.gson=new Gson();
    }

    /**
     * 订单列表
     * @param key keyword
     * @param page 0:全部; >0 指定页面
     */
    public void getOrderInfo(String key,int page){
        if (TextUtils.isEmpty(key)){
            MGToast.showToast("key is empty!");
            return;
        }
        switch (key){
            case "all":
                methodName=UserConstants.ORDER_INFO_ALL;
                break;
            case "pay_wait":
                methodName=UserConstants.ORDER_INFO_PAY_WAIT;
                break;
            case "use_wait":
                methodName=UserConstants.ORDER_INFO_USE_WAIT;
                break;
            case "comment_wait":
                methodName=UserConstants.ORDER_INFO_COMMENT_WAIT;
                break;
            case "refund":
                methodName=UserConstants.ORDER_INFO_REFUND;
                break;
            default:
                key="all";
                break;
        }
        Log.e("test","key:"+key);
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.ORDER_INFO);
        params.put("type", key);
//        if (page>0){
//            params.put("page", page+"");
//        }

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.e("test","订单列表: "+responseBody);
                RootOrderInfo rootOrderInfo = gson.fromJson(responseBody, RootOrderInfo.class);
                final List<ResultOrderInfo> result = rootOrderInfo.getResult();
                if (result!=null){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(methodName,result);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFinish(methodName);
                    }
                });
            }
        });
    }

    /**
     * 取消订单
     * @param deal_id 订单id
     */
    public void postCancelOrderOperator(String deal_id){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.ORDER_INFO_CANCEL_ORDER);
        params.put("order_id", deal_id);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = gson.fromJson(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                if ("200".endsWith(statusCode)){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(UserConstants.ORDER_INFO_CANCEL_ORDER,null);
                        }
                    });
                }else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onFailue(UserConstants.ORDER_INFO_CANCEL_ORDER);
                        }
                    });
                }
            }
        });
    }

    /**
     * 删除订单
     * @param deal_id 订单id
     */
    public void postDeleteOrderOperator(String deal_id){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.ORDER_INFO);
        params.put("order_id", deal_id);
        OkHttpUtils.getInstance().delete(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = gson.fromJson(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                if ("200".endsWith(statusCode)){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(UserConstants.ORDER_INFO,null);
                        }
                    });
                }else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onFailue(UserConstants.ORDER_INFO);
                        }
                    });
                }
            }
        });
    }

    /**
     * 退款页面的
     * @param detail_id
     * @param tuan_id
     */
    public void getOrderItemTuangou(String detail_id,String tuan_id){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.REFUND_APPLICATION_PAGE);
        params.put("id", detail_id);
        params.put("tuan_id", tuan_id);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.e("test","responseBody :"+responseBody);
                final List<ResultRefundPage> result = gson.fromJson(responseBody, RootRefundPage.class)
                        .getResult();
                if (result!=null && result.size()>0){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(UserConstants.REFUND_APPLICATION_PAGE,result);
                        }
                    });
                }else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onFailue(UserConstants.REFUND_APPLICATION_PAGE);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFinish(UserConstants.REFUND_APPLICATION_PAGE);
                    }
                });
            }
        });
    }

    /**
     * 退款申请提交
     * @param number 退款数量
     * @param order_id 订单id
     * @param tuan_id 团购id
     */
    public void postRefundApply(String number,String order_id,String tuan_id){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", UserConstants.REFUND_APPLICATION);
        params.put("id", order_id);
        params.put("tuan_id", tuan_id);
        params.put("number", number);
        OkHttpUtils.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                Root root = gson.fromJson(responseBody, Root.class);
                if ("200".endsWith(root.getStatusCode())){
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onSuccess(UserConstants.REFUND_APPLICATION,null);
                        }
                    });
                }else {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView2.onFailue(UserConstants.REFUND_APPLICATION);
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        mView2=null;
        gson=null;
        methodName=null;
    }
}
