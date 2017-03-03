package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiShopFragmentCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/1.
 * 我的小店
 */

public class HiShopFragment extends HiBaseFragment {

    boolean finish;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_hihome_myshop, container ,false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiShopFragmentCategory(cacheView, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(null != getCategory() && isVisibleToUser && !finish){
            finish = true;
            getCategory().update();
        }
    }

    @Override
    public HiShopFragmentCategory getCategory() {
        return (HiShopFragmentCategory)super.getCategory();
    }
}
