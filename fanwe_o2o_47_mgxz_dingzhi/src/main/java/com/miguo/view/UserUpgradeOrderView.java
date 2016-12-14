package com.miguo.view;

import com.miguo.entity.UserUpgradeOrderBean;

/**
 * Created by zlh on 2016/12/14.
 */

public interface UserUpgradeOrderView extends BaseView {

    void getUserUpgradeInfoSuccess(UserUpgradeOrderBean.Result.Body body);
    void getUserUpgradeInfoError(String message);

}
