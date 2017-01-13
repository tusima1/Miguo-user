package com.miguo.category.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiRepresentBannerFragmentAdapter;
import com.miguo.adapter.HiRepresentCateAdapter;
import com.miguo.dao.GetAdspaceListDao;
import com.miguo.dao.GetSearchCateConditionDao;
import com.miguo.dao.impl.GetAdspaceListDaoImpl;
import com.miguo.dao.impl.GetSearchCateConditionDaoImpl;
import com.miguo.definition.AdspaceParams;
import com.miguo.definition.IntentKey;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.factory.SearchCateConditionFactory;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiRepresentCateFragment;
import com.miguo.listener.fragment.HiRepresentFragmentListener;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.RecyclerBounceNestedScrollView;
import com.miguo.ui.view.RepresentAppBarLayout;
import com.miguo.ui.view.RepresentBannerView;
import com.miguo.ui.view.RepresentViewPager;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;
import com.miguo.view.GetAdspaceListView;
import com.miguo.view.GetSearchCateConditionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentFragmentCategory extends FragmentCategory implements PtrHandler, RecyclerBounceNestedScrollView.OnRecyclerScrollViewListener{

    @ViewInject(R.id.ptr_layout)
    PtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.scroll_layout)
    LinearLayout scrollLayout;

    @ViewInject(R.id.coorddinatorlayout)
    RecyclerBounceNestedScrollView scrollview;

    @ViewInject(R.id.app_bar)
    LinearLayout appBarLayout;

    @ViewInject(R.id.title_layout)
    RelativeLayout topLayout;

    @ViewInject(R.id.menu)
    FakeDropDownMenu fakeDropDownMenu;

    @ViewInject(R.id.top_menu)
    FakeDropDownMenu topFakeDropDownMenu;

    @ViewInject(R.id.pager)
    RepresentViewPager pager;
    HiRepresentBannerFragmentAdapter bannerAdapter;

    @ViewInject(R.id.represent_banner)
    RepresentBannerView representBannerView;

    GetSearchCateConditionDao getSearchCateConditionDao;
    GetAdspaceListDao getAdspaceListDao;

    public HiRepresentFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiRepresentFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        fakeDropDownMenu.setOnClickListener(listener);
        scrollview.setOnRecyclerScrollViewListener(this);
    }

    @Override
    protected void init() {
        initPtrLayout(ptrFrameLayout);
        setTitlePadding(topLayout);
        initBottomSpace();
        /**
         * 接口请求
         */
        initSearchCateCondition();
        initAdspaceList();
        onRefresh();
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
    }

    private void initBottomSpace(){
        BarryTab tab = (BarryTab) getActivity().findViewById(R.id.tab);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        scrollview.setPadding(0, 0, 0, tab.getMeasuredHeight());
//        scrollview.setLayoutParams(params);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return scrollview.canRefresh();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    public void onRefresh(){
        setCurrentHttpUuid(UUID.randomUUID().toString());
        getSearchCateConditionDao.getSearchCateCondition();
        getAdspaceListDao.getAdspaceList(getCurrentHttpUuid(), AppRuntimeWorker.getCity_id(), AdspaceParams.TYPE_SHOP, AdspaceParams.TERMINAL_TYPE);
    }

    public void loadComplete(){
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        handleFilterBar(t);
        Log.d(tag, "t : " + t);
    }

    @Override
    public void onScrollToEnd() {

    }

    private void handleFilterBar(int t){
        if(t >= scrollLayout.getMeasuredHeight()){
            if(topFakeDropDownMenu.getVisibility() != View.VISIBLE){
                showTopMenu();
            }
            return;
        }
        if(topFakeDropDownMenu.getVisibility() == View.VISIBLE){
            showFakeMenu();
        }
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

    /**
     * 获取banner
     */
    private void initAdspaceList(){
        getAdspaceListDao = new GetAdspaceListDaoImpl(new GetAdspaceListView() {
            @Override
            public void getAdspaceListSuccess(String httpUuid, List<AdspaceListBean.Result.Body> body, String type) {
                initRepresentBanner(body);
                loadComplete();
            }

            @Override
            public void getAdspaceListError(String httpUuid) {
                loadComplete();
            }
        });
    }

    private void initRepresentBanner(List<AdspaceListBean.Result.Body> body){
        representBannerView.init(body);
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
    }

    private void updateCategoryViewPagerParams(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> categories){
        LinearLayout.LayoutParams params = getLineaLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getCategoryViewPagerHeight(categories));
        pager.setLayoutParams(params);
    }

    public void updateCategories(){
        if(null != SearchCateConditionFactory.get()){
            initCategories(SearchCateConditionFactory.get().getCategoryList());
        }
    }

    public void clickMenu(){
        scrollview.smoothScrollTo(0, scrollLayout.getMeasuredHeight());
        showTopMenu();
    }

    private void showTopMenu(){
        topFakeDropDownMenu.setVisibility(View.VISIBLE);
        fakeDropDownMenu.setVisibility(View.GONE);
    }

    private void showFakeMenu(){
        topFakeDropDownMenu.setVisibility(View.GONE);
        fakeDropDownMenu.setVisibility(View.VISIBLE);
    }

    private int getCategoryViewPagerHeight(List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> categories){
        return categories.size() <= 4 ? HiRepresentCateAdapter.getItemHeight() : HiRepresentCateAdapter.getItemHeight() * 2;
    }
}
