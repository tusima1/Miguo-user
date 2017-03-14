package com.miguo.dao.impl;

import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.user.UserConstants;
import com.google.gson.Gson;
import com.miguo.dao.CreateShareRecordDao;
import com.miguo.entity.CreateShareRecordBean;
import com.miguo.view.BaseView;
import com.miguo.view.CreateShareRecordView;

import java.util.TreeMap;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/13.
 */

public class CreateShareRecordDaoImpl extends BaseDaoImpl implements CreateShareRecordDao {

    public CreateShareRecordDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public void createShareRecord(String content_type, String share_target, String lgn_user_id, String generate_episode, String content_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("content_type", content_type);
        params.put("share_target", "0");
        params.put("lgn_user_id", lgn_user_id);
        params.put("generate_episode", generate_episode);
        params.put("content_id", content_id);
        params.put("method", UserConstants.CREATE_SHARE_RECORD);

        OkHttpUtil.getInstance().post(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                CreateShareRecordBean bean = new Gson().fromJson(responseBody, CreateShareRecordBean.class);
                if(isEmpty(bean)){
                    getListener().createShareRecordError(BASE_ERROR_MESSAGE);
                    return;
                }
                if(bean.getStatusCode() != 200 && bean.getStatusCode()!=370){
                    getListener().createShareRecordError(bean.getMessage());
                    return;
                }

                if(bean.getStatusCode() == 200){
                    try{
                        getListener().createShareRecordSuccess(bean.getResult().get(0).getBody().getId());
                        return;
                    }catch (Exception e){
                        getListener().createShareRecordError(BASE_ERROR_MESSAGE);
                        return;
                    }
                }

                getListener().createShareRecordError(bean.getMessage());

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().createShareRecordError(BASE_ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void createShareRecordFromMultiSalePay(String lgn_user_id) {
        createShareRecord("10", "0", lgn_user_id, "1", "");
    }

    @Override
    public void createShareRecordFromOfflinePay(String lgn_user_id, String content_id) {
        createShareRecord("2", "0", lgn_user_id, "1", content_id);
    }

    @Override
    public void createShareRecordFromSingleSalePay(String lgn_user_id, String content_id) {
        createShareRecord("1", "0", lgn_user_id, "1", content_id);
    }

    @Override
    public CreateShareRecordView getListener() {
        return (CreateShareRecordView)baseView;
    }
}
