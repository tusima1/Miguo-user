package com.fanwe.seller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanwe.seller.views.fragment.FirstFragment;

import java.util.List;


public class SecondTypeAdapter extends FragmentPagerAdapter {

    private List<FirstFragment> fragmentList;




    public SecondTypeAdapter(FragmentManager fm, List<FirstFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }



    @Override
    public Fragment getItem(int position) {
        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position+"";
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}