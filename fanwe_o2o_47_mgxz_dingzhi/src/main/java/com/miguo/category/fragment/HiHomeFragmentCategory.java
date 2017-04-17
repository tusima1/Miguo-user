package com.miguo.category.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.fanwe.seller.views.SellerFragment;
import com.fanwe.view.FixRequestDisallowTouchEventPtrFrameLayout;
import com.fanwe.view.HomeTuanTimeLimitView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiRepresentBannerFragmentAdapter;
import com.miguo.adapter.HiRepresentCateAdapter;
import com.miguo.adapter.HomePagerAdapter;
import com.miguo.adapter.HomebannerPagerAdapter;
import com.miguo.dao.GetSearchCateConditionDao;
import com.miguo.dao.TouTiaoDao;
import com.miguo.dao.impl.GetSearchCateConditionDaoImpl;
import com.miguo.dao.impl.TouTiaoDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.definition.HomeActionUrls;
import com.miguo.definition.IntentKey;
import com.miguo.definition.TimeLimitedType;
import com.miguo.entity.BannerTypeModel;
import com.miguo.dao.CheckCitySignDao;
import com.miguo.dao.GetAdspaceListDao;
import com.miguo.dao.GetMenuListDao;
import com.miguo.dao.HomeGreetingDao;
import com.miguo.dao.impl.CheckCitySignDaoImpl;
import com.miguo.dao.impl.GetAdspaceListDaoImpl;
import com.miguo.dao.impl.GetMenuListDaoImpl;
import com.miguo.dao.impl.HomeGreetingDaoImpl;
import com.miguo.definition.AdspaceParams;
import com.miguo.definition.MenuParams;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.CheckCitySignBean;
import com.miguo.entity.MenuBean;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.entity.ToutiaoBean;
import com.miguo.factory.AdspaceTypeFactory;
import com.miguo.factory.ClassNameFactory;
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiRepresentCateFragment;
import com.miguo.listener.fragment.HiHomeFragmentListener;
import com.miguo.model.TouchToMoveListener;
import com.miguo.ui.view.HomeADView3;
import com.miguo.ui.view.HomeTimeLimitView;
import com.miguo.ui.view.RepresentViewPager;
import com.miguo.utils.BaseUtils;
import com.miguo.ui.view.AutoBanner;
import com.miguo.ui.view.AutofitTextView;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.HomeADView2;
import com.miguo.ui.view.HomeLooperViewPager;
import com.miguo.ui.view.HomeViewPager;
import com.miguo.ui.view.RecyclerBounceNestedScrollView;
import com.miguo.utils.HomeCategoryUtils;
import com.miguo.view.CheckCityView;
import com.miguo.view.GetAdspaceListView;
import com.miguo.view.GetSearchCateConditionView;
import com.miguo.view.HomeGreetingView;
import com.miguo.view.TouTiaoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by by zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeFragmentCategory extends FragmentCategory implements
        PtrHandler,
        RecyclerBounceNestedScrollView.OnRecyclerScrollViewListener,
        RecyclerBounceNestedScrollView.RecyclerScrollViewOnTouchListener,
        HomeLooperViewPager.HomeBannerViewPagerOnTouchListener,
        HomeTimeLimitView.OnTimeLimitClickListener,
        HomeTimeLimitView.TimeLimitedOnTouchListener,
        TouchToMoveListener,
        HomeGreetingView,
        GetAdspaceListView,
        CheckCityView {


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
    LinearLayout searchLayout;

    @ViewInject(R.id.frag_home_title_bar_ll_msg)
    LinearLayout messageLayout;

    @ViewInject(R.id.frag_home_title_bar_ll_earn)
    LinearLayout areaLayout;

    @ViewInject(R.id.ptr_layout)
    FixRequestDisallowTouchEventPtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.recycler_scrollview)
    RecyclerBounceNestedScrollView scrollView;

    /**
     * 狗蛋哥早安部分
     */
    @ViewInject(R.id.top_say_hi_layout)
    LinearLayout topSayHi;
    @ViewInject(R.id.sayhi)
    AutofitTextView sayhi;
    @ViewInject(R.id.city_sayhi)
    TextView citySayHi;

    @ViewInject(R.id.sayhi_layout)
    RelativeLayout sayHiLayout;

    @ViewInject(R.id.title_space)
    View space;

    @ViewInject(R.id.banner_layout)
    RelativeLayout bannerLayout;

    /**
     * 网络请求失败的显示界面
     */
    @ViewInject(R.id.loading_fail)
    LinearLayout loadingFail;
    @ViewInject(R.id.refresh)
    TextView refresh;

    /**
     * 有商家回家的标志
     */
    boolean hasSeeler = false;

    @ViewInject(R.id.pager)
    RepresentViewPager pager;
    HiRepresentBannerFragmentAdapter bannerAdapter;
    @ViewInject(R.id.indicator_circle)
    CircleIndicator circleIndicator;

    @ViewInject(R.id.live_layout)
    RelativeLayout liveLayout;

    /**
     * 米果头条
     */
    @ViewInject(R.id.toutiao_1)
    LinearLayout toutiaoLayout1;
    @ViewInject(R.id.toutiao1_name_1)
    TextView toutiao1Name1;
    @ViewInject(R.id.toutiao1_name_2)
    TextView toutiao1Name2;
    @ViewInject(R.id.toutiao1_content_1)
    TextView toutiao1Content1;
    @ViewInject(R.id.toutiao1_content_2)
    TextView toutiao1Content2;

    @ViewInject(R.id.toutiao_2)
    LinearLayout toutiaoLayout2;
    @ViewInject(R.id.toutiao2_name_1)
    TextView toutiao2Name1;
    @ViewInject(R.id.toutiao2_name_2)
    TextView toutiao2Name2;
    @ViewInject(R.id.toutiao2_content_1)
    TextView toutiao2Content1;
    @ViewInject(R.id.toutiao2_content_2)
    TextView toutiao2Content2;
    /**
     * 限时特惠
     */
    @ViewInject(R.id.home_tuan)
    HomeADView3 homeTuanTimeLimitView;
