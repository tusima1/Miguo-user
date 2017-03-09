package com.miguo.listener.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.MyMessageActivity;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiHomeFragmentCategory;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.MenuBean;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;
import com.miguo.ui.view.HomeADView2;
import com.miguo.ui.view.HomeTagsView;
import com.miguo.ui.view.floatdropdown.SearchGuideActivity;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/20.
 */
public class HiHomeFragmentListener extends FragmentListener implements HomeADView2.OnTopicAdsClickListener, HomeTagsView.OnHomeTagsClickListener{

    public HiHomeFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.frag_home_title_bar_ll_saoyisao:
                clickQrScan();
                break;
            case R.id.frag_home_title_bar_ll_msg:
                clickMessage();
                break;
            case R.id.frag_home_title_bar_ll_earn:
                clickEarn();
                break;
            case R.id.frag_home_title_bar_ll_search:
                clickSearch();
                break;
            case R.id.city_sayhi:
                clickCity();
                break;
            case R.id.refresh:
                clickRefresh();
                break;
            case R.id.nodata:
                clickNodata();
                break;
        }
    }

    private void clickNodata(){
        if(null == getCategory().getCitySign() || TextUtils.isEmpty(getCategory().getCitySign().getCity_image_link())){
        return;
        }
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.WEB_PAGE_ACTIVITY));
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.HOME_BANNER_WEB_PAGE, getCategory().getCitySign().getCity_image_link());
        bundle.putString(IntentKey.HOME_WEB_PAGE_TITLE, getCategory().getCitySign().getCity_image_title());
        intent.putExtras(bundle);
        BaseUtils.jumpToNewActivityForResult(getActivity(), intent, RequestCode.HOME_WEB_PAGE);
    }

    private void clickRefresh(){
        getCategory().clickRefresh();
    }

    /**
     * 问候语上的城市
     */
    private void clickCity(){
        clickEarn();
    }

    /**
     * 点击搜索
     */
    private void clickSearch() {
        Intent intent = new Intent(getActivity(), SearchGuideActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    /**
     * 点击区域
     */
    private void clickEarn() {
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.CITY_LIST_ACTIVITY));
        BaseUtils.jumpToNewActivityForResult(getActivity(), intent, RequestCode.RESUTN_CITY_ID);
    }

    /**
     * 点击扫码
     */
    private void clickQrScan(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.SCAN_QR_CODE));
        BaseUtils.jumpToNewActivityForResult(getActivity(), intent, 12);
    }

    /**
     * 点击消息中心
     */
    private void clickMessage(){
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.MESSAGE));
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public void onTopicAdsClick(AdspaceListBean.Result.Body ad) {
        getCategory().onTopicAdsClick(ad);
    }

    @Override
    public void onTagsClick(MenuBean.Result.Body item) {
        getCategory().onTagsClick(item);
    }

    @Override
    public HiHomeFragmentCategory getCategory() {
        return (HiHomeFragmentCategory) super.getCategory();
    }
}
