package com.miguo.category.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.o2o.miguo.R;
import com.fanwe.view.LoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiRepresentBannerAdapter;
import com.miguo.adapter.HiRepresentBannerFragmentAdapter;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiRepresentBannerFragment;
import com.miguo.ui.view.RepresentViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentBannerFragmentCategory extends FragmentCategory {

    LoadMoreRecyclerView recyclerView;
    HiRepresentBannerAdapter adapter;

    public HiRepresentBannerFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        adapter = new HiRepresentBannerAdapter(getActivity(), new ArrayList());
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
        initRecyclerView();
        initRecyclerViewParams();
        onRefresh();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(adapter);
    }

    private void onRefresh(){
        adapter.notifyDataSetChanged(getFragment().getCategories());
    }

    private void initRecyclerViewParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getScreenWidth() - adapter.getPaddingSpace(), ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(params);
    }

    public HiRepresentBannerFragment getFragment(){
        return (HiRepresentBannerFragment)fragment;
    }

}
