package com.miguo.live.views.view.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.ui.view.HiLiveListFragmentViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideLiveFragment extends BaseFragment {
    private SimpleFragmentPagerAdapter pagerAdapter;
    private HiLiveListFragmentViewPager viewPager;

    private List<HiFunnyTabBean.Result.Body> tags=new ArrayList<>();
    private FragmentManager fm;
    private OnGuideLivePagerInitListener mListener;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_live;
    }

    @Override
    protected void initView(View content) {
        viewPager = (HiLiveListFragmentViewPager) content.findViewById(R.id.viewpager);
    }

    @Override
    protected void startFlow() {
        //empty
    }

    public void setViewPagerTags(List<HiFunnyTabBean.Result.Body> tags){
        this.tags=tags;
        bindDataView();
    }

    private void bindDataView() {
        if (tags==null || tags.size()<1){
            Log.e("GuideLiveFragment","父类传来的tag为null");
            return;
        }
        fm = getActivity().getSupportFragmentManager();
        pagerAdapter = new SimpleFragmentPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        if (mListener!=null){
            mListener.onGuideLivePagerInit(viewPager,tags.size());
        }
    }


    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private HashMap<Integer,GuidePagerFragment> pagerMap=new HashMap<>();

        private SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            GuidePagerFragment target = pagerMap.get(position);
            if (target == null){
                target=GuidePagerFragment.newInstance
                        (tags.get(position).getTab_id());
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
            return tags.get(position).getTitle();
        }
    }

    public interface OnGuideLivePagerInitListener{
        void onGuideLivePagerInit(ViewPager viewPager, int number);
    }

    public void setOnGuideLivePagerInitListener(OnGuideLivePagerInitListener listener){
        this.mListener=listener;
    }

}
