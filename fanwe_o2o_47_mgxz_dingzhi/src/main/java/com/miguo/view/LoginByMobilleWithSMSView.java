package com.miguo.view;

import com.fanwe.user.model.UserInfoNew;

/**
 * Created by zlh on 2016/12/2.
 */
public interface LoginByMobilleWithSMSView extends BaseView{
    void loginByMobileWithSMSSuccess(UserInfoNew userInfoNew);
    void loginByMobileWithSMSError(String message);
}
