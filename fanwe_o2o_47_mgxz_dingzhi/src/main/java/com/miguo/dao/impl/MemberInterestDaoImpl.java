package com.miguo.dao.impl;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.UserConstants;
import com.miguo.dao.MemberInterestDao;
import com.miguo.entity.MemberInterestBean;
import com.miguo.entity.UserCheckExistBean;
import com.miguo.view.BaseView;
import com.miguo.view.MemberInterestView;

import java.util.TreeMap;

/**
 * Created by zlh on 2016/12/13.
 */

public class MemberInterestDaoImpl extends BaseDaoImpl implements MemberInterestDao {

    public MemberInterestDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void getMemberInterest(){
        getMemberInterest(-1);
    }

    @Override
    public void getMemberInterest(int level) {
        TreeMap<String, String> params = new TreeMap<>();
        if(level != -1){
            params.put("level", level + "");
        }
        params.put("method", UserConstants.MEMBERINTEREST);
        OkHttpUtils.getInstance().get(null, params, new MgCallback(MemberInterestBean.class) {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getMemberInterestError(message);
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                MemberInterestBean bean = (MemberInterestBean)responseBody;
                if(bean == null){
                    getListener().getMemberInterestError("");
                    return;
                }
                if(bean.getStatusCode() != 200){
                    getListener().getMemberInterestError("");
                    return;
                }
                if(null == bean.getResult() || null == bean.getResult().get(0) || null == bean.getResult().get(0).getBody()){
                    getListener().getMemberInterestError("");
                    return;
                }
                getListener().getMemberInterestSuccess(bean.getResult().get(0).getBody());
            }

        });
    }

    @Override
    public MemberInterestView getListener() {
        return (MemberInterestView)baseView;
    }
}
