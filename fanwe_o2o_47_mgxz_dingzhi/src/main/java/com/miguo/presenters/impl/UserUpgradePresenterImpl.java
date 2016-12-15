package com.miguo.presenters.impl;
import android.text.TextUtils;

import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.wxapp.SDWxappPay;
import com.miguo.dao.UserUpgradeOrderDao;
import com.miguo.dao.impl.UserUpgradeOrderDaoImpl;
import com.miguo.entity.UserUpgradeOrderBean;
import com.miguo.entity.UserUpgradeOrderBean2;
import com.miguo.presenters.UserUpgradePresenter;
import com.miguo.view.BaseView;
import com.miguo.view.UserUpgradeOrderView;
import com.miguo.view.UserUpgradeOrderView2;
import com.miguo.view.UserUpgradePresenterView;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by zlh on 2016/12/14.
 * 获取用户升级信息
 * 用户升级（全额支付、预扣费支付、微信支付、支付宝支付）
 */
public class UserUpgradePresenterImpl extends BasePresenterImpl implements UserUpgradePresenter, UserUpgradeOrderView, UserUpgradeOrderView2{

    /**
     * 获取用户升级信息
     */
    UserUpgradeOrderDao userUpgradeOrderDao;

    public UserUpgradePresenterImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    protected void initModels() {
        userUpgradeOrderDao = new UserUpgradeOrderDaoImpl(this);
    }

    @Override
    public void getUserUpgradeInfo() {
        userUpgradeOrderDao.getUserUpgradeInfo();
    }

    /**
     * 全额支付
     */
    @Override
    public void userUpgradeByAccount() {
        /**
         * {@link com.miguo.dao.impl.UserUpgradeOrderDaoImpl}
         * {@link #userUpgradeByAccountSuccess()}
         * {@link #userUpgradeError(String)}
         */
        userUpgradeOrderDao.userUpgradeByAccount();
    }

    /**
     * 预扣费
     */
    @Override
    public void userUpgradeByWithholding() {
        /**
         * {@link com.miguo.dao.impl.UserUpgradeOrderDaoImpl}
         * {@link #userUpgradeByWithholdingSuccess()}
         * {@link #userUpgradeError(String)}
         */
        userUpgradeOrderDao.userUpgradeByWithholding();
    }

