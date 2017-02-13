package com.miguo.dao.impl;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.miguo.dao.TencentSignDao;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.view.BaseView;
import com.miguo.view.TencentSignView;

import java.util.TreeMap;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * 腾讯获取签名实现类
 */
public class TencentSignDaoImpl extends BaseDaoImpl implements TencentSignDao{

    public TencentSignDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public TencentSignView getListener() {
        return (TencentSignView)baseView;
    }

    @Override
    public void getTencentSign(String token) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("method", LiveConstants.GENERATE_SIGN);

        OkHttpUtil.getInstance().get(null, params, new MgCallback(RootGenerateSign.class) {

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getTencentSignError();
            }

            @Override
            public void onSuccessResponseWithBean(Object responseBody) {
                RootGenerateSign signBean = (RootGenerateSign)responseBody;
                if(null == signBean || !signBean.getStatusCode().equals("200")){
                    getListener().getTencentSignError();
                    return;
                }

                if(null == signBean.getResult() || null == signBean.getResult().get(0)){
                    getListener().getTencentSignError();
                    return;
                }

                if(null == signBean.getResult().get(0).getBody() || null == signBean.getResult().get(0).getBody().get(0)){
                    getListener().getTencentSignError();
                    return;
                }

                getListener().getTencentSignSuccess(signBean.getResult().get(0).getBody().get(0));
            }
        });
    }
}
