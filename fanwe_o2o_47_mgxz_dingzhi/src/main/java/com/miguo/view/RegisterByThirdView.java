package com.miguo.view;

import com.fanwe.user.model.UserInfoNew;

/**
 * Created by zlh on 2016/12/5.
 */
public interface RegisterByThirdView extends BaseView {

    void registerByThirdSuccess(UserInfoNew user);
    void registerByThirdError(String message);

}
