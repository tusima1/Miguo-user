package com.miguo.listener.fragment;

import android.content.Intent;
import android.view.View;

import com.fanwe.CityListActivity;
import com.fanwe.HomeSearchActivity;
import com.fanwe.MyMessageActivity;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiHomeFragmentCategory;
import com.miguo.definition.ClassPath;
import com.miguo.definition.RequestCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.views.utils.BaseUtils;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/20.
 */
public class HiHomeFragmentListener extends FragmentListener{

    public HiHomeFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
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
        }
    }

    private void clickCity(){
        clickEarn();
    }

    /**
     * 点击搜索
     */
    private void clickSearch() {
        Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    /**
     * 点击区域
     */
    private void clickEarn() {
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.CITY_LIST_ACTIVITY));
        BaseUtils.jumpToNewActivityForResult(getActivity(), intent, RequestCode.RESUTN_CITY_ID);
    }

    private void clickMessage(){
        Intent intent = new Intent(getActivity(), MyMessageActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public HiHomeFragmentCategory getCategory() {
        return (HiHomeFragmentCategory) super.getCategory();
    }
}
