package com.miguo.presenters.impl;

import android.text.TextUtils;

import com.fanwe.library.alipay.easy.PayResult;
import com.fanwe.wxapp.SDWxappPay;
import com.miguo.dao.OnlinePayOrderPaymentDao;
import com.miguo.dao.impl.OnlinePayOrderPaymentDaoImpl;
import com.miguo.entity.OnlinePayOrderPaymentBean;
import com.miguo.entity.UserUpgradeOrderBean2;
import com.miguo.presenters.OnlinePayOrderPaymentPresenter;
import com.miguo.view.BaseView;
import com.miguo.view.OnlinePayOrderPaymentPresenterView;
import com.miguo.view.OnlinePayOrderPaymentView;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class OnlinePayOrderPaymentPresenterImpl extends BasePresenterImpl implements OnlinePayOrderPaymentPresenter {

    OnlinePayOrderPaymentDao onlinePayOrderPaymentDao;

    public OnlinePayOrderPaymentPresenterImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void alipay(String orderId, int useAmount) {
        onlinePayOrderPaymentDao.alipay(orderId, useAmount);
    }

    @Override
    public void wechat(String orderId, int useAmount) {
        onlinePayOrderPaymentDao.wechat(orderId, useAmount);
    }

    @Override
    public void amount(String orderId) {
        onlinePayOrderPaymentDao.amount(orderId);
    }

    @Override
    protected void initModels() {
        initOnlinePayOrderPaymentDao();
    }

    private void initOnlinePayOrderPaymentDao(){
        onlinePayOrderPaymentDao = new OnlinePayOrderPaymentDaoImpl(new OnlinePayOrderPaymentView() {
            @Override
            public void amountPaySuccess(OnlinePayOrderPaymentBean.Result.Body body) {
                getListener().paySuccess(body);
            }

            @Override
            public void wechatPaySuccess(OnlinePayOrderPaymentBean.Result.Body body) {
                handleWechatPaySuccess(body);
            }

            @Override
            public void alipaySuccess(OnlinePayOrderPaymentBean.Result.Body body) {
                handleAlipaySuccess(body);
            }

            @Override
            public void payError(String message) {
                getListener().payError(message);
            }
        });
    }

    private void handleWechatPaySuccess(OnlinePayOrderPaymentBean.Result.Body body){
        UserUpgradeOrderBean2.Result.Body.Config config = body.getConfig();
        String appId = config.getAppid();
        if (TextUtils.isEmpty(appId)) {
            getListener().payError("appId为空");
            return;
        }

        String partnerId = config.getMch_id();
        if (TextUtils.isEmpty(partnerId)) {
            getListener().payError("partnerId为空");
            return;
        }

        String prepayId = config.getPrepay_id();
        if (TextUtils.isEmpty(prepayId)) {
            getListener().payError("prepayId为空");
            return;
        }

        String nonceStr = config.getNonce_str();
        if (TextUtils.isEmpty(nonceStr)) {
            getListener().payError("nonceStr为空");
            return;
        }

        String timeStamp = config.getTime_stamp();
        if (TextUtils.isEmpty(timeStamp)) {
            getListener().payError("timeStamp为空");
            return;
        }

        String packageValue = config.getPackage_value();
        if (TextUtils.isEmpty(packageValue)) {
            getListener().payError("packageValue为空");
            return;
        }

        String sign = config.getSign();
        if (TextUtils.isEmpty(sign)) {
            getListener().payError("sign为空");
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

    private void handleAlipaySuccess(OnlinePayOrderPaymentBean.Result.Body body){
        UserUpgradeOrderBean2.Result.Body.Config config = body.getConfig();
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
            getListener().payError("order_spec为空");
            return;
        }

        if (TextUtils.isEmpty(sign)) {
            getListener().payError("sign为空");
            return;
        }

        if (TextUtils.isEmpty(signType)) {
            getListener().payError("signType为空");
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
                    getListener().payError("支付成功");
                    return;
                }
                /**
                 * 支付结果确认中
                 */
                if ("8000".equals(status)){
                    getListener().payError("支付结果确认中");
                    return;
                }
                /**
                 * 返回信息
                 */
                getListener().payError(info);
            }

            @Override
            public void onFailure(Exception e, String msg) {
                if (e != null) {
                    getListener().payError("错误:" + e.toString());
                    return;
                }
                if (!TextUtils.isEmpty(msg)) {
                    getListener().payError(msg);
                }
            }
        });
        payer.pay(orderSpec, sign, signType);
    }

    @Override
    public OnlinePayOrderPaymentPresenterView getListener() {
        return (OnlinePayOrderPaymentPresenterView)baseView;
    }
}
