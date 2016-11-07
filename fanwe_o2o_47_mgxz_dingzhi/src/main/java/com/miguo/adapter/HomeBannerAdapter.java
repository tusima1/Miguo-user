package com.miguo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zlh/狗蛋哥/Barry on 16/8/18.
 * 首页banner
 */
public class HomeBannerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;
    int itemCount = 0;
    FragmentManager fm;


    public HomeBannerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void notifyDataSetChanged(ArrayList<Fragment> adds) {
        for(int i = 0; i<fragments.size(); i++){
            fm.beginTransaction().remove(fragments.get(i)).commit();
        }
        this.fragments = adds;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        if(itemCount > 0){
            itemCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