//    GetSpecialListDao getSpecialListDao;
    @ViewInject(R.id.home_tuan_limit_bottom_layout)
    LinearLayout homeTuanLimitBottomLayout;

    /**
     * 广告位2
     */
    @ViewInject(R.id.home_ad_view_2)
    HomeADView3 homeADView2;
    @ViewInject(R.id.home_ad_view_2_space_layout)
    LinearLayout homeAdView2SpaceLayout;

    /**
     * 接口类
     */
    HomeGreetingDao homeGreetingDao;
    GetAdspaceListDao getAdspaceListDao;
    CheckCitySignDao checkCitySignDao;

    /**
     * 今日精选
     */
    FeaturedGrouponCategory featuredGrouponCategory;



    boolean hasTop = true;
    int topHeight = dip2px(206);

    boolean touchDisableMove = false;

    @ViewInject(R.id.nodata)
    ImageView nodata;

    /**

     * 城市已开通的信息
     */
    CheckCitySignBean.Result.Body citySign;

    GetSearchCateConditionDao getSearchCateConditionDao;

    TouTiaoDao touTiaoDao;

    @ViewInject(R.id.mgdr_layout)
    LinearLayout mgrdLayout;

    @ViewInject(R.id.dyhb_layout)
    LinearLayout dyhbLayout;

    @ViewInject(R.id.mdyj_layout)
    LinearLayout mdyjLayout;

    @ViewInject(R.id.hbyh_layout)
    LinearLayout hbyhLayout;

    public HiHomeFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
