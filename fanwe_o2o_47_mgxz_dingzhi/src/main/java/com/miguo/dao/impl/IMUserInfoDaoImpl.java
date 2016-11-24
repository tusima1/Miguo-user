package com.miguo.dao.impl;

import com.miguo.dao.IMUserInfoDao;
import com.miguo.view.BaseView;
import com.miguo.view.IMUserInfoView;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * IM登录成功后绑定用户信息到腾讯
 */
public class IMUserInfoDaoImpl extends BaseDaoImpl implements IMUserInfoDao{

    public IMUserInfoDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public IMUserInfoView getListener() {
        return (IMUserInfoView)baseView;
    }

    @Override
    public void updateTencentNickName(String nickname) {
        if(null == nickname || "".equals(nickname)){
            return;
        }

        TIMFriendshipManager.getInstance().setNickName(nickname, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }

    @Override
    public void updateTencentAvatar(String avatar) {
        if(null == avatar || "".equals(avatar)){
            return;
        }
        TIMFriendshipManager.getInstance().setFaceUrl(avatar, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }
}
