package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.HomeSearchActivity;
import com.fanwe.MainActivity;
import com.fanwe.MapSearchActivity;
import com.fanwe.constant.Constant.SearchTypeMap;
import com.fanwe.constant.Constant.SearchTypeNormal;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.o2o.miguo.R;
import com.sunday.eventbus.SDBaseEvent;

public class StoreListContainerFragment extends BaseFragment {

    private StoreListFragment mFragAll;
    private StoreListFragment mFragYouhui;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(TitleType.TITLE);
        return setContentView(R.layout.view_container);
    }

    @Override
    protected void init() {
        super.init();
        addFragments();
        initTitle();
    }

    private void addFragments() {
        Bundle bundle = getActivity().getIntent().getExtras();

        mFragAll = new StoreListFragment();
        mFragAll.setArguments(bundle);
        mFragAll.getArguments().putString(StoreListFragment.EXTRA_STORE_TYPE, "2");

        mFragYouhui = new StoreListFragment();
        mFragYouhui.setArguments(bundle);
        mFragYouhui.getArguments().putString(StoreListFragment.EXTRA_STORE_TYPE, "1");

    }

    private void initTitle() {

        if (getActivity() instanceof MainActivity) {
            mTitle.setLeftImageLeft(0);
        } else {
            mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
        }

        mTitle.setCustomViewMiddle(R.layout.view_tab_two);

        SDTabItemCorner tabYouhui = (SDTabItemCorner) mTitle.findViewById(R.id.tab_0);
        tabYouhui.setTabName("优惠商家");
        tabYouhui.setmPosition(EnumTabPosition.FIRST);
        tabYouhui.reverseDefaultConfig();

        SDTabItemCorner tabAll = (SDTabItemCorner) mTitle.findViewById(R.id.tab_1);
        tabAll.setTabName("全部商家");
        tabAll.setmPosition(EnumTabPosition.LAST);
        tabAll.reverseDefaultConfig();

        SDViewNavigatorManager navigatorManager = new SDViewNavigatorManager();
        navigatorManager.setItems(new SDViewBase[]{tabYouhui, tabAll});
        navigatorManager.setmListener(new SDViewNavigatorManagerListener() {

            @Override
            public void onItemClick(View v, int index) {
                switch (index) {
                    case 0:
                        getSDFragmentManager().toggle(R.id.view_container_fl_content, null, mFragYouhui);
                        break;
                    case 1:
                        getSDFragmentManager().toggle(R.id.view_container_fl_content, null, mFragAll);
                        break;

                    default:
                        break;
                }
            }
        });
        navigatorManager.setSelectIndex(0, tabAll, true);
        mTitle.initRightItem(2);
        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_location_home_top);
        mTitle.getItemRight(1).setImageLeft(R.drawable.ic_search_home_top);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        switch (index) {
            case 0:
                startNearbyMapSearchActivity();
                break;
            case 1:
                startHomeSearchActivity();
                break;

            default:
                break;
        }
    }

    private void startHomeSearchActivity() {
        Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(HomeSearchActivity.EXTRA_SEARCH_TYPE, SearchTypeNormal.MERCHANT);
        startActivity(intent);
    }

    private void startNearbyMapSearchActivity() {
        Intent intent = new Intent(getActivity(), MapSearchActivity.class);
        intent.putExtra(MapSearchActivity.EXTRA_SEARCH_TYPE, SearchTypeMap.STORE);
        startActivity(intent);
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CITY_CHANGE:
                initTitle();
                break;

            default:
                break;
        }
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
