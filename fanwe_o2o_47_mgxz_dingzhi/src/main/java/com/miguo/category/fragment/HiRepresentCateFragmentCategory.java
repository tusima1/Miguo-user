package com.miguo.category.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.o2o.miguo.R;
import com.fanwe.view.LoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiRepresentCateAdapter;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiRepresentCateFragment;

import java.util.ArrayList;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentCateFragmentCategory extends FragmentCategory {

    @ViewInject(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;
    HiRepresentCateAdapter adapter;

    public HiRepresentCateFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        adapter = new HiRepresentCateAdapter(getActivity(), new ArrayList());
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5));
        recyclerView.setAdapter(adapter);
    }

    private void onRefresh(){
        adapter.notifyDataSetChanged(getFragment().getCategories());
    }

    private void initRecyclerViewParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getScreenWidth() - adapter.getPaddingSpace(), ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(params);
    }

    public HiRepresentCateFragment getFragment(){
        return (HiRepresentCateFragment)fragment;
    }

}
