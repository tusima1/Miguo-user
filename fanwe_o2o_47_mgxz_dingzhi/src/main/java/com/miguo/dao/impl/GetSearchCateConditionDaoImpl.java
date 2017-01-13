package com.miguo.dao.impl;

import com.alibaba.fastjson.JSON;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.dao.GetSearchCateConditionDao;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.view.BaseView;
import com.miguo.view.GetSearchCateConditionView;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by zlh on 2017/1/9.
 */

public class GetSearchCateConditionDaoImpl extends BaseDaoImpl implements GetSearchCateConditionDao{

    public GetSearchCateConditionDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public GetSearchCateConditionView getListener() {
        return (GetSearchCateConditionView)baseView;
    }

    @Override
    public void getSearchCateCondition() {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("method", "GetSearchCateCondition");
        params.put("city_id", "69e0405b-de8c-4247-8a0a-91ca45c4b30c");
        OkHttpUtils.getInstance().get("", params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                SearchCateConditionBean bean = JSON.parseObject(responseBody, SearchCateConditionBean.class);
                if(null == bean || SDCollectionUtil.isEmpty(bean.getResult()) || SDCollectionUtil.isEmpty(bean.getResult().get(0).getBody())){
                    getListener().getSearchCateConditionError(BASE_ERROR_MESSAGE);
                    return;
                }

                getListener().getSearchCateConditionSuccess(bean.getResult().get(0).getBody().get(0));
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getSearchCateConditionError(BASE_ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().getSearchCateConditionError(BASE_ERROR_MESSAGE);
            }

        });

    }
}
