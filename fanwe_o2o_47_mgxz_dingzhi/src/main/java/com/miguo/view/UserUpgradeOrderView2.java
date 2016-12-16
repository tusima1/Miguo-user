package com.miguo.view;

import com.miguo.entity.UserUpgradeOrderBean;
import com.miguo.entity.UserUpgradeOrderBean2;

/**
 * Created by zlh on 2016/12/14.
 */

public interface UserUpgradeOrderView2 extends BaseView {

    void userUpgradeByAccountSuccess();

    /**
     * 预付费支付成功
     */
    void userUpgradeByWithholdingSuccess();

    /**
     * 微信支付成功
     */
    void userUpgradeByWechatSuccess(UserUpgradeOrderBean2.Result.Body.Config config);

    /**
     * 支付宝支付成功
     */
    void userUpgradeByAlipaySuccess(UserUpgradeOrderBean2.Result.Body.Config config);

    /**
     * 支付失败！
     * @param message
     */
    void userUpgradeError(String message);

}
