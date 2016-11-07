package com.miguo.dao.impl;

import com.miguo.dao.IMLoginDao;
import com.miguo.view.BaseView;
import com.miguo.view.IMLoginView;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * IM登录接口实现类
 */
public class IMLoginDaoImpl extends BaseDaoImpl implements IMLoginDao{

    public IMLoginDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public IMLoginView getListener() {
        return (IMLoginView)baseView;
    }

    @Override
    public void imLogin(String identify, String userSig) {
        MySelfInfo.getInstance().setId(identify);
        MySelfInfo.getInstance().setUserSig(userSig);
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constants.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constants.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constants.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        getListener().imLoginError(s);
                    }

                    @Override
                    public void onSuccess() {
                        getListener().imLoginSuccess();
                    }
                });
    }
}
