package com.miguo.listener.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.miguo.category.fragment.FragmentCategory;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by zlh/Barry/狗蛋哥 on 13/10/16.
 */
public abstract class FragmentListener implements
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        AbsListView.OnScrollListener, PtrHandler
{

    FragmentCategory category;

    String tag = this.getClass().getSimpleName();

    public FragmentListener(FragmentCategory category){
        this.category = category;
    }

    public FragmentCategory getCategory(){
        return category;
    }

    public Intent getIntent(Context context, Class<?> clz){
        return category.getIntent(context, clz);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {

    }
}
