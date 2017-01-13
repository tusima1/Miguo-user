package com.miguo.dao.impl;

import com.alibaba.fastjson.JSON;
import com.fanwe.app.App;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getBusinessListings.ResultBusinessListings;
import com.fanwe.seller.model.getBusinessListings.RootBusinessListings;
import com.google.gson.Gson;
import com.miguo.dao.GetShopFromParamsDao;
import com.miguo.entity.RepresentFilterBean;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.view.BaseView;
import com.miguo.view.GetShopFromParamsView;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by zlh on 2017/1/13.
 */

public class GetShopFromParamsDaoImpl extends BaseDaoImpl implements GetShopFromParamsDao{

    public GetShopFromParamsDaoImpl(BaseView baseView) {
        super(baseView);
    }

    public void getShop(RepresentFilterBean filter){
        getShop(filter.getAreaOne(), filter.getAreaTwo(), filter.getCategoryOne(), filter.getCategoryTwo(), filter.getFilter(), filter.getKeyword(), filter.getSortType(), filter.getPageNum(), filter.getPageSize(), filter.getMerchantType());
    }

    @Override
    public void getShop(String area_one, String area_two, String category_one, String category_two, String filter, String keyword, String sort_type, int pageNum, int pageSize, String merchant_type) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("area_one", area_one);
        params.put("area_two", area_two);
        params.put("category_one", category_one);
        params.put("category_two", category_two);
        params.put("filter", filter);
        params.put("sort_type", sort_type);
        params.put("keyword", keyword);
        params.put("merchant_type", merchant_type);
        params.put("page_size", String.valueOf(pageSize));
        params.put("page", String.valueOf(pageNum));
        params.put("method", SellerConstants.SHOP_SEARCH);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootBusinessListings root = new Gson().fromJson(responseBody, RootBusinessListings.class);
                List<ResultBusinessListings> results = root.getResult();
                if(null == root || SDCollectionUtil.isEmpty(root.getResult())){
                    getListener().getShopFromParamsError(BASE_ERROR_MESSAGE);
                    return;
                }

                getListener().getShopFromParamsSuccess(root.getResult());
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                getListener().getShopFromParamsError(BASE_ERROR_MESSAGE);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getListener().getShopFromParamsError(BASE_ERROR_MESSAGE);
            }
        });
    }

    @Override
    public GetShopFromParamsView getListener() {
        return (GetShopFromParamsView)baseView;
    }
}
