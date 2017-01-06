package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.ShopDetailItemFragmentCategory;
import com.miguo.entity.HiShopDetailBean;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/17.
 */
public class ShopDetailPagerItemFragmet extends HiBaseFragment{

    HiShopDetailBean.Result.ShopImage banner;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hishop_detail_banner, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        setBanner((HiShopDetailBean.Result.ShopImage) getArguments().getSerializable("images"));
        category = new ShopDetailItemFragmentCategory(cacheView,this);
    }

    public HiShopDetailBean.Result.ShopImage getBanner() {
        return banner;
    }

    public void setBanner(HiShopDetailBean.Result.ShopImage banner) {
        this.banner = banner;
    }
}
