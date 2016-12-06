package com.miguo.view;

import com.fanwe.model.User_infoModel;
import com.fanwe.user.model.ThirdLoginInfo;
import com.fanwe.user.model.UserInfoNew;

/**
 * Created by zlh on 2016/11/30.
 */

public interface LoginByThirdView extends BaseView{

    /**
     *
     * @param userInfoModel 数据库的用户model
     * @param userInfoNew 全局的用户model
     */
    void thirdLoginSuccess(User_infoModel userInfoModel, UserInfoNew userInfoNew);

    /**
     *
     * @param thirdLoginInfo
     */
    void thirdLoginUnRegister(ThirdLoginInfo thirdLoginInfo);
    void thirdLoginError(String message);

}
