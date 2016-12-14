package com.miguo.dao.impl;

import com.fanwe.app.App;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.miguo.dao.UserUpgradeOrderDao;
import com.miguo.definition.PaymentId;
import com.miguo.entity.UserUpgradeOrderBean;
import com.miguo.entity.UserUpgradeOrderBean2;
import com.miguo.view.BaseView;
import com.miguo.view.UserUpgradeOrderView;
import com.miguo.view.UserUpgradeOrderView2;

import java.util.TreeMap;

/**
 * Created by zlh on 2016/12/14.
 */

public class UserUpgradeOrderDaoImpl extends BaseDaoImpl implements UserUpgradeOrderDao {

    public UserUpgradeOrderDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getUserUpgradeInfo() {
        TreeMap<String , String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("method", "UserUpgradeOrder");
        OkHttpUtils.getInstance().get("", params, new MgCallback(UserUpgradeOrderBean.class) {
            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                UserUpgradeOrderBean bean = (UserUpgradeOrderBean)responseBody;
                if(null == bean){
                    getListener().getUserUpgradeInfoError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getUserUpgradeInfoError(bean.getMessage());
                    return;
                }
                if(isEmpty(bean.getResult())  || isEmpty(bean.getResult().get(0)) || isEmpty(bean.getResult().get(0)) ||isEmpty(bean.getResult().get(0).getBody())|| isEmpty( bean.getResult().get(0).getBody().get(0)) ){
                    getListener().getUserUpgradeInfoError(BASE_ERROR_MESSAGE);
                    return;
                }
                /**
                 * 成功
                 */
                getListener().getUserUpgradeInfoSuccess(bean.getResult().get(0).getBody().get(0));

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getUserUpgradeInfoError(message);
            }
        });
    }

    private void userUpdategrade(final String payment_id){
        TreeMap<String , String> params = new TreeMap<>();
        params.put("token", App.getInstance().getToken());
        params.put("payment_id", payment_id);
        params.put("method", "UserUpgradeOrder");
        OkHttpUtils.getInstance().post("", params, new MgCallback(UserUpgradeOrderBean2.class) {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener2().userUpgradeError(message);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                UserUpgradeOrderBean2 bean = (UserUpgradeOrderBean2)responseBody;
                if(null == bean){
                    getListener2().userUpgradeError(BASE_ERROR_MESSAGE);
                    return;
                }

                if(bean.getStatusCode() != 200){
                    getListener2().userUpgradeError(bean.getMessage());
                    return;
                }

                if(isEmpty(bean.getResult())  || isEmpty(bean.getResult().get(0)) || isEmpty(bean.getResult().get(0)) ||isEmpty(bean.getResult().get(0).getBody()) || isEmpty( bean.getResult().get(0).getBody().get(0))){
                    getListener2().userUpgradeError(BASE_ERROR_MESSAGE);
                    return;
                }

                /**
                 * 支付成功/获取支付信息成功
                 */
                handleUpgradeSuccess(payment_id, bean.getResult().get(0).getBody().get(0));


            }
        });
    }

    /**
     * 根据payment_id判断回调成功
     * @param payment_id 支付方式
     * @param config 如果是第三方支付，需要config信息
     */
    private void handleUpgradeSuccess(String payment_id, UserUpgradeOrderBean2.Result.Body config){
        switch (payment_id){
            case PaymentId.ACCOUNT:
                if(config.getOrder_info().getOrder_status() >= 3){
                    getListener2().userUpgradeByAccountSuccess();
                    return;
                }
                getListener2().userUpgradeError("升级失败！");
                break;
            case PaymentId.WITHHOLDING:
                if(config.getOrder_info().getOrder_status() >= 3) {
                    getListener2().userUpgradeByWithholdingSuccess();
                }
                getListener2().userUpgradeError("升级失败！");
                break;
            case PaymentId.WECHAT:
                getListener2().userUpgradeByWechatSuccess(config.getConfig());
                break;
            case PaymentId.ALIPAY:
                getListener2().userUpgradeByAlipaySuccess(config.getConfig());
                break;
        }
    }

    /**
     *  全额支付
     */
    @Override
    public void userUpgradeByAccount() {
        userUpdategrade(PaymentId.ACCOUNT);
    }

    /**
     * 预扣费支付
     */
    @Override
    public void userUpgradeByWithholding() {
        userUpdategrade(PaymentId.WITHHOLDING);
    }

    /**
     * 微信支付
     */
    @Override
    public void userUpgradeByWechat() {
        userUpdategrade(PaymentId.WECHAT);
    }

    /**
     * 支付宝支付
     */
    @Override
    public void userUpgradeByAlipay() {
        userUpdategrade(PaymentId.ALIPAY);
    }

    /**
     * 升级信息回调
     * @return
     */
    public UserUpgradeOrderView2 getListener2(){
        return (UserUpgradeOrderView2)baseView;
    }

    /**
     * 获取升级信息回调
     * @return
     */
    @Override
    public UserUpgradeOrderView getListener() {
        return (UserUpgradeOrderView)baseView;
    }
}
