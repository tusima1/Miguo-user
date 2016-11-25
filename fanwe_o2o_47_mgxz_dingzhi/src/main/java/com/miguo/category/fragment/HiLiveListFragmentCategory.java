package com.miguo.category.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiLiveListFragmentViewPagerAdapter;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiLiveListFragement;
import com.miguo.fragment.HiSingleLiveListFragment;
import com.miguo.ui.view.HiLiveListFragmentViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */

public class HiLiveListFragmentCategory extends FragmentCategory{

    @ViewInject(R.id.live_list_viewpager)
    HiLiveListFragmentViewPager viewPager;

    HiLiveListFragmentViewPagerAdapter pagerAdapter;

    ArrayList<Fragment> fragments;

    public HiLiveListFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {

    }

    @Override
    protected void setFragmentListener() {

    }

    @Override
    protected void init() {
    }

    public void initLiveList(List<HiFunnyTabBean.Result.Body> tabs){
        fragments = new ArrayList<>();
        for(int i = 0; i<tabs.size(); i++){
            HiSingleLiveListFragment hiLiveListFragement = new HiSingleLiveListFragment();
            hiLiveListFragement.setTab_id(tabs.get(i).getTab_id());
            fragments.add(hiLiveListFragement);
            if(i == 0){
                hiLiveListFragement.setFirstItem(true);
            }
        }
        pagerAdapter = new HiLiveListFragmentViewPagerAdapter(fragment.getChildFragmentManager(), fragments);
        pagerAdapter.setTabs(tabs);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(7);
        if(getFragment().getOnPagerInitListener() != null){
            getFragment().getOnPagerInitListener().pagerInit(viewPager, fragments.size());
        }
    }

    public interface OnPagerInitListener{
        void pagerInit(ViewPager viewPager, int number);
    }

    public HiLiveListFragement getFragment(){
        return (HiLiveListFragement)fragment;
    }

}
