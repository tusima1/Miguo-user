package com.miguo.category.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiRepresentBannerFragmentAdapter;
import com.miguo.dao.GetAdspaceListDao;
import com.miguo.dao.GetSearchCateConditionDao;
import com.miguo.dao.impl.GetAdspaceListDaoImpl;
import com.miguo.dao.impl.GetSearchCateConditionDaoImpl;
import com.miguo.definition.AdspaceParams;
import com.miguo.definition.IntentKey;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.SearchCateConditionBean;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiRepresentCateFragment;
import com.miguo.ui.view.RepresentBannerView;
import com.miguo.ui.view.RepresentViewPager;
import com.miguo.view.GetAdspaceListView;
import com.miguo.view.GetSearchCateConditionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentFragmentCategory extends FragmentCategory {

    @ViewInject(R.id.title_layout)
    RelativeLayout topLayout;

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

    }

    @Override
    protected void setFragmentListener() {

    }

    @Override
    protected void init() {
        setTitlePadding(topLayout);
        initSearchCateCondition();
        initAdspaceList();
        initRepresentBanner();
        onRefresh();
    }

    public void onRefresh(){
        setCurrentHttpUuid(UUID.randomUUID().toString());
        getSearchCateConditionDao.getSearchCateCondition();
        getAdspaceListDao.getAdspaceList(getCurrentHttpUuid(), AppRuntimeWorker.getCity_id(), AdspaceParams.TYPE_SHOP, AdspaceParams.TERMINAL_TYPE);
    }

    private void initSearchCateCondition(){
        getSearchCateConditionDao = new GetSearchCateConditionDaoImpl(new GetSearchCateConditionView(){
            @Override
            public void getSearchCateConditionError(String message) {

            }

            @Override
            public void getSearchCateConditionSuccess(SearchCateConditionBean.Result.Body body) {
                initCategories(body.getCategoryList());
            }
        });
    }

    private void initAdspaceList(){
        getAdspaceListDao = new GetAdspaceListDaoImpl(new GetAdspaceListView() {
            @Override
            public void getAdspaceListSuccess(String httpUuid, List<AdspaceListBean.Result.Body> body, String type) {

            }

            @Override
            public void getAdspaceListError(String httpUuid) {

            }
        });
    }

    private void initRepresentBanner(){
        representBannerView.init(new ArrayList());
    }

    private void initCategories(List<SearchCateConditionBean.Result.Body.Categories> categories){
        ArrayList<Fragment> fragments = new ArrayList<>();
        int count = categories.size() / 8 + (categories.size() % 8 > 0 ? 1 : 0);
        for(int i = 0; i < count; i++){
            List<SearchCateConditionBean.Result.Body.Categories> current = new ArrayList<>();
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

    private void initBanner(){

    }

}
