package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.model.CitylistModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiHomeFragmentCategory;
import com.miguo.category.fragment.HomeBannerFragmentCategory;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeFragment extends HiBaseFragment{

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hihome, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiHomeFragmentCategory(cacheView, this);
    }

    public void updateFromCityChanged(CitylistModel model){
        if(null != getCategory()){
            getCategory().updateFromCityChanged(model);
        }
    }

    public void onRefreshGreeting(){
        if(null != getCategory()) {
            getCategory().onRefreshGreeting();
        }
    }

    public HiHomeFragmentCategory getCategory(){
        return (HiHomeFragmentCategory)category;
    }

}
