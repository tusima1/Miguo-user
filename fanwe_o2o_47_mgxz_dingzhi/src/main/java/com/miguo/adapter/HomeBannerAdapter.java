package com.miguo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by mac on 16/8/18.
 */
public class HomeBannerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public HomeBannerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
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

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        if(position == -1){
//            position = fragments.size() - 1;
//        }
//        if(position == fragments.size()){
//            position = 0;
//        }
//        return fragments.get(position);
//    }
}
