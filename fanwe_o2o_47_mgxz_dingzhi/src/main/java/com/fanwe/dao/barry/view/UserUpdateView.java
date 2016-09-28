package com.fanwe.dao.barry.view;

import com.fanwe.model.UserUpdateInfoBean;

/**
 * Created by Administrator on 2016/9/28.
 */
public interface UserUpdateView {
    void getUserUpdateInfoSuccess(UserUpdateInfoBean.Result.Body info);
    void getUserUpdateInfoError(String msg);
}
