package com.miguo.live.views.view.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.customview.tab.ExtTabLayout;
import com.fanwe.o2o.miguo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideLiveFragment extends BaseFragment {
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ExtTabLayout tabLayout;

    private ArrayList<String> tags=new ArrayList<>();
    private FragmentManager fm;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_live;
    }

    @Override
    protected void initView(View content) {
        viewPager = (ViewPager) content.findViewById(R.id.viewpager);
        tabLayout = (ExtTabLayout) content.findViewById(R.id.tab);
    }

    @Override
    protected void startFlow() {
        bindDataView();
    }

    @Override
    protected boolean getBundleData(Bundle args) {
        if (args ==null)return false;
        try {
            tags = (ArrayList<String>) args.getSerializable("tags");
        } catch (Exception e) {
            Log.e("test",e.toString());
        }
        return !(tags == null || tags.size() <=0 );
    }

    private void bindDataView() {
        fm = getActivity().getSupportFragmentManager();
        pagerAdapter = new SimpleFragmentPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(ExtTabLayout.MODE_SCROLLABLE);
    }


    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private HashMap<Integer,GuidePagerFragment> pagerMap=new HashMap<>();

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            GuidePagerFragment target = pagerMap.get(position);
            if (target == null){
                target=GuidePagerFragment.newInstance
                        (tags.get(position));
                pagerMap.put(position,target);
                Log.e("test","new fragment :" +position);
            }
            target=pagerMap.get(position);
            return target;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,  position);
            fm.beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            fm.beginTransaction().hide(pagerMap.get(position)).commit();
        }

        @Override
        public int getCount() {
            return tags.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tags.get(position);
        }
    }

}
