package com.miguo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.miguo.entity.HiFunnyTabBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */

public class HiLiveListFragmentViewPagerAdapter extends FragmentStatePagerAdapter{

    ArrayList<Fragment> fragments;
    List<HiFunnyTabBean.Result.Body> tabs;

    public HiLiveListFragmentViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTabs().get(position).getTitle();
    }

    public List<HiFunnyTabBean.Result.Body> getTabs() {
        return tabs;
    }

    public void setTabs(List<HiFunnyTabBean.Result.Body> tabs) {
        this.tabs = tabs;
    }
}