    /**
     * 微信支付（先获取config信息，再调支付）
     */
    @Override
    public void userUpgradeByWechat() {
        /**
         * {@link com.miguo.dao.impl.UserUpgradeOrderDaoImpl}
         * {@link #userUpgradeByWechatSuccess(UserUpgradeOrderBean2.Result.Body.Config)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradeOrderDao.userUpgradeByWechat();
    }

    /**
     * 支付宝支付（先获取config信息，再调支付）
     */
    @Override
    public void userUpgradeByAlipay() {
        /**
         * {@link com.miguo.dao.impl.UserUpgradeOrderDaoImpl}
         * {@link #userUpgradeByAlipaySuccess(UserUpgradeOrderBean2.Result.Body.Config)}
         * {@link #userUpgradeError(String)}
         */
        userUpgradeOrderDao.userUpgradeByAlipay();
    }

    /** 获取用户升级信息回调 / 各种支付方式支付回调 **/
    /**
     * 获取用户升级信息失败
     * @param message
     */
    @Override
    public void getUserUpgradeInfoError(String message) {
        getListener().userUpgradeError(message);
    }

    /**
     * 获取用户升级信息成功
     * @param body
     */
    @Override
    public void getUserUpgradeInfoSuccess(UserUpgradeOrderBean.Result.Body body) {
        //暂时单独用UserUpgradeOrderDao调用
    }

    /**
     * 全额支付升级成功
     */
    @Override
    public void userUpgradeByAccountSuccess() {
        getListener().userUpgradeSuccess("支付成功！");
    }

    /**
     * 预扣费升级成功
     */
    @Override
    public void userUpgradeByWithholdingSuccess() {
        getListener().userUpgradeSuccess("预扣费支付成功！");
    }

    /**
     * 微信获取预支付信息成功
     * @param config
     */
    @Override
    public void userUpgradeByWechatSuccess(UserUpgradeOrderBean2.Result.Body.Config config) {
        String appId = config.getAppid();
        if (TextUtils.isEmpty(appId)) {
            getListener().userUpgradeError("appId为空");
            return;
        }

        String partnerId = config.getMch_id();
        if (TextUtils.isEmpty(partnerId)) {
            getListener().userUpgradeError("partnerId为空");
            return;
        }

        String prepayId = config.getPrepay_id();
        if (TextUtils.isEmpty(prepayId)) {
            getListener().userUpgradeError("prepayId为空");
            return;
        }

        String nonceStr = config.getNonce_str();
        if (TextUtils.isEmpty(nonceStr)) {
            getListener().userUpgradeError("nonceStr为空");
            return;
        }

        String timeStamp = config.getTime_stamp();
        if (TextUtils.isEmpty(timeStamp)) {
            getListener().userUpgradeError("timeStamp为空");
            return;
        }

        String packageValue = config.getPackage_value();
        if (TextUtils.isEmpty(packageValue)) {
            getListener().userUpgradeError("packageValue为空");
            return;
        }

        String sign = config.getSign();
        if (TextUtils.isEmpty(sign)) {
            getListener().userUpgradeError("sign为空");
            return;
        }

        SDWxappPay.getInstance().setAppId(appId);

        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = partnerId;
        req.prepayId = prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = timeStamp;
        req.packageValue = packageValue;
        req.sign = sign;

        SDWxappPay.getInstance().pay(req);
    }

    /**
     * 支付宝获取预支付信息成功
     * @param config
     */
    @Override
    public void userUpgradeByAlipaySuccess(UserUpgradeOrderBean2.Result.Body.Config config) {
        if(null == getActivity()){
            throw new RuntimeException("BaseView must extends com.miguo.category.Category");
        }
        if (config == null) {
            showToast("获取支付宝支付参数失败");
            return;
        }

        String orderSpec = config.getTextHtml();

        String sign = config.getSign();

        String signType = config.getSign_type();

        if (TextUtils.isEmpty(orderSpec)) {
            getListener().userUpgradeError("order_spec为空");
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            getListener().userUpgradeError("sign为空");
            return;
        }

        if (TextUtils.isEmpty(signType)) {
            getListener().userUpgradeError("signType为空");
            return;
        }

        com.fanwe.library.alipay.easy.SDAlipayer payer = new com.fanwe.library.alipay.easy.SDAlipayer(getActivity());
        payer.setmListener(new com.fanwe.library.alipay.easy.SDAlipayer.SDAlipayerListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(PayResult result) {
                String info = result.getMemo();
                String status = result.getResultStatus();
                /**
                 * 支付成功
                 */
                if ("9000".equals(status)){
                    getListener().userUpgradeSuccess("支付成功");
                    return;
                }
                /**
                 * 支付结果确认中
                 */
                if ("8000".equals(status)){
                    getListener().userUpgradeSuccess("支付结果确认中");
                    return;
                }
                /**
                 * 返回信息
                 */
                getListener().userUpgradeError(info);
            }

            @Override
            public void onFailure(Exception e, String msg) {
                if (e != null) {
                    getListener().userUpgradeError("错误:" + e.toString());
                    return;
                }
                if (!TextUtils.isEmpty(msg)) {
                    getListener().userUpgradeError(msg);
                }
            }
        });
        payer.pay(orderSpec, sign, signType);
    }

    /**
     * 用户升级失败/获取预支付信息失败！
     * @param message
     */
    @Override
    public void userUpgradeError(String message) {
        getListener().userUpgradeError(message);
    }
    /** 获取用户升级信息回调 / 各种支付方式支付回调 **/

    @Override
    public UserUpgradePresenterView getListener() {
        return (UserUpgradePresenterView)baseView;
    }
}
