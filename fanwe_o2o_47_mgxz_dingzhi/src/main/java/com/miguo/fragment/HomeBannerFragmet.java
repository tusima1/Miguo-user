package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HomeBannerFragmentCategory;
import com.miguo.entity.AdspaceListBean;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/17.
 */
public class HomeBannerFragmet extends HiBaseFragment{

    AdspaceListBean.Result.Body banner;

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hihome_fragment_banner, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        setBanner((AdspaceListBean.Result.Body) getArguments().getSerializable("image"));
        category = new HomeBannerFragmentCategory(cacheView,this);
    }

    public AdspaceListBean.Result.Body getBanner() {
        return banner;
    }

    public void setBanner(AdspaceListBean.Result.Body banner) {
        this.banner = banner;
    }
}
