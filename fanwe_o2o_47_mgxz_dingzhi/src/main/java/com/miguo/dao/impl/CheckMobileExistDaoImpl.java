package com.miguo.dao.impl;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.user.UserConstants;
import com.miguo.dao.CheckMobileExistDao;
import com.miguo.entity.UserCheckExistBean;
import com.miguo.view.BaseView;
import com.miguo.view.CheckMobileExistView;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/12/2.
 */

public class CheckMobileExistDaoImpl extends BaseDaoImpl implements CheckMobileExistDao{

    public CheckMobileExistDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void checkMobileExist(String mobile) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("mobile", mobile);
        params.put("method", UserConstants.USER_CHECK_EXIST);
        OkHttpUtil.getInstance().get(null, params, new MgCallback(UserCheckExistBean.class) {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().mobileDoesNotExist(message);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                UserCheckExistBean user = (UserCheckExistBean)responseBody;
                if(user.exist()){
                    getListener().mobileExist();
                    return;
                }
                getListener().mobileDoesNotExist(user.getMessage());
                super.onSuccessResponseWithBean(responseBody);
            }

        });
    }

    @Override
    public CheckMobileExistView getListener() {
        return (CheckMobileExistView)baseView;
    }
}
