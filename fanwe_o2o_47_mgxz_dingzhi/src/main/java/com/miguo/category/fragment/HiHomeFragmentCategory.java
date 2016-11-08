package com.miguo.category.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.TimeLimitActivity;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.dao.barry.GetSpecialListDao;
import com.fanwe.dao.barry.impl.GetSpecialListDaoImpl;
import com.fanwe.dao.barry.view.GetSpecialListView;
import com.fanwe.model.CitylistModel;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.SellerFragment;
import com.fanwe.view.FixRequestDisallowTouchEventPtrFrameLayout;
import com.fanwe.view.HomeTuanTimeLimitView;
import com.fanwe.view.RecyclerScrollView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HomeBannerAdapter;
import com.miguo.adapter.HomePagerAdapter;
import com.miguo.dao.GetAdspaceListDao;
import com.miguo.dao.GetMenuListDao;
import com.miguo.dao.HomeGreetingDao;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.dao.impl.GetAdspaceListDaoImpl;
import com.miguo.dao.impl.GetMenuListDaoImpl;
import com.miguo.dao.impl.HomeGreetingDaoImpl;
import com.miguo.definition.AdspaceParams;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.MenuParams;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.MenuBean;
import com.miguo.factory.AdspaceTypeFactory;
import com.miguo.factory.ClassNameFactory;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HomeBannerFragmet;
import com.miguo.listener.fragment.HiHomeFragmentListener;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.ui.view.AutofitTextView;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.HomeADView2;
import com.miguo.ui.view.HomeBannerViewPager;
import com.miguo.ui.view.HomeTagsView;
import com.miguo.ui.view.HomeViewPager;
import com.miguo.view.GetAdspaceListView;
import com.miguo.view.GetMenuListView;
import com.miguo.view.HomeGreetingView;
import com.miguo.view.LoginByMobileView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by by zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeFragmentCategory extends FragmentCategory implements
        PtrHandler,
        RecyclerScrollView.OnRecyclerScrollViewListener,
        RecyclerScrollView.RecyclerScrollViewOnTouchListener,
        HomeBannerViewPager.HomeBannerViewPagerOnTouchListener,
        HomeTuanTimeLimitView.TimeLimitedOnTouchListener,
        GetSpecialListView, HomeTuanTimeLimitView.OnTimeLimitClickListener,
        HomeGreetingView,
        GetAdspaceListView, GetMenuListView{

    /**
     * 顶部导航栏的东西
     */
    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.frag_home_title_bar_tv_earn)
    TextView city;

    @ViewInject(R.id.frag_home_title_bar_ll_search)
    RelativeLayout searchLayout;

    @ViewInject(R.id.frag_home_title_bar_ll_msg)
    LinearLayout messageLayout;

    @ViewInject(R.id.frag_home_title_bar_ll_earn)
    LinearLayout areaLayout;

    @ViewInject(R.id.ptr_layout)
    FixRequestDisallowTouchEventPtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.recycler_scrollview)
    RecyclerScrollView scrollView;

    /**
     * 狗蛋哥早安部分
     */
    @ViewInject(R.id.top_say_hi_layout)
    LinearLayout topSayHi;
    @ViewInject(R.id.sayhi)
    AutofitTextView sayhi;
    @ViewInject(R.id.scroll_content)
    LinearLayout scrollContent;
    @ViewInject(R.id.city_sayhi)
    TextView citySayHi;

    /**
     * 轮播ViewPager
     */
    @ViewInject(R.id.home_view_pager)
    HomeBannerViewPager homeViewPager;

    HomeBannerAdapter homeBannerAdapter;
    @ViewInject(R.id.indicator_circle)
    CircleIndicator circleIndicator;

    /**
     * 限时特惠
     */
    @ViewInject(R.id.home_tuan)
    HomeTuanTimeLimitView homeTuanTimeLimitView;
    GetSpecialListDao getSpecialListDao;

    /**
     * 标签栏
     */
    @ViewInject(R.id.home_tags_view)
    HomeTagsView homeTagsView;

    /**
     * 广告位2
     */
    @ViewInject(R.id.home_ad_view_2)
    HomeADView2 homeADView2;

    /**
     * 接口类
     */
    HomeGreetingDao homeGreetingDao;
    GetAdspaceListDao getAdspaceListDao;
    GetMenuListDao getMenuListDao;
    /**
     * 今日精选
     */
    FeaturedGrouponCategory featuredGrouponCategory;

    boolean hasTop = true;
    int topHeight = dip2px(150);

    boolean touchDisableMove = false;

    public HiHomeFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        getSpecialListDao = new GetSpecialListDaoImpl(this);
        homeGreetingDao = new HomeGreetingDaoImpl(this);
        getAdspaceListDao = new GetAdspaceListDaoImpl(this);
        getMenuListDao = new GetMenuListDaoImpl(this);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiHomeFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        messageLayout.setOnClickListener(listener);
        areaLayout.setOnClickListener(listener);
        searchLayout.setOnClickListener(listener);
        citySayHi.setOnClickListener(listener);
        homeADView2.setOnTopicAdsClickListener((HiHomeFragmentListener)listener);
        homeTagsView.setOnHomeTagsClickListener((HiHomeFragmentListener)listener);
        scrollView.setRecyclerScrollViewOnTouchListener(this);
        homeViewPager.setHomeBannerViewPagerOnTouchListener(this);
        homeTuanTimeLimitView.setTimeLimitedOnTouchListener(this);
    }

    @Override
    protected void init() {
        setTopHeight(dip2px(150));
        setTitleAlpha(titleLayout, 0);
        setTitlePadding(titleLayout);
        initPtrLayout(ptrFrameLayout);
        initChirldViewsParent();
        initDefaultCity();
        /**
         * 今日精选
         */
        initFeaturedGrouponCategory();
        /**
         * 因为问候语上滑要消失，所以不加入刷新方法
         */
        onRefreshGreeting();
        onRefresh();
    }

    private void initChirldViewsParent(){
        homeTagsView.setHiHomeFragmentCategory(this);
    }

    private void initDefaultCity(){
        citySayHi.setText(AppRuntimeWorker.getCity_name());
        city.setText(AppRuntimeWorker.getCity_name());
    }

    public void onRefresh(){
        onRefreshTimeLimit();
        onRefreshAdspaceList();
        onRefreshFeaturedGroupon();
        onRefreshMenus();
        startTabShowAnimation();
    }

    /**
     * 首页banner数据
     */
    private void initBanner(List<AdspaceListBean.Result.Body> bodys){

        ArrayList<Fragment> fragmets = new ArrayList<>();

        if(bodys == null || bodys.size() == 0){
            bodys = new ArrayList<>();
            AdspaceListBean.Result.Body body = new AdspaceListBean().new Result().new Body();
            body.setIcon("");
            bodys.add(body);
        }

        for(int i = 0; i< bodys.size(); i++){
            HomeBannerFragmet fragmet = new HomeBannerFragmet();
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.HOME_BANNER_IMAGE, bodys.get(i));
            fragmet.setArguments(bundle);
            fragmets.add(fragmet);
        }
        if(homeBannerAdapter != null){
            homeBannerAdapter.notifyDataSetChanged(fragmets);
        }
        homeBannerAdapter = new HomeBannerAdapter(fragment.getChildFragmentManager(), fragmets);
        homeViewPager.setAdapter(homeBannerAdapter);
        circleIndicator.setViewPager(homeViewPager);
        homeBannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        homeBannerAdapter.notifyDataSetChanged();
    }

    private void initFeaturedGrouponCategory(){
        featuredGrouponCategory = new FeaturedGrouponCategory(view, fragment);
    }

    /**
     * 首页标签、菜单栏
     */
    private void initHomeMenuView(List<MenuBean.Result.Body> list){
        homeTagsView.init(list);
    }

    private void initHomeADView2(List<AdspaceListBean.Result.Body> body){
        homeADView2.init(body);
    }

    protected void initPtrLayout(PtrFrameLayout ptrFrameLayout) {
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
        MaterialHeader ptrHead = new MaterialHeader(getActivity());
        ptrHead.setPadding(0, 24, 0, 24);
        ptrFrameLayout.setHeaderView(ptrHead);
        ptrFrameLayout.addPtrUIHandler(ptrHead);
        /**
         * 设置下拉刷新回调
         */
        ptrFrameLayout.setPtrHandler(this);
        scrollView.setOnRecyclerScrollViewListener(this);
    }

    public void onRefreshTimeLimit(){
        try{
            getSpecialListDao.getSpecialList(
                    AppRuntimeWorker.getCity_id(),
                    BaiduMapManager.getInstance().getLongitude() + "",
                    BaiduMapManager.getInstance().getLatitude() + "",
                    "0");
        }catch (Exception e){
            getSpecialListDao.getSpecialList(
                    "fc9ebab9-7aa1-49d5-8c56-2bddc7d92ded",
                    "",
                    "",
                    "0");
        }
    }

    /**
     * 刷新今日精选
     */
    public void onRefreshFeaturedGroupon(){
        featuredGrouponCategory.onRefresh();
    }

    public void onRefreshMenus(){
        getMenuListDao.getMenuList(MenuParams.TERMINAL_TYPE, MenuParams.MENU_TYPE_INDEX);
    }

    /**
     * 刷新广告
     * banners + topics
     */
    public void onRefreshAdspaceList(){
        getAdspaceListDao.getAdspaceList(AppRuntimeWorker.getCity_id(), AdspaceParams.TYPE_INDEX, AdspaceParams.TERMINAL_TYPE);
    }


    /**
     * 刷新问候语
     */
    public void onRefreshGreeting(){
        homeGreetingDao.getTodayGreeting(App.getApplication().getToken());
    }


    public void loadComplete() {
        ptrFrameLayout.refreshComplete();
        scrollView.loadComplite();
    }

    /** scroll view 滚动监听 */
    @Override
    public void onScrollToEnd() {
        featuredGrouponCategory.onLoadMore();
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        checkTop(l, t, oldl, oldt);
        checkTitle(l, t , oldl, oldt);
        checkTopPadding(l, t , oldl, oldt);

    }


    int moveDistance = dip2px(30);
    int currentT = 0;
    boolean animRunning = false;
    long animDuration = 200;
    private void checkTitle(int l, int t, int oldl, int oldt){

        if(!isHasTop() && !isAnimRunning() && t > getTopHeight()){
            if(currentT == 0){
                currentT = t;
            }
            /**
             * 下滑时要隐藏标题栏和底部栏
             */
            Log.d(tag + "a" , "t: " + t + " ,t - moveDistance : " + (t - moveDistance) + " ,current: " + currentT);
            Log.d(tag + "b" , "t: " + t + " ,t + moveDistance : " + (t + moveDistance) + " ,current: " + currentT);
            if(t - moveDistance > currentT){
                if(titleLayout.getAlpha() > 1 || getTab().getAlpha() == 1){
                    startTitleLeaveAnimation();
                    startTabLeaveAnimation();
                }
                currentT = 0;
            }

            if(t + moveDistance < currentT){
                if(titleLayout.getAlpha() == 0){
                    startTitleShowAnimation();
                    startTabShowAnimation();
                }
                currentT = 0;
            }
        }

        if(!isHasTop() && t < getTopHeight() - moveDistance && !isAnimRunning() && titleLayout.getAlpha() == 1){
            currentT = 0;
            startTitleLeaveAnimation();
            startTabShowAnimation();
        }

    }

    private void startTitleLeaveAnimation(){
        TranslateAnimation titleAnimation = new TranslateAnimation(0, 0, 0, -titleLayout.getMeasuredHeight());
        titleAnimation.setDuration(animDuration);
        titleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimRunning(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimRunning(false);
                setTitleAlpha(titleLayout, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        titleLayout.startAnimation(titleAnimation);
    }

    private void startTabLeaveAnimation(){
        TranslateAnimation tabAnimation = new TranslateAnimation(0, 0, 0, getTab().getMeasuredHeight());
        tabAnimation.setDuration(animDuration);
        tabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimRunning(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimRunning(false);
                setTitleAlpha(getTab(), 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getTab().startAnimation(tabAnimation);

    }


    private void startTitleShowAnimation(){
        if(titleLayout.getAlpha() == 1){
            return;
        }
        setTitleAlpha(titleLayout, 1);
        TranslateAnimation titleAnimation = new TranslateAnimation(0, 0, -titleLayout.getMeasuredHeight(), 0);
        titleAnimation.setDuration(animDuration);
        titleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimRunning(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimRunning(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        titleLayout.startAnimation(titleAnimation);

        TranslateAnimation tabAnimation = new TranslateAnimation(0, 0, getTab().getMeasuredHeight(), 0);
        tabAnimation.setDuration(animDuration);
        tabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimRunning(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimRunning(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getTab().startAnimation(titleAnimation);

    }

    private void startTabShowAnimation(){
        if(getTab().getAlpha() == 1){
            return;
        }
        setTitleAlpha(getTab(), 1);
        TranslateAnimation tabAnimation = new TranslateAnimation(0, 0, getTab().getMeasuredHeight(), 0);
        tabAnimation.setDuration(animDuration);
        tabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAnimRunning(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAnimRunning(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getTab().startAnimation(tabAnimation);

    }


    private void checkTopPadding(int l, int t, int oldl, int oldt){
        if(t == 0){
            scrollView.scrollTo(2, 0);
        }
    }

    /** scroll view 滚动监听 end */


    /**
     * 滚出顶部后移除
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    private void checkTop(int l, int t, int oldl, int oldt){
        if(isHasTop()){
            if(t >= getTopHeight()){
                setHasTop(false);
                scrollContent.removeView(topSayHi);
                scrollView.scrollTo(0,0);
            }
        }
    }

    public void updateFromCityChanged(CitylistModel model){
        updateCityName(model.getName(), model.getPy());
        onRefresh();
    }

    public void updateCityName(String cityName, String cityPy){
        city.setText(cityName);
        /**
         * View有可能已经被移除了
         */
        try{
            citySayHi.setText(cityPy);
        }catch (Exception e){

        }
    }

    @Override
    public void onActionDown(MotionEvent ev) {
        setTouchDisableMove(true);
    }

    @Override
    public void onActionMove(MotionEvent ev) {
    }

    @Override
    public void onActionCancel(MotionEvent ev) {
        setTouchDisableMove(false);
    }

    /**
     * 跳转到直播列表
     */
    @Override
    public void onActionLiveList() {
        getHomeViewPager().setCurrentItem(1);
        /**
         * 如果当前的低栏隐藏了
         */
        if(getTab().getAlpha() == 0){
            startTabShowAnimation();
        }
    }

    /**
     * 跳转到门店列表
     */
    @Override
    public void onActionShopList(String cate_id) {
        getHomeViewPager().setCurrentItem(2);
        ((SellerFragment)((HomePagerAdapter)getHomeViewPager().getAdapter()).getItem(2)).handlerCateIdChanged(cate_id);
        /**
         * 如果当前的低栏隐藏了
         */
        if(getTab().getAlpha() == 0){
            startTabShowAnimation();
        }
    }

    /** ptr framelayout 下拉刷新监听 */
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return scrollView.canRefresh() && !isTouchDisableMove();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    /** click method end ***/


    /** ptr framelayout 下拉刷新监听 end*/


    /** 获取限时特惠数据回调*/
    @Override
    public void getSpecialListSuccess(final SpecialListModel.Result result) {
        if(getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result != null){
                        homeTuanTimeLimitView.setVisibility(View.VISIBLE);
                        homeTuanTimeLimitView.init(result);
                        homeTuanTimeLimitView.setParent(ptrFrameLayout);
                        homeTuanTimeLimitView.setOnTimeLimitClickListener(HiHomeFragmentCategory.this);
                }
            }
        });
    }

    @Override
    public void getSpecialListLoadmoreSuccess(SpecialListModel.Result result) {
    }

    @Override
    public void getSpecialListError(String msg) {
    }

    @Override
    public void getSpecialListNoData(String msg) {
    }
    /** 获取限时特惠数据回调 end*/


    /** 限时特惠点击回调*/
    @Override
    public void onTimeLimitClick() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), TimeLimitActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }
    /** 限时特惠点击回调 end*/


    /**
     * 首页广告内容
     * @param body
     */
    @Override
    public void getAdspaceListSuccess(final List<AdspaceListBean.Result.Body> body, final String type) {
        updateAdspaceViews(body, type);
    }

    private void updateAdspaceViews(final List<AdspaceListBean.Result.Body> body, String type){
        List<AdspaceListBean.Result.Body> banner = new ArrayList<>();
        List<AdspaceListBean.Result.Body> topic = new ArrayList<>();
        for(AdspaceListBean.Result.Body bd : body ){
            if(bd.getAdspace_id().equals(AdspaceParams.TYPE_BANNER_INDEX)){
                banner.add(bd);
            }else {
                topic.add(bd);
            }
        }
        initBanner(banner);
        initHomeADView2(topic);
    }

    @Override
    public void getAdspaceListError() {

    }

    /**
     * 首页问候语回调
     * @param greeting
     */
    @Override
    public void getHomeGreetingSuccess(final String greeting) {
        try {
            sayhi.setText(greeting);
        }catch (Exception e){

        }
    }

    @Override
    public void getHomeGreetingError() {
        getHomeGreetingSuccess("神秘人，你好！");
    }

    /**
     * 首页菜单
     */
    @Override
    public void getMenuListSuccess(final List<MenuBean.Result.Body> list) {
        initHomeMenuView(list);
    }

    @Override
    public void getMenuListError() {

    }

    /**
     * 专题点击回调
     * @param ad
     */
    public void onTopicAdsClick(AdspaceListBean.Result.Body ad) {
        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.SPECIAL_TOPIC_ACTIVITY));
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.SPECIAL_TOPIC_ID, ad.getType_id());
        intent.putExtras(bundle);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    public void onTagsClick(MenuBean.Result.Body item){
        if(item.getType().equals(AdspaceParams.BANNER_LIVE_LIST)){
            onActionLiveList();
            return;
        }
        if(item.getType().equals(AdspaceParams.BANNER_SHOP_LIST)){
            onActionShopList(item.getType_id());
            return;
        }
        AdspaceTypeFactory.clickWidthType(item.getType(), getActivity(), item.getType_id());
    }

    public boolean isHasTop() {
        return hasTop;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }

    /**
     * 还要减掉标题栏的高度
     * @return
     */
    public int getTopHeight() {
        return topHeight;
    }

    public BarryTab getTab(){
        return (BarryTab)getActivity().findViewById(R.id.tab);
    }

    public boolean isAnimRunning() {
        return animRunning;
    }

    public void setAnimRunning(boolean animRunning) {
        this.animRunning = animRunning;
    }

    public HomeViewPager getHomeViewPager(){
        return (HomeViewPager)view.getParent();
    }

    public void setTopHeight(int topHeight) {
        this.topHeight = topHeight;
    }

    public boolean isTouchDisableMove() {
        return touchDisableMove;
    }

    public void setTouchDisableMove(boolean touchDisableMove) {
        this.touchDisableMove = touchDisableMove;
    }
}
