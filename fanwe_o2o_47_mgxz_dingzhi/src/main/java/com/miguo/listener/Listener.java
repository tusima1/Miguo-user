package com.miguo.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiBaseActivity;
import com.miguo.category.Category;
import com.miguo.utils.BaseUtils;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by mac on 16/3/14.
 */
public abstract class Listener implements
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        AbsListView.OnScrollListener,
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener,
        View.OnTouchListener, PtrHandler
{

    protected String tag = this.getClass().getSimpleName();

    protected Category category;
    App app;
    String TAG = Listener.class.getSimpleName();
    HiBaseActivity activity;

    public Listener(HiBaseActivity activity){
        this.activity = activity;
    }

    public Listener(Category category){
        this.category = category;
        this.app = category.getApp();

    }

    public Intent getIntent(Context context, Class<?> clz){
        return category.getIntent(context, clz);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            BaseUtils.finishActivity(getActivity());
        }
        onClickThis(v);
    }

    protected void onClickThis(View v){

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

    public void showToast(String msg){
        category.showToast(msg);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {

    }

    public HiBaseActivity getActivity(){
        return category.getActivity();
    }

//    @Override
//    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        LogUtil.d(tag, "scrollX:" + " ,scrollY:" + scrollY+ " ,oldScrollX: " + oldScrollX + " ,oldScrollY:" + oldScrollY);
//    }

    public Category getCategory(){
        return category;
    }




}
