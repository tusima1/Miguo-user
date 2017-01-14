package com.miguo.view;

import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.seller.model.getBusinessListings.ResultBusinessListings;

import java.util.List;

/**
 * Created by zlh on 2017/1/13.
 */

public interface GetShopFromParamsView extends BaseView {

    void getShopFromParamsSuccess(List<ModelBusinessListings> results);
    void getShopFromParamsLoadMoreSuccess(List<ModelBusinessListings> results);
    void getShopFromParamsError(String message);

}
