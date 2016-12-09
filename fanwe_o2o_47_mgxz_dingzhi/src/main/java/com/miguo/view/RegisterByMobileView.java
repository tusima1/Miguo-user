package com.miguo.view;

import com.fanwe.user.model.UserInfoNew;

/**
 * Created by zlh on 2016/12/3.
 */

public interface RegisterByMobileView extends BaseView {
    void registerByMobileSuccess(UserInfoNew user);
    void registerByMobileError(String message);
}
