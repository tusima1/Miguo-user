package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiLiveListFragmentCategory;
import com.miguo.entity.HiFunnyTabBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */

public class HiLiveListFragement extends HiBaseFragment{

    HiLiveListFragmentCategory.OnPagerInitListener onPagerInitListener;

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hilivelist, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiLiveListFragmentCategory(cacheView, this);
    }

    public HiLiveListFragmentCategory.OnPagerInitListener getOnPagerInitListener() {
        return onPagerInitListener;
    }

    public void initFragments(List<HiFunnyTabBean.Result.Body> tabs){
        if(null != getCategory()){
            getCategory().initLiveList(tabs);
        }
    }

    @Override
    public HiLiveListFragmentCategory getCategory() {
        return (HiLiveListFragmentCategory)super.getCategory();
    }

    public void setOnPagerInitListener(HiLiveListFragmentCategory.OnPagerInitListener onPagerInitListener) {
        this.onPagerInitListener = onPagerInitListener;
    }
}