//        getSpecialListDao = new GetSpecialListDaoImpl(this);
        homeGreetingDao = new HomeGreetingDaoImpl(this);
        getAdspaceListDao = new GetAdspaceListDaoImpl(this);
        checkCitySignDao = new CheckCitySignDaoImpl(this);
        initSearchCateCondition();
        initTouTiaoDao();
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
        mgrdLayout.setOnClickListener(listener);
        dyhbLayout.setOnClickListener(listener);
        mdyjLayout.setOnClickListener(listener);
        hbyhLayout.setOnClickListener(listener);


        nodata.setOnClickListener(listener);
        refresh.setOnClickListener(listener);
        messageLayout.setOnClickListener(listener);
        areaLayout.setOnClickListener(listener);
        liveLayout.setOnClickListener(listener);
        searchLayout.setOnClickListener(listener);
        titleLayout.setOnClickListener(listener);
        qrScran.setOnClickListener(listener);
        citySayHi.setOnClickListener(listener);
        homeADView2.setOnTopicAdsClickListener((HiHomeFragmentListener) listener);
        scrollView.setRecyclerScrollViewOnTouchListener(this);
    }

    @Override
    protected void init() {
        initHandler();
        setTopHeight(dip2px(210));
        setTitleAlpha(titleLayout, 0);
        setTitlePadding(sayHiLayout);
        setTitlePadding(titleLayout);
        initPtrLayout(ptrFrameLayout);
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


    public void clickLiveLayout(){
        getHomeViewPager().setCurrentItem(5);
    }

    /**
     * 获取分类数据
     */
    private void initSearchCateCondition(){
        getSearchCateConditionDao = new GetSearchCateConditionDaoImpl(new GetSearchCateConditionView(){
            @Override
            public void getSearchCateConditionError(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadComplete();
                    }
                });
            }

            @Override
            public void getSearchCateConditionSuccess(SearchCateConditionBean.ResultBean.BodyBean body) {
                SearchCateConditionFactory.update(body);
                updateCategories();
                loadComplete();
            }
        });
    }

    List<ToutiaoBean.Result.Body> toutiao;
    Handler handler;
    Timer timer;
    int currentPosition = 0;
    private void initTouTiaoDao(){
        touTiaoDao = new TouTiaoDaoImpl(new TouTiaoView() {
            @Override
            public void getToutiaoListSuccess(List<ToutiaoBean.Result.Body> toutiao){
                HiHomeFragmentCategory.this.toutiao = toutiao;
                initTimer();
            }

            @Override
            public void getToutiaoListError(String message) {

            }
        });
    }

    private void initHandler(){
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        currentPosition = (currentPosition + 1) * 2 >= toutiao.size() ? 0 : currentPosition + 1;
                        updateToutiao(currentPosition);
                        break;
                }
                return true;
            }
        });
    }

    private void initTimer(){
        if(toutiao == null || toutiao.size() <= 0){
            return;
        }
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 0, 3000);
    }

    private void updateToutiao(int position){
        toutiao1Name1.setText(toutiao.get(position * 2).getUser_name());
        toutiao1Content1.setText(toutiao.get(position * 2).getTitle());

        if(position * 2 + 1 < toutiao.size()){
            toutiao1Name2.setText(toutiao.get(position * 2 + 1).getUser_name());
            toutiao1Content2.setText(toutiao.get(position * 2 + 1).getTitle());
        }else {
            toutiao1Name2.setText("");
            toutiao1Content2.setText("");
        }

        int position2 = position * 2 + 2 >= toutiao.size() ? 0 : position + 1;

        toutiao2Name1.setText(toutiao.get(position2 * 2).getUser_name());
        toutiao2Content1.setText(toutiao.get(position2 * 2).getTitle());

        if(position2 * 2 + 1 < toutiao.size()){
            toutiao2Name2.setText(toutiao.get(position2 * 2 + 1).getUser_name());
            toutiao2Content2.setText(toutiao.get(position2 * 2 + 1).getTitle());
        }else {
            toutiao2Name2.setText("");
            toutiao2Content2.setText("");
        }


        startAnimation();

    }

    private void startAnimation(){
        toutiaoLayout2.setVisibility(View.VISIBLE);
        TranslateAnimation animation1 = new TranslateAnimation(0, 0, 0, -toutiaoLayout1.getMeasuredHeight());
        animation1.setDuration(500);


        TranslateAnimation animation2 = new TranslateAnimation(0, 0, toutiaoLayout1.getMeasuredHeight(), 0);
        animation2.setDuration(500);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toutiao1Name1.setText(toutiao2Name1.getText().toString());
                toutiao1Name2.setText(toutiao2Name2.getText().toString());

                toutiao1Content1.setText(toutiao2Content1.getText().toString());
                toutiao1Content2.setText(toutiao2Content2.getText().toString());

                toutiaoLayout2.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        toutiaoLayout1.startAnimation(animation1);
        toutiaoLayout2.startAnimation(animation2);

    }

    public void updateCategories(){
        SearchCateConditionBean.ResultBean.BodyBean bean = SearchCateConditionFactory.getHomeRepresent();
        if(null != bean){
            initCategories(bean.getCategoryList());
        }
    }

    private void initCategories(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> categories){
        updateCategoryViewPagerParams(categories);
        ArrayList<Fragment> fragments = new ArrayList<>();
        int count = categories.size() / 8 + (categories.size() % 8 > 0 ? 1 : 0);
        for(int i = 0; i < count; i++){
            List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> current = new ArrayList<>();
            int categoryTypeCount = ((i + 1) * 8 <= categories.size() ? 8 : categories.size() - (i * 8));
            for(int j = 0; j < categoryTypeCount; j++){
                current.add(categories.get(i * 8 + j));
            }
            HiRepresentCateFragment fragment = new HiRepresentCateFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentKey.REPRESENT_CATEGORYS, (Serializable) current);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        bannerAdapter = new HiRepresentBannerFragmentAdapter(fragment.getChildFragmentManager(), fragments);
        pager.setAdapter(bannerAdapter);
        circleIndicator.setViewPager(pager);
        circleIndicator.setVisibility(categories.size() <= 8 ? View.GONE : View.VISIBLE);
        pager.setTouchToMoveListener(this);
        pager.setPtrFrameLayout(ptrFrameLayout);
    }

    private void updateCategoryViewPagerParams(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> categories){
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getCategoryViewPagerHeight(categories));
        pager.setLayoutParams(params);
    }

    private int getCategoryViewPagerHeight(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> categories){
        return categories.size() <= 4 ? HiRepresentCateAdapter.getItemHeight() : HiRepresentCateAdapter.getItemHeight() * 2;
    }

    private void initDefaultCity() {
        ModelCityList bean = AppRuntimeWorker.getCityCurr();
        citySayHi.setText(bean.getUname());
        city.setText(bean.getName());
    }

    public void onRefreshAfter() {
        onRefreshGreeting();
//        onRefreshTimeLimit();
        onRefreshAdspaceList();
        onRefreshFeaturedGroupon();
        onRefreshSearchCondition();
        onRefreshToutiao();
        initLimited();
    }

    private void onRefreshToutiao(){
        touTiaoDao.getToutiao();
    }

    public void clickRefresh(){
        onRefresh();
    }


    public void onRefresh() {
        setCurrentHttpUuid(UUID.randomUUID().toString());
        checkCitySign();
    }

    private void checkCitySign() {
        /**
         * {@link com.miguo.dao.impl.CheckCitySignDaoImpl}
         * {@link #checkCitySignSuccess()}
         * {@link #checkCitySignError(CheckCitySignBean.Result.Body citySign)}
         */
        checkCitySignDao.checkCitySign(AppRuntimeWorker.getCity_id());
    }

    private void clearPage() {
        sayHiLayout.setVisibility(View.GONE);
//        homeTuanTimeLimitView.setVisibility(View.GONE);
        homeTuanLimitBottomLayout.setVisibility(View.GONE);
        homeADView2.setVisibility(View.GONE);
        featuredGrouponCategory.clearPage();
    }

    /**
     * 首页banner数据
     */
    private void initBanner(List<AdspaceListBean.Result.Body> bodys) {
//        bannerLayout.setVisibility(SDCollectionUtil.isEmpty(bodys) ? View.GONE : View.VISIBLE);
//        RelativeLayout.LayoutParams bannerParams = getRelativeLayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, SDCollectionUtil.isEmpty(bodys) ? RelativeLayout.LayoutParams.WRAP_CONTENT : dip2px(210));
//        homeViewPager.setLayoutParams(bannerParams);
//        bannerLayout.setVisibility(SDCollectionUtil.isEmpty(bodys) ? View.GONE : View.VISIBLE);
//        homeBannerAdapter = new HomebannerPagerAdapter(this, bodys);
//        homeViewPager.setAdapter(homeBannerAdapter);
    }

    private void initFeaturedGrouponCategory() {
        featuredGrouponCategory = new FeaturedGrouponCategory(view, fragment);
    }

    private void initHomeADView2(List<AdspaceListBean.Result.Body> body) {
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

    /**
     * 刷新今日精选
     */
    public void onRefreshFeaturedGroupon() {
        featuredGrouponCategory.onRefresh();
    }

    public void onRefreshSearchCondition(){
        getSearchCateConditionDao.getSearchCateCondition();
    }

    /**
     * 刷新广告
     * banners + topics
     */
    public void onRefreshAdspaceList(){
        getAdspaceListDao.getAdspaceList(getCurrentHttpUuid(), AppRuntimeWorker.getCity_id(), AdspaceParams.TYPE_INDEX, AdspaceParams.TERMINAL_TYPE);
    }

    /**
     * 刷新问候语
     */
    public void onRefreshGreeting() {
        if (!isHasSeeler()) {
            return;
        }
        homeGreetingDao.getTodayGreeting(getCurrentHttpUuid(), App.getApplication().getToken());
    }

    /**
     * 城市已开通，并且请求成功
     */
    public void showLoadingSuccessWithCity(){
        if(!isHasTop()){
            showTitleAndTab();
        }
        loadingFail.setVisibility(View.GONE);
        nodata.setVisibility(View.GONE);
    }

    /**
     * 显示网络加载失败！
     */
    public void showLoadingFailed(){
        showTitleAndTab();
        loadingFail.setVisibility(View.VISIBLE);
        nodata.setVisibility(View.GONE);
    }

    /**
     * 显示城市未开通
     */
    public void showNoCity(){
        showTitleAndTab();
        loadingFail.setVisibility(View.GONE);
        nodata.setVisibility(View.VISIBLE);
    }

    public void loadComplete() {
        ptrFrameLayout.refreshComplete();
    }

    public void loadCompleteWithLoadmore() {
        loadComplete();
        scrollView.loadComplite();
    }

    public void loadCompleteWithNoData() {
        loadComplete();
        scrollView.loadCompliteWithNoData();
    }

    public void loadCompleteWithError() {
        loadComplete();
        scrollView.loadCompliteWithError();
    }

    private void initLimited(){
        List<AdspaceListBean.Result.Body> limited = new ArrayList<>();
        AdspaceListBean.Result.Body xianShiTeHui = new AdspaceListBean().new Result().new Body();
        xianShiTeHui.setResId(R.drawable.xianshitehui);
        xianShiTeHui.setTitle("限时特惠");
        xianShiTeHui.setType(TimeLimitedType.XIAN_SHI_TE_HUI);

        AdspaceListBean.Result.Body yiYuanQiang = new AdspaceListBean().new Result().new Body();
        yiYuanQiang.setResId(R.drawable.yiyuanqiang);
        yiYuanQiang.setTitle("一元抢");
        yiYuanQiang.setType(TimeLimitedType.YI_YUAN_QIANG);

        AdspaceListBean.Result.Body benZhouTuiJian = new AdspaceListBean().new Result().new Body();
        benZhouTuiJian.setResId(R.drawable.benzhoutuijian);
        benZhouTuiJian.setTitle("本周推荐");
        benZhouTuiJian.setType(TimeLimitedType.BEN_ZHOU_TUI_JIAN);

        limited.add(xianShiTeHui);
        limited.add(yiYuanQiang);
        limited.add(benZhouTuiJian);
        homeTuanTimeLimitView.init(limited);
        initLimitedListener();
    }

    private void initLimitedListener(){
        homeTuanTimeLimitView.setOnTopicAdsClickListener(new HomeADView3.OnTopicAdsClickListener() {
            @Override
            public void onTopicAdsClick(AdspaceListBean.Result.Body ad) {
                switch (ad.getType()){
                    case TimeLimitedType.XIAN_SHI_TE_HUI:
                        break;
                    case TimeLimitedType.YI_YUAN_QIANG:
                        break;
                    case TimeLimitedType.BEN_ZHOU_TUI_JIAN:
                        break;
                }
            }
        });
    }

    /**
     * scroll view 滚动监听
     */
    @Override
    public void onScrollToEnd() {
        if (isHasSeeler()) {
            featuredGrouponCategory.onLoadMore();
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        checkTop(l, t, oldl, oldt);
        checkTitle(l, t, oldl, oldt);
        checkTopPadding(l, t, oldl, oldt);
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

    public void showTitleAndTab() {
        if (isAnimRunning()) {
            setShowTitleAndTabFlag(true);
            return;
        }
        setShowTitleAndTabFlag(false);
        startTabShowAnimation();
        startTitleShowAnimation();
    }

    private void setTitleVisibility(int visibility) {
        if (titleLayout == null) {
            return;
        }
        titleLayout.setVisibility(visibility);
    }

    private void setTabVisibility(int visibility) {
        if (getTab() == null) {
            return;
        }
        getTab().setVisibility(visibility);
    }

    int moveDistance = dip2px(30);
    int currentT = 0;
    boolean animRunning = false;
    long animDuration = 200;

    private void checkTitle(int l, int t, int oldl, int oldt) {

        if (!isHasTop() && !isAnimRunning() && t > (hasBanner() ? getTopHeight() : 0) && isHasSeeler()) {
            if (currentT == 0) {
                currentT = t;
            }
            /**
             * 下滑时要隐藏标题栏和底部栏
             */
            Log.d(tag + "a", "t: " + t + " ,t - moveDistance : " + (t - moveDistance) + " ,current: " + currentT);
            Log.d(tag + "b", "t: " + t + " ,t + moveDistance : " + (t + moveDistance) + " ,current: " + currentT);
            if (t - moveDistance > currentT) {
                if (titleLayout.getAlpha() > 0 || getTab().getAlpha() > 0) {
                    startTitleLeaveAnimation();
                    startTabLeaveAnimation();
                }
                currentT = 0;
            }

            if (t + moveDistance < currentT) {
                if (titleLayout.getAlpha() == 0) {
                    startTitleShowAnimation();
                    startTabShowAnimation();
                }
                currentT = 0;
            }
        }

        if (hasBanner()) {
            if (!isHasTop() && t < getTopHeight() - moveDistance && !isAnimRunning()) {
                currentT = 0;
                startTitleShowAnimation();
                startTabShowAnimation();
            }
        }


        if (hasBanner() && !isHasTop() && t == 0 && !isAnimRunning()) {
            currentT = 0;
            startTitleShowAnimation();
            startTabShowAnimation();
        }

    }

    public boolean hasBanner() {
        return bannerLayout.getVisibility() == View.VISIBLE;
    }

    private void startTitleLeaveAnimation() {
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
                if (isShowTitleAndTabFlag()) {
                    showTitleAndTab();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        titleLayout.startAnimation(titleAnimation);
    }

    private void startTabLeaveAnimation() {
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

    private void startTitleShowAnimation() {
        if (titleLayout.getAlpha() > 0) {
            setTitleAlpha(titleLayout, 1);
            setTitleVisibility(View.VISIBLE);
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

    private void startTabShowAnimation() {
        if (getTab().getAlpha() > 0) {
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


    private void checkTopPadding(int l, int t, int oldl, int oldt) {
        if (t == 0) {
            scrollView.scrollTo(2, 0);
        }
    }

    /** scroll view 滚动监听 end */


    /**
     * 滚出顶部后移除
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    private void checkTop(int l, int t, int oldl, int oldt) {
        if (isHasTop() && isHasSeeler()) {

            float radio = t / (float) (getTopHeight() - space.getMeasuredHeight());
            setTitleAlpha(titleLayout, radio);

            if (t >= getTopHeight() - space.getMeasuredHeight()) {
                setHasTop(false);
                sayHiLayout.removeView(topSayHi);
                LinearLayout.LayoutParams params = getLineaLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, titleLayout.getMeasuredHeight(), 0, 0);
                scrollView.scrollTo(0, space.getMeasuredHeight());
            }
        }
    }

    public void updateFromCityChanged(ModelCityList model) {
        updateCityName(model.getName(), model.getUname());
        onRefresh();
    }

    public void updateCityName(String cityName, String cityPy) {
        city.setText(cityName);
        /**
         * View有可能已经被移除了
         */
        try {
            citySayHi.setText(cityPy);
        } catch (Exception e) {

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
        getHomeViewPager().setCurrentItem(2);
        /**
         * 如果当前的低栏隐藏了
         */
        if (getTab().getAlpha() == 0) {
            startTabShowAnimation();
        }
    }

    /**
     * 跳转到门店列表
     */
    @Override
    public void onActionShopList(String cate_id, String tid) {
        if(cate_id == null || cate_id.equals("")){
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClassNameFactory.getClass(ClassPath.SECOND_REPRESENT));
        intent.putExtra(IntentKey.FIRST_TYPE, cate_id);
        intent.putExtra(IntentKey.SECOND_TYPE, tid);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
        /**
         * 如果当前的低栏隐藏了
         */
        if (getTab().getAlpha() == 0) {
            startTabShowAnimation();
        }
    }

    /**
     * ptr framelayout 下拉刷新监听
     */
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return scrollView.canRefresh() && !isTouchDisableMove();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    /** click method end ***/


    /**
     * ptr framelayout 下拉刷新监听 end
     */


    @Override
    public void checkCitySignSuccess() {
        scrollView.showLoadingLayout();
        setHasSeeler(true);
        showLoadingSuccessWithCity();
        homeAdView2SpaceLayout.setVisibility(View.VISIBLE);
        onRefreshAfter();
    }

    @Override
    public void checkCitySignError(final CheckCitySignBean.Result.Body citySign) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setCitySign(citySign);
                scrollView.hideLoadingLayout();
                clearPage();
                setTitleAlpha(titleLayout, 1);
                currentT = 0;
                sayHiLayout.setVisibility(View.VISIBLE);
                space.setVisibility(View.VISIBLE);
                homeAdView2SpaceLayout.setVisibility(View.GONE);
                try {
                    topSayHi.setVisibility(View.GONE);
                } catch (Exception e) {

                }
                showNoCity();
                setHasSeeler(false);
                loadComplete();
            }
        });

    }

    @Override
    public void networkError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleNetWorkError();
            }
        });

    }

    private void handleNetWorkError(){
        scrollView.hideLoadingLayout();
        clearPage();
        setTitleAlpha(titleLayout, 1);
        currentT = 0;
        sayHiLayout.setVisibility(View.VISIBLE);
        space.setVisibility(View.VISIBLE);
        homeAdView2SpaceLayout.setVisibility(View.GONE);
        try {
            topSayHi.setVisibility(View.GONE);
        } catch (Exception e) {

        }
        showLoadingFailed();
        setHasSeeler(false);
        loadComplete();
    }

//    /**
//     * 获取限时特惠数据回调
//     */
//    @Override
//    public void getSpecialListSuccess(String httpUuid,final SpecialListModel.Result result) {
//        if(!isCurrentHttp(httpUuid)){
//            loadComplete();
//            return;
//        }
//        if (getActivity() == null) {
//            return;
//        }
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (result != null) {
//                    homeTuanLimitBottomLayout.setVisibility(SDCollectionUtil.isEmpty(result.getBody()) ? View.GONE : View.VISIBLE);
//                    homeTuanTimeLimitView.setVisibility(SDCollectionUtil.isEmpty(result.getBody()) ? View.GONE : View.VISIBLE);
//                    homeTuanTimeLimitView.init(result);
//                    homeTuanTimeLimitView.setParent(ptrFrameLayout);
//                    homeTuanTimeLimitView.setOnTimeLimitClickListener(HiHomeFragmentCategory.this);
//                }
//                loadComplete();
//            }
//        });
//    }
//
//    @Override
//    public void getSpecialListLoadmoreSuccess(String httpUuid,SpecialListModel.Result result) {
//        loadComplete();
//    }
//
//    @Override
//    public void getSpecialListError(final String httpUuid,String msg) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(isCurrentHttp(httpUuid)){
//                    homeTuanLimitBottomLayout.setVisibility(View.GONE);
//                }
//                loadComplete();
//            }
//        });
//
//    }

//    @Override
//    public void getSpecialListNoData(String httpUuid,String msg) {
//        if(isCurrentHttp(httpUuid)){
//            homeTuanLimitBottomLayout.setVisibility(View.GONE);
//            homeTuanTimeLimitView.setVisibility(View.GONE);
//        }
//        loadComplete();
//    }
    /** 获取限时特惠数据回调 end*/


    /**
     * 限时特惠点击回调
     */
    @Override
    public void onTimeLimitClick() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), TimeLimitActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }
    /** 限时特惠点击回调 end*/


    /**
     * 首页广告内容
     *
     * @param body
     */
    @Override
    public void getAdspaceListSuccess(String httpUuid,final List<AdspaceListBean.Result.Body> body, final String type) {
        if(isCurrentHttp(httpUuid)){
            updateAdspaceViews(body, type);
        }
        loadComplete();
    }

    private void updateAdspaceViews(final List<AdspaceListBean.Result.Body> body, String type) {
        List<AdspaceListBean.Result.Body> banner = new ArrayList<>();
        List<AdspaceListBean.Result.Body> topic = new ArrayList<>();
        for (AdspaceListBean.Result.Body bd : body) {
            if (bd.getAdspace_id().equals(AdspaceParams.TYPE_BANNER_INDEX)) {
                banner.add(bd);
            } else if (bd.getAdspace_id().equals(AdspaceParams.TYPE_TOPIC_INDEX)) {
                topic.add(bd);
            }
        }
        initBanner(banner);
        initHomeADView2(topic);
    }

    @Override
    public void getAdspaceListError(final String httpUuid) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isCurrentHttp(httpUuid)){
                    homeADView2.setVisibility(View.GONE);
                }
                loadComplete();
            }
        });

    }

    /**
     * 首页问候语回调
     *
     * @param greeting
     */
    @Override
    public void getHomeGreetingSuccess(String httpUuid,final String greeting) {
        if(!isCurrentHttp(httpUuid)){
            return;
        }
        sayHiLayout.setVisibility(View.VISIBLE);
        try {
            sayhi.setText(greeting);
        } catch (Exception e) {

        }
    }

    @Override
    public void getHomeGreetingError(final String httpUuid) {
        if(!isCurrentHttp(httpUuid)){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getHomeGreetingSuccess(httpUuid,"神秘人，你好！");
                loadComplete();
            }
        });

    }


    /**
     * 专题点击回调
     *
     * @param ad
     */
    public void onTopicAdsClick(AdspaceListBean.Result.Body ad) {
        if (null == ad) {
            return;
        }

        if (null == ad.getType() || null == ad.getType_id()) {
            return;
        }

        String type_id = ad.getType_id();
        if (TextUtils.isEmpty(type_id) || !type_id.startsWith("{")) {
            return;
        }

        BannerTypeModel model = HomeCategoryUtils.parseTypeJson(type_id);

        if (ad.getType().equals(AdspaceParams.BANNER_LIVE_LIST)) {
            onActionLiveList();
            return;
        }
        if (ad.getType().equals(AdspaceParams.BANNER_SHOP_LIST)) {
            onActionShopList(model.getCate_id(), ad.getType_id());
            return;
        }
        String paramValue = model.getId();
        if (TextUtils.isEmpty(paramValue)) {
            paramValue = model.getUrl();
        }

        AdspaceTypeFactory.clickWidthType(ad.getType(), getActivity(), paramValue);
    }

    /**
     * 2016-11-25 edit by zhouhy for 南洋 把item.getType_id 变成了JSON.
     * 1 商品详情 门店详情 专题详情  id
     * 50 URL   url   52门店列表  cate_id     tid  54直播列表      tag
     * <p>
     * //keyword
     *
     * @param item
     */
    public void onTagsClick(MenuBean.Result.Body item) {
        if (null == item) {
            return;
        }
        String type_id = item.getType_id();
        if (TextUtils.isEmpty(type_id) || !type_id.startsWith("{")) {
            return;
        }

        BannerTypeModel model = HomeCategoryUtils.parseTypeJson(type_id);

        if (item.getType().equals(AdspaceParams.BANNER_LIVE_LIST)) {
            onActionLiveList();
            return;
        }
        if (item.getType().equals(AdspaceParams.BANNER_SHOP_LIST)) {
            onActionShopList(model.getCate_id(), model.getTid());
            return;
        }
        String paramValue = model.getId();
        if (TextUtils.isEmpty(paramValue)) {
            paramValue = model.getUrl();
        }

        AdspaceTypeFactory.clickWidthType(item.getType(), getActivity(), paramValue);
    }

    public boolean isHasTop() {
        return hasTop;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }

    /**
     * 还要减掉标题栏的高度
     *
     * @return
     */
    public int getTopHeight() {
        return topHeight;
    }

    public BarryTab getTab() {
        return (BarryTab) getActivity().findViewById(R.id.tab);
    }

    public boolean isAnimRunning() {
        return animRunning;
    }

    public void setAnimRunning(boolean animRunning) {
        this.animRunning = animRunning;
    }

    public HomeViewPager getHomeViewPager() {
        return (HomeViewPager) view.getParent();
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

    public CheckCitySignBean.Result.Body getCitySign() {
        return citySign;
    }

    public void setCitySign(CheckCitySignBean.Result.Body citySign) {
        this.citySign = citySign;
    }
}
