package com.miguo.category.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiFunnyFragmentAdapter;
import com.miguo.dao.HiFunnyTabDao;
import com.miguo.dao.impl.HiFunnyTabDaoImpl;
import com.miguo.definition.TabSet;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiLiveListFragement;
import com.miguo.listener.fragment.HiFunnyFragmentListener;
import com.miguo.live.views.view.frag.GuideLiveFragment;
import com.miguo.ui.view.FunnyViewPager;
import com.miguo.ui.view.SlidingTabLayout;
import com.miguo.view.HiFunnyTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/16.
 */

public class HiFunnyFragmentCategory extends FragmentCategory implements HiLiveListFragmentCategory.OnPagerInitListener, HiFunnyTabView, GuideLiveFragment.OnGuideLivePagerInitListener {

    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.live)
    TextView titleLive;

    @ViewInject(R.id.guide)
    TextView titleGuide;

    @ViewInject(R.id.coorddinatorlayout)
    CoordinatorLayout coordinatorLayout;

    @ViewInject(R.id.title2)
    LinearLayout title2;

    @ViewInject(R.id.live2)
    TextView live2;

    @ViewInject(R.id.guide2)
    TextView guide2;

    @ViewInject(R.id.tab_layout)
    SlidingTabLayout slidingTabLayout;

    @ViewInject(R.id.tab_layout2)
    SlidingTabLayout slidingTabLayoutGuide;

    @ViewInject(R.id.app_bar)
    AppBarLayout appBarLayout;

    @ViewInject(R.id.funny_viewpager)
    FunnyViewPager funnyViewPager;
    HiFunnyFragmentAdapter funnyFragmentAdapter;
    ArrayList<Fragment> fragments;

    HiFunnyTabDao hiFunnyTabDao;

    public HiFunnyFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        hiFunnyTabDao = new HiFunnyTabDaoImpl(this);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiFunnyFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        live2.setOnClickListener(listener);
        titleLive.setOnClickListener(listener);
        titleGuide.setOnClickListener(listener);
        guide2.setOnClickListener(listener);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int offset = Math.abs(verticalOffset);
                Log.d(tag, "onOffsetChanged: " + offset + " appbare messure height: " + (appBarLayout.getMeasuredHeight() - title2.getMeasuredHeight() - slidingTabLayout.getMeasuredHeight()));
                setTitle2Alpha(offset);
            }
        });
    }

    private void setTitle2Alpha(float offset){
        int space = appBarLayout.getMeasuredHeight() - title2.getMeasuredHeight() - slidingTabLayout.getMeasuredHeight();
        float radio = offset / space;
        setTitleAlpha(title2, 1 - radio);
        setTitleAlpha(titleLayout, radio);
    }

    @Override
    protected void init() {
        setTitleAlpha(titleLayout, 0);
        setTitlePadding(titleLayout);
        setTitlePadding(title2);
        initViewPager();
        initTabList();
    }

    private void initViewPager(){
        fragments = new ArrayList<>();
        HiLiveListFragement liveListFragement = new HiLiveListFragement();
        GuideLiveFragment guideLiveFragment = new GuideLiveFragment();
        liveListFragement.setOnPagerInitListener(this);
        guideLiveFragment.setOnGuideLivePagerInitListener(this);
        fragments.add(liveListFragement);
        fragments.add(guideLiveFragment);
        funnyFragmentAdapter = new HiFunnyFragmentAdapter(fragment.getChildFragmentManager(), fragments);
        funnyViewPager.setAdapter(funnyFragmentAdapter);
    }

    private void initTabList(){
        updateLiveSlidingTabLayout(true);
        updateGuideSlidingTabLayout(false);
        hiFunnyTabDao.getFunnyTab(TabSet.LIVE);
        hiFunnyTabDao.getFunnyTab(TabSet.GUIDE);
    }

    public void clickLive(){
        updateLive2TextColor(true);
        updateTitleLiveTextColor(true);
        updateGuide2TextColor(false);
        updateTitleGuideTextColor(false);
        updateLiveSlidingTabLayout(true);
        updateGuideSlidingTabLayout(false);
        funnyViewPager.setCurrentItem(0);
    }

    public void clickGuide(){
        updateLive2TextColor(false);
        updateTitleLiveTextColor(false);
        updateGuide2TextColor(true);
        updateTitleGuideTextColor(true);
        updateLiveSlidingTabLayout(false);
        updateGuideSlidingTabLayout(true);
        funnyViewPager.setCurrentItem(1);
    }

    private void updateLiveSlidingTabLayout(boolean isSelect){
        slidingTabLayout.setVisibility(isSelect ? View.VISIBLE : View.GONE);
    }
    private void updateGuideSlidingTabLayout(boolean isSelect){
        slidingTabLayoutGuide.setVisibility(isSelect ? View.VISIBLE : View.GONE);
    }

    private void updateLive2TextColor(boolean isSelect){
        live2.setTextColor(getColor(isSelect ? R.color.black_43 : R.color.gray_text_e2));
    }

    private void updateGuide2TextColor(boolean isSelect){
        guide2.setTextColor(getColor(isSelect ? R.color.black_43 : R.color.gray_text_e2));
    }

    private void updateTitleLiveTextColor(boolean isSelect){
        titleLive.setTextColor(getColor(isSelect ? R.color.black_2e : R.color.gray_text_99));
    }

    private void updateTitleGuideTextColor(boolean isSelect){
        titleGuide.setTextColor(getColor(isSelect ? R.color.black_2e : R.color.gray_text_99));
    }

    @Override
    public void pagerInit(ViewPager viewPager, int number) {
        slidingTabLayout.setViewPager(viewPager, number >= 6 ? 6 : number);
    }

    @Override
    public void getGuideTabListError() {

    }
    @Override
    public void onGuideLivePagerInit(ViewPager viewPager, int number) {
        slidingTabLayoutGuide.setViewPager(viewPager,number >= 6 ? 6 : number);
    }

    @Override
    public void getLiveTabListSuccess(List<HiFunnyTabBean.Result.Body> tabs) {
        if(getHiLiveListFragement() != null){
            getHiLiveListFragement().initFragments(tabs);
        }
    }

    @Override
    public void getGuideTabListSuccess(List<HiFunnyTabBean.Result.Body> tabs) {
        if (tabs==null || tabs.size()<1)return;
        if (getGuideLiveFragment()!=null){
            getGuideLiveFragment().setViewPagerTags(tabs);
        }
    }

    @Override
    public void getLiveTabListError() {

    }

    private HiLiveListFragement getHiLiveListFragement(){
        try {
            return (HiLiveListFragement)fragments.get(0);
        }catch (Exception e){
            return null;
        }
    }

    private GuideLiveFragment getGuideLiveFragment(){
        try {
            return (GuideLiveFragment)fragments.get(1);
        }catch (Exception e){
            return null;
        }
    }


}
