package com.miguo.dao.impl;

import android.text.TextUtils;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtil;
import com.google.gson.Gson;
import com.miguo.dao.FeaturedGrouponDao;
import com.miguo.definition.GrouponConstants;
import com.miguo.entity.BodyFeaturedGroupBuy;
import com.miguo.entity.ModelFeaturedGroupBuy;
import com.miguo.entity.ResultFeaturedGroupBuy;
import com.miguo.entity.RootFeaturedGroupBuy;
import com.miguo.view.BaseView;
import com.miguo.view.FeaturedGrouponView;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 * 首页精选推荐列表接口实现类
 */
public class FeaturedGrouponDaoImpl extends BaseDaoImpl implements FeaturedGrouponDao {

    public FeaturedGrouponDaoImpl(BaseView baseView) {
        super(baseView);
    }

    @Override
    public FeaturedGrouponView getListener() {
        return (FeaturedGrouponView)baseView;
    }

    @Override
    public void getFeaturedGroupBuy(final String httpUuid, String cityId, final String pageNum, String pageSize, String keyword, String m_longitude, String m_latitude) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("city_id", cityId);
        params.put("page", pageNum);
        params.put("page_size", pageSize);
        params.put("keyword", keyword);
        params.put("m_longitude", m_longitude);
        params.put("m_latitude", m_latitude);
        params.put("method", GrouponConstants.FEATURED_GROUP_BUG);

        OkHttpUtil.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                //200
                RootFeaturedGroupBuy root = new Gson().fromJson(responseBody, RootFeaturedGroupBuy.class);
                //过滤错误请求
                if (!"200".equals(root.getStatusCode())) {
                    if (!TextUtils.isEmpty(root.getMessage())) {
                        getListener().getFeaturedGrouponError(httpUuid, root.getMessage());
                    } else {
                        getListener().getFeaturedGrouponError(httpUuid, "getFeaturedGroupBuy error");
                    }
                    return;
                }
                List<ResultFeaturedGroupBuy> result = root.getResult();
                if (SDCollectionUtil.isEmpty(result)) {
                    getListener().getFeaturedGrouponError(httpUuid, "getFeaturedGroupBuy error");
                    return;
                }
                List<BodyFeaturedGroupBuy> body = result.get(0).getBody();
                if (SDCollectionUtil.isEmpty(body)) {
                    getListener().getFeaturedGrouponError(httpUuid, "getFeaturedGroupBuy error");
                    return;
                }
                List<ModelFeaturedGroupBuy> items = body.get(0).getTuan_list();
                if(pageNum.equals("1")){
                    getListener().getFeaturedGrouponSuccess(httpUuid,items);
                }else {
                    getListener().getFeaturedGrouponLoadmoreSuccess(httpUuid, items);
                }
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getFeaturedGrouponError(httpUuid, message);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().getFeaturedGrouponError(httpUuid, "");
            }
        });
    }
}
