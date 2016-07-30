package com.fanwe.seller.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.fanwe.seller.views.fragment.FragmentMineShopList;

import java.util.ArrayList;

/**
 * 店铺列表适配器
 * Created by Administrator on 2016/7/29.
 */
public class MineShopListPagerAdapter extends FragmentStatePagerAdapter {
    public final static int TYPE_MINE = 1;
    public final static int TYPE_FRIEND = 2;
    public final static int TYPE_RECOMMEND = 3;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public MineShopListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentMineShopList fragment = new FragmentMineShopList();
        Bundle bd = new Bundle();
        if (position == 0) {
            //我的代言
            bd.putInt("type", TYPE_MINE);
        } else if (position == 1) {
            //朋友推荐
            bd.putInt("type", TYPE_FRIEND);
        } else if (position == 2) {
            //精选
            bd.putInt("type", TYPE_RECOMMEND);
        }
        fragment.setArguments(bd);
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

}
