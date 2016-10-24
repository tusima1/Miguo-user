package com.miguo.category;

import android.support.v4.app.Fragment;

import com.miguo.adapter.HomePagerAdapter;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.fragment.HiHomeFragment;
import com.miguo.listener.HiHomeListener;
import com.miguo.live.definition.TabId;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.HomeViewPager;

import java.util.ArrayList;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeCategory extends Category{


    @ViewInject(R.id.tab)
    BarryTab tab;

    @ViewInject(R.id.home_view_pager)
    HomeViewPager homeViewPager;

    HomePagerAdapter homePagerAdapter;

    ArrayList<Fragment> fragments;

    public HiHomeCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {

    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiHomeListener(this);
    }

    @Override
    protected void setThisListener() {
        tab.setOnTabClickListener((HiHomeListener)listener);
    }

    @Override
    protected void init() {
        initHomePagers();
    }

    @Override
    protected void initViews() {
        initTab();
    }

    /**
     * 初始化tab
     * author：zlh/Barry/狗蛋哥
     * create time:2016 10/13
     * modified time:null
     */
    private void initTab(){
        tab.
            /**
             * 首页
             * 名字 默认图标 按下后图标 tab id
             */
            addTab(getString(R.string.home), R.drawable.tab_home_normal, R.drawable.tab_home_pressed, TabId.TAB_A).
            addTab(getString(R.string.supplier), R.drawable.tab_seller_normal, R.drawable.tab_seller_pressed, TabId.TAB_B).
            addTab("我要直播", R.drawable.tab_live_normal, R.drawable.tab_live_pressed, TabId.TAB_C).
            addTab(getString(R.string.market), R.drawable.tab_market_normal, R.drawable.tab_market_pressed, TabId.TAB_D).
            addTab(getString(R.string.mine), R.drawable.tab_my_normal, R.drawable.tab_my_pressed, TabId.TAB_E).
            /**
             * 设置为默认模式（图标+文字形式）
             */
            setTabType(BarryTab.Type.NORMAL).
            /**
             * 设置文字大小
             */
            setTextSize(12).
            /**
             * 设置图标大小，单位：dp
             * 设置了width等于设置了height
             */
            setIconWidht(20).
            /**
             * 设置tab默认文字颜色
             */
            setNormalColor(R.color.text_home_menu_normal).
            /**
             * 设置tab选中时候的文字颜色
             */
            setPressColor(R.color.text_home_menu_selected).
            /**
             * 绑定ViewPager
             */
            setViewPager(homeViewPager).
            /**
             * 生产
             */
            builder();
    }

    private void initHomePagers(){
        fragments = new ArrayList<>();
        fragments.add(new HiHomeFragment());
//        fragments.add(new HomeFragment());
//        fragments.add(new StoreListContainerFragment());
//        fragments.add(new StoreListContainerFragment());
//        fragments.add(new MarketFragment());
//        fragments.add(new MyFragment2());

        homePagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), fragments);

        homeViewPager.setAdapter(homePagerAdapter);
        homeViewPager.setOffscreenPageLimit(5);
    }

    public void clickTab(int position){
        homeViewPager.setCurrentItem(position);
    }



}
