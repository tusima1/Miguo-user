package com.fanwe.model;

import com.fanwe.seller.model.getCityList.ModelCityList;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public class ModelDaoCity {
    private List<ModelCityList> citylist;

    public List<ModelCityList> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<ModelCityList> citylist) {
        this.citylist = citylist;
    }
}
