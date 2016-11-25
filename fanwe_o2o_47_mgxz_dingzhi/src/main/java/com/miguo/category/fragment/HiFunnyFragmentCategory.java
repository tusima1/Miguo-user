package com.miguo.category.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.ChineseCharClassifier;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiFunnyFragmentAdapter;
import com.miguo.dao.GetInterestingDao;
import com.miguo.dao.HiFunnyTabDao;
import com.miguo.dao.impl.GetInterestingDaoImpl;
import com.miguo.dao.impl.HiFunnyTabDaoImpl;
import com.miguo.definition.TabSet;
import com.miguo.entity.HiFunnyTabBean;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiLiveListFragement;
import com.miguo.listener.fragment.HiFunnyFragmentListener;
import com.miguo.ui.view.FunnyViewPager;
import com.miguo.ui.view.SlidingTabLayout;
import com.miguo.view.GetInterestingView;
import com.miguo.view.HiFunnyTabView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/16.
 */

public class HiFunnyFragmentCategory extends FragmentCategory implements
        HiLiveListFragmentCategory.OnPagerInitListener,
        HiFunnyTabView,GetInterestingView{

    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.top_text_layout)
    LinearLayout titleTextLayout;
    /**
     * 大字体。
     */
    @ViewInject(R.id.title_text)
    private TextView titleText;
    /**
     * 小字体。
     */
    @ViewInject(R.id.summary_text)
    private TextView summaryText;

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
    ViewPager funnyViewPager;
    HiFunnyFragmentAdapter funnyFragmentAdapter;
    ArrayList<Fragment> fragments;

    HiFunnyTabDao hiFunnyTabDao;
    GetInterestingDao interestingDao;

    private SharedPreferences settings;
    private String interestingStr = "";
    String currentData = "";


    public HiFunnyFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        currentData = DateFormat.format("yyyy-MM-dd",new Date(System.currentTimeMillis())).toString();
        settings = getActivity().getSharedPreferences("miguo", Context.MODE_PRIVATE);
        interestingStr = settings.getString("Interesting","");
        hiFunnyTabDao = new HiFunnyTabDaoImpl(this);
        interestingDao = new GetInterestingDaoImpl(this);
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
        setTitlePadding(titleTextLayout);
        setTitlePadding(title2);
        initViewPager();
        initTabList();
        initInteresting();
    }

    private void initInteresting(){
        String cityId = AppRuntimeWorker.getCity_id();
        if (!TextUtils.isEmpty(interestingStr)) {
            String[] list = interestingStr.split("-");
            if (list.length < 3) {
                settings.edit().putString("Interesting", "").commit();
                interestingDao.getInteresting(cityId);
            } else {
                if (cityId.equals(list[0]) && currentData.equals(list[1])) {
                    setInterestingStr(list[2]);
                } else {
                    settings.edit().putString("Interesting", "").commit();
                    interestingDao.getInteresting(cityId);
                }
            }
        } else {
            interestingDao.getInteresting(cityId);
        }
    }

    private void initViewPager(){
        fragments = new ArrayList<>();
        HiLiveListFragement liveListFragement = new HiLiveListFragement();
        HiLiveListFragement liveListFragement2 = new HiLiveListFragement();
        liveListFragement.setOnPagerInitListener(this);
        fragments.add(liveListFragement);
        fragments.add(liveListFragement2);
        funnyFragmentAdapter = new HiFunnyFragmentAdapter(fragment.getChildFragmentManager(), fragments);
        funnyViewPager.setAdapter(funnyFragmentAdapter);
    }

    private void initTabList(){
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
    public void getLiveTabListSuccess(List<HiFunnyTabBean.Result.Body> tabs) {
        if(getHiLiveListFragement() != null){
            getHiLiveListFragement().initFragments(tabs);
        }
    }

    @Override
    public void getGuideTabListSuccess(List<HiFunnyTabBean.Result.Body> tabs) {

    }

    @Override
    public void getLiveTabListError() {

    }

    @Override
    public void getInterestingError() {

    }

    @Override
    public void getInterestingSuccess(List<HashMap<String, String>> datas) {
        if (datas == null || datas.size() < 1) {
            return;
        } else {
            String value = datas.get(0).get("value");
            if (!TextUtils.isEmpty(value)) {
                setInterestingStr(value);
                settings.edit().putString("Interesting", AppRuntimeWorker.getCity_id() + "-" + currentData + "-" + value).commit();

            }
        }
    }
    /**
     * 显示有趣页欢迎的话。
     *
     * @param interestingStr
     */
    public void setInterestingStr(String interestingStr) {
        if (interestingStr == null || interestingStr.length() < 1) {
            return;
        } else {
            String titleStr = "";
            String summaryStr = "";
            char[] chars = interestingStr.toCharArray();
            if (interestingStr.length() <= 6) {
                titleStr = interestingStr;
                summaryStr = "";
            } else {
                if (interestingStr.indexOf(",")==6|| ChineseCharClassifier.isChinesePunctuation(chars[6])) {
                    titleStr = interestingStr.substring(0, 7);
                    summaryStr = interestingStr.substring(7, interestingStr.length());
                } else {
                    titleStr = interestingStr.substring(0, 6);
                    summaryStr = interestingStr.substring(6, interestingStr.length());
                }
                if (summaryStr.endsWith(",") ||  ChineseCharClassifier.isChinesePunctuation(chars[summaryStr.length()-1])) {
                    summaryStr = summaryStr.substring(0, summaryStr.length() - 1);
                }
            }
            titleText.setText(titleStr);
            summaryText.setText(summaryStr);
        }
    }

    public HiLiveListFragement getHiLiveListFragement(){
        try {
            return (HiLiveListFragement)fragments.get(0);
        }catch (Exception e){
            return null;
        }
    }

}
