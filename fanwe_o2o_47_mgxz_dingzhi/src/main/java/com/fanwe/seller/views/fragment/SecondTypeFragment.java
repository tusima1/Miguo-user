package com.fanwe.seller.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.SecondTypeAdapter;
import com.fanwe.seller.views.customize.DPViewPager;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * 二级分类fragment
 * Created by zhouhy on 2017/1/11.
 */

public class SecondTypeFragment extends Fragment {
    DPViewPager mViewPager;
    public List<FirstFragment> fragments;
    private CircleIndicator mIndicator;



    private int selectedChildFragment =0;
    public SecondTypeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.secondtype_frag, null);
        mViewPager = (DPViewPager) view.findViewById(R.id.secondtype_viewpager);
//        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(new SecondTypeAdapter(getChildFragmentManager(), fragments));
        mIndicator = (CircleIndicator) view.findViewById(R.id.indicator_circle);
        mIndicator.setViewPager(mViewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(fragments!=null&&fragments.size()<2){
            mIndicator.setVisibility(View.INVISIBLE);
        }else{
            mIndicator.setVisibility(View.VISIBLE);

        }
    }

    public List<FirstFragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<FirstFragment> fragments) {
        this.fragments = fragments;
    }
    public void setSelectedChildFragment(int selectedChildFragment) {
        this.selectedChildFragment = selectedChildFragment;
    }




}
