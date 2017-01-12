package com.fanwe.seller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanwe.seller.views.fragment.SecondTypeFragment;

import java.util.List;

/**
 * fragementPager adapter
 * Created by zhouhy on 2017/1/11.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<SecondTypeFragment> fragmentList;




    public MyPagerAdapter(FragmentManager fm, List<SecondTypeFragment> fragmentList) {
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
