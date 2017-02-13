package com.miguo.dao.impl;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.user.UserConstants;
import com.miguo.dao.GetSMSCodeDao;
import com.miguo.definition.SMSType;
import com.miguo.view.BaseView;
import com.miguo.view.GetSMSCodeView;

import java.util.TreeMap;

/**
 * Created by zlh on 2016/12/2.
 */

public class GetSMSCodeDaoImpl extends BaseDaoImpl implements GetSMSCodeDao{

    public GetSMSCodeDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getSMSCode(String mobile, int type) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("mobile", mobile);
        params.put("type", type + "");
        params.put("method", UserConstants.SEND_CAPTCHA);
        OkHttpUtil.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                getListener().getSMSCodeSuccess("验证码发送成功！");
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getSMSCodeError("验证码发送失败，请重新发送！");
            }
        });
    }

    @Override
    public void getSMSCodeForRegister(String mobile) {
        getSMSCode(mobile, SMSType.REGISTER);
    }

    @Override
    public void getSMSCodeForModifyPassword(String mobile) {
        getSMSCode(mobile, SMSType.MODIFY_PASSWORD);
    }

    @Override
    public void getSMSCodeForWithDraw(String mobile) {
        getSMSCode(mobile, SMSType.WITHDRAW);
    }

    @Override
    public void getSMSCodeForQuickLoginAndBindMobile(String mobile) {
        getSMSCode(mobile, SMSType.QUICK_LOGIN_AND_BIND_MOBILE);
    }

    @Override
    public GetSMSCodeView getListener() {
        return (GetSMSCodeView)baseView;
    }
}
