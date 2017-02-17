package com.miguo.listener.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;


import com.miguo.app.HiBaseActivity;
import com.miguo.category.dialog.DialogFragmentCategory;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by mac on 16/3/23.
 */
public abstract class DialogFragmentListener implements
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        AbsListView.OnScrollListener, PtrHandler,
        TextWatcher
{


    protected DialogFragmentCategory category;


    public DialogFragmentListener(DialogFragmentCategory category){
        this.category = category;
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
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

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

    public DialogFragmentCategory getCategory(){
        return category;
    }


    public void showToast(String msg){
        category.showToast(msg);
    }


    public HiBaseActivity getActivity(){
        return category.getActivity();
    }


}
