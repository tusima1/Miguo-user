package com.miguo.category.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.TimeLimitActivity;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.dao.barry.GetSpecialListDao;
import com.fanwe.dao.barry.impl.GetSpecialListDaoImpl;
import com.fanwe.dao.barry.view.GetSpecialListView;
import com.fanwe.library.utils.SDCollectionUtil;
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
import com.miguo.dao.CheckCitySignDao;
import com.miguo.dao.GetAdspaceListDao;
import com.miguo.dao.GetMenuListDao;
import com.miguo.dao.HomeGreetingDao;
import com.miguo.dao.impl.CheckCitySignDaoImpl;
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
import com.miguo.ui.view.RecyclerBounceScrollView;
import com.miguo.view.CheckCityView;
import com.miguo.view.GetAdspaceListView;
import com.miguo.view.GetMenuListView;
import com.miguo.view.HomeGreetingView;

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
        RecyclerBounceScrollView.OnRecyclerScrollViewListener,
        RecyclerBounceScrollView.RecyclerScrollViewOnTouchListener,
        HomeBannerViewPager.HomeBannerViewPagerOnTouchListener,
        HomeTuanTimeLimitView.TimeLimitedOnTouchListener,
        GetSpecialListView, HomeTuanTimeLimitView.OnTimeLimitClickListener,
        HomeGreetingView,
        GetAdspaceListView, GetMenuListView, CheckCityView{

    /**
     * 顶部导航栏的东西
     */
    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.frag_home_title_bar_tv_earn)
    TextView city;

    @ViewInject(R.id.frag_home_title_bar_ll_saoyisao)
    ImageView qrScran;

    @ViewInject(R.id.frag_home_title_bar_ll_search)
    RelativeLayout searchLayout;

    @ViewInject(R.id.frag_home_title_bar_ll_msg)
    LinearLayout messageLayout;

    @ViewInject(R.id.frag_home_title_bar_ll_earn)
    LinearLayout areaLayout;

    @ViewInject(R.id.ptr_layout)
    FixRequestDisallowTouchEventPtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.recycler_scrollview)
    RecyclerBounceScrollView scrollView;

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

    @ViewInject(R.id.sayhi_layout)
    RelativeLayout sayHiLayout;

    @ViewInject(R.id.title_space)
    View space;

    /**
     * 轮播ViewPager
     */
    @ViewInject(R.id.home_view_pager)
    HomeBannerViewPager homeViewPager;

    @ViewInject(R.id.banner_layout)
    RelativeLayout bannerLayout;

    HomeBannerAdapter homeBannerAdapter;
    @ViewInject(R.id.indicator_circle)
    CircleIndicator circleIndicator;

    /**
     * 有商家回家的标志
     */
    boolean hasSeeler = false;

    /**
     * 限时特惠
     */
    @ViewInject(R.id.home_tuan)
    HomeTuanTimeLimitView homeTuanTimeLimitView;
    GetSpecialListDao getSpecialListDao;
    @ViewInject(R.id.home_tuan_limit_bottom_layout)
    LinearLayout homeTuanLimitBottomLayout;

    /**
     * 标签栏
     */
    @ViewInject(R.id.home_tags_view)
    HomeTagsView homeTagsView;
    @ViewInject(R.id.home_tags_view_layout)
    LinearLayout homeTagsViewLayout;

    /**
     * 广告位2
     */
    @ViewInject(R.id.home_ad_view_2)
    HomeADView2 homeADView2;
    @ViewInject(R.id.home_ad_view_2_space_layout)
    LinearLayout homeAdView2SpaceLayout;

    /**
     * 接口类
     */
    HomeGreetingDao homeGreetingDao;
    GetAdspaceListDao getAdspaceListDao;
    GetMenuListDao getMenuListDao;
    CheckCitySignDao checkCitySignDao;

    /**
     * 今日精选
     */
    FeaturedGrouponCategory featuredGrouponCategory;

    boolean hasTop = true;
    int topHeight = dip2px(150);

    boolean touchDisableMove = false;

    @ViewInject(R.id.nodata)
    ImageView nodata;

    public HiHomeFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        getSpecialListDao = new GetSpecialListDaoImpl(this);
        homeGreetingDao = new HomeGreetingDaoImpl(this);
        getAdspaceListDao = new GetAdspaceListDaoImpl(this);
        getMenuListDao = new GetMenuListDaoImpl(this);
        checkCitySignDao = new CheckCitySignDaoImpl(this);
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
        qrScran.setOnClickListener(listener);
        citySayHi.setOnClickListener(listener);
        homeADView2.setOnTopicAdsClickListener((HiHomeFragmentListener)listener);
        homeTagsView.setOnHomeTagsClickListener((HiHomeFragmentListener)listener);
        scrollView.setRecyclerScrollViewOnTouchListener(this);
        homeViewPager.setHomeBannerViewPagerOnTouchListener(this);
        homeTuanTimeLimitView.setTimeLimitedOnTouchListener(this);
    }

    @Override
    protected void init() {
        setTopHeight(dip2px(210));
        setTitleAlpha(titleLayout, 0);
        setTitlePadding(sayHiLayout);
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
        onRefresh();
    }

    private void initChirldViewsParent(){
        homeTagsView.setHiHomeFragmentCategory(this);
    }

    private void initDefaultCity(){
        citySayHi.setText(AppRuntimeWorker.getCityNameInPy());
        city.setText(AppRuntimeWorker.getCity_name());
    }

    public void onRefreshAfter(){
//        clearPage();
        onRefreshGreeting();
        onRefreshTimeLimit();
        onRefreshAdspaceList();
        onRefreshFeaturedGroupon();
        onRefreshMenus();
        startTabShowAnimation();
    }

    public void onRefresh(){
        checkCitySign();
    }

    private void checkCitySign(){
        checkCitySignDao.checkCitySign(AppRuntimeWorker.getCity_id());
    }

    private void clearPage(){
        sayHiLayout.setVisibility(View.GONE);
        bannerLayout.setVisibility(View.GONE);
        homeTuanTimeLimitView.setVisibility(View.GONE);
        homeTuanLimitBottomLayout.setVisibility(View.GONE);
        homeTagsView.setVisibility(View.GONE);
        homeTagsViewLayout.setVisibility(View.GONE);
        homeADView2.setVisibility(View.GONE);
        featuredGrouponCategory.clearPage();
    }

    /**
     * 首页banner数据
     */
    private void initBanner(List<AdspaceListBean.Result.Body> bodys){
        bannerLayout.setVisibility(SDCollectionUtil.isEmpty(bodys) ? View.GONE : View.VISIBLE);
        RelativeLayout.LayoutParams bannerParams = getRelativeLayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, SDCollectionUtil.isEmpty(bodys) ? RelativeLayout.LayoutParams.WRAP_CONTENT : dip2px(200));
        homeViewPager.setLayoutParams(bannerParams);

        bannerLayout.setVisibility(SDCollectionUtil.isEmpty(bodys) ? View.GONE : View.VISIBLE);

        ArrayList<Fragment> fragmets = new ArrayList<>();
        for(int i = 0; i< bodys.size(); i++){
            HomeBannerFragmet fragmet = new HomeBannerFragmet();
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.HOME_BANNER_IMAGE, bodys.get(i));
            fragmet.setArguments(bundle);
            fragmets.add(fragmet);
        }
        homeBannerAdapter = new HomeBannerAdapter(fragment.getChildFragmentManager(), fragmets);
        homeViewPager.setAdapter(homeBannerAdapter);
        circleIndicator.setViewPager(homeViewPager);
        homeBannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    private void initFeaturedGrouponCategory(){
        featuredGrouponCategory = new FeaturedGrouponCategory(view, fragment);
    }

    /**
     * 首页标签、菜单栏
     */
    private void initHomeMenuView(List<MenuBean.Result.Body> list){
        homeTagsView.setVisibility(SDCollectionUtil.isEmpty(list) ? View.GONE : View.VISIBLE);
        homeTagsViewLayout.setVisibility(SDCollectionUtil.isEmpty(list) ? View.GONE : View.VISIBLE);
        homeTagsView.init(list);
    }

    private void initHomeADView2(List<AdspaceListBean.Result.Body> body){
        homeADView2.setVisibility(SDCollectionUtil.isEmpty(body) ? View.GONE : View.VISIBLE);
        homeAdView2SpaceLayout.setVisibility(SDCollectionUtil.isEmpty(body) ? View.GONE : View.VISIBLE);
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
        if(!isHasSeeler()){
            return;
        }
        homeGreetingDao.getTodayGreeting(App.getApplication().getToken());
    }


    public void loadComplete() {
        ptrFrameLayout.refreshComplete();
    }

    public void loadCompleteWithLoadmore(){
        loadComplete();
        scrollView.loadComplite();
    }

    public void loadCompleteWithNoData(){
        loadComplete();
        scrollView.loadCompliteWithNoData();
    }

    public void loadCompleteWithError(){
        loadComplete();
        scrollView.loadCompliteWithError();
    }

    /** scroll view 滚动监听 */
    @Override
    public void onScrollToEnd() {
        if(isHasSeeler()){
            featuredGrouponCategory.onLoadMore();
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        checkTop(l, t, oldl, oldt);
        checkTitle(l, t , oldl, oldt);
        checkTopPadding(l, t , oldl, oldt);
    }

    /**
     * 如果在tab消失过程中的点击了tab，这个时候设置tab出现是无效的，因为处于动画中
     * 所以在这里设置一个标志判断是否需要显示，如果这个标志为true
     * 那就需要在tab消失动画结束后再调用这个方法
     */
    boolean showTitleAndTabFlag = false;

    public boolean isShowTitleAndTabFlag() {
        return showTitleAndTabFlag;
    }

    public void setShowTitleAndTabFlag(boolean showTitleAndTabFlag) {
        this.showTitleAndTabFlag = showTitleAndTabFlag;
    }

    public void showTitleAndTab(){
        if(isAnimRunning()){
            setShowTitleAndTabFlag(true);
            return;
        }
        setShowTitleAndTabFlag(false);
        startTabShowAnimation();
        startTitleShowAnimation();
    }

    private void setTitleVisibility(int visibility){
        if(titleLayout == null){
            return;
        }
        titleLayout.setVisibility(visibility);
    }

    private void setTabVisibility(int visibility){
        if(getTab() == null){
            return;
        }
        getTab().setVisibility(visibility);
    }

    int moveDistance = dip2px(30);
    int currentT = 0;
    boolean animRunning = false;
    long animDuration = 200;
    private void checkTitle(int l, int t, int oldl, int oldt){

        if(!isHasTop() && !isAnimRunning() && t > (hasBanner() ? getTopHeight() : 0) && isHasSeeler()){
            if(currentT == 0){
                currentT = t;
            }
            /**
             * 下滑时要隐藏标题栏和底部栏
             */
            Log.d(tag + "a" , "t: " + t + " ,t - moveDistance : " + (t - moveDistance) + " ,current: " + currentT);
            Log.d(tag + "b" , "t: " + t + " ,t + moveDistance : " + (t + moveDistance) + " ,current: " + currentT);
            if(t - moveDistance > currentT){
                if(titleLayout.getAlpha() > 0 || getTab().getAlpha() > 0){
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

        if(hasBanner()){
            if(!isHasTop() && t < getTopHeight() - moveDistance && !isAnimRunning()){
                currentT = 0;
                startTitleShowAnimation();
                startTabShowAnimation();
            }
        }


        if(hasBanner() && !isHasTop() && t == 0 && !isAnimRunning()){
            currentT = 0;
            startTitleShowAnimation();
            startTabShowAnimation();
        }

    }

    public boolean hasBanner(){
        return bannerLayout.getVisibility() == View.VISIBLE;
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
                setTitleVisibility(View.GONE);
                if(isShowTitleAndTabFlag()){
                    showTitleAndTab();
                }
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
                setTabVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getTab().startAnimation(tabAnimation);

    }

    private void startTitleShowAnimation(){
        if(titleLayout.getAlpha() > 0){
            return;
        }
        setTitleAlpha(titleLayout, 1);
        setTitleVisibility(View.VISIBLE);
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

    }

    private void startTabShowAnimation(){
        if(getTab().getAlpha() > 0){
            return;
        }
        setTitleAlpha(getTab(), 1);
        setTabVisibility(View.VISIBLE);
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
        if(isHasTop() && isHasSeeler()){

            float radio = t / (float)(getTopHeight() - space.getMeasuredHeight());
            setTitleAlpha(titleLayout, radio);

            if(t >= getTopHeight() - space.getMeasuredHeight()){
                setHasTop(false);
                sayHiLayout.removeView(topSayHi);
                LinearLayout.LayoutParams params = getLineaLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, titleLayout.getMeasuredHeight(), 0, 0);
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


    @Override
    public void checkCitySignSuccess() {
        setHasSeeler(true);
        nodata.setVisibility(View.GONE);
        homeAdView2SpaceLayout.setVisibility(View.VISIBLE);
        onRefreshAfter();
    }

    @Override
    public void checkCitySignError() {
        clearPage();
        setTitleAlpha(titleLayout, 1);
        currentT = 0;
        sayHiLayout.setVisibility(View.VISIBLE);
        space.setVisibility(View.VISIBLE);
        homeAdView2SpaceLayout.setVisibility(View.GONE);
        try{
            topSayHi.setVisibility(View.GONE);
        }catch (Exception e){

        }
        nodata.setVisibility(View.VISIBLE);
        setHasSeeler(false);
        loadComplete();
    }

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
                    homeTuanLimitBottomLayout.setVisibility(SDCollectionUtil.isEmpty(result.getBody()) ? View.GONE : View.VISIBLE);
                    homeTuanTimeLimitView.setVisibility(SDCollectionUtil.isEmpty(result.getBody()) ? View.GONE : View.VISIBLE);
                    homeTuanTimeLimitView.init(result);
                    homeTuanTimeLimitView.setParent(ptrFrameLayout);
                    homeTuanTimeLimitView.setOnTimeLimitClickListener(HiHomeFragmentCategory.this);
                }
                loadComplete();
            }
        });
    }

    @Override
    public void getSpecialListLoadmoreSuccess(SpecialListModel.Result result) {
        loadComplete();
    }

    @Override
    public void getSpecialListError(String msg) {
        homeTuanLimitBottomLayout.setVisibility(View.GONE);
        loadComplete();
    }

    @Override
    public void getSpecialListNoData(String msg) {
        homeTuanLimitBottomLayout.setVisibility(View.GONE);
        homeTuanTimeLimitView.setVisibility(View.GONE);
        loadComplete();
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
        loadComplete();
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
        homeADView2.setVisibility(View.GONE);
        loadComplete();
    }

    /**
     * 首页问候语回调
     * @param greeting
     */
    @Override
    public void getHomeGreetingSuccess(final String greeting) {
        sayHiLayout.setVisibility(View.VISIBLE);
        try {
            sayhi.setText(greeting);
        }catch (Exception e){

        }
    }

    @Override
    public void getHomeGreetingError() {
        getHomeGreetingSuccess("神秘人，你好！");
        loadComplete();
    }

    /**
     * 首页菜单
     */
    @Override
    public void getMenuListSuccess(final List<MenuBean.Result.Body> list) {
        initHomeMenuView(list);
        loadComplete();
    }

    @Override
    public void getMenuListError() {
        loadComplete();
    }

    /**
     * 专题点击回调
     * @param ad
     */
    public void onTopicAdsClick(AdspaceListBean.Result.Body ad) {
        if(null == ad){
            return;
        }

        if(null == ad.getType() || null == ad.getType_id()){
            return;
        }

        Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.SPECIAL_TOPIC_ACTIVITY));
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.SPECIAL_TOPIC_ID, ad.getType_id());
        intent.putExtras(bundle);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    public void onTagsClick(MenuBean.Result.Body item){
        if(null == item){
            return;
        }

        if(null == item.getType_id() || null == item.getType_id()){
            return;
        }

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

    public boolean isHasSeeler() {
        return hasSeeler;
    }

    public void setHasSeeler(boolean hasSeeler) {
        this.hasSeeler = hasSeeler;
    }

}
