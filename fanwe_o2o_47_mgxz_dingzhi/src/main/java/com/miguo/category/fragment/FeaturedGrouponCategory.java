package com.miguo.category.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiGrouponFeaturedAdapter;
import com.miguo.dao.FeaturedGrouponDao;
import com.miguo.dao.impl.FeaturedGrouponDaoImpl;
import com.miguo.definition.PageSize;
import com.miguo.entity.ModelFeaturedGroupBuy;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiHomeFragment;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.view.FeaturedGrouponView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 * 首页精选推荐列表
 */
public class FeaturedGrouponCategory extends FragmentCategory implements FeaturedGrouponView{


    @ViewInject(R.id.featured_title_layout)
    LinearLayout featuredTitleLayout;

    @ViewInject(R.id.recyclerview_frag_featured_groupon)
    private RecyclerView recyclerView;
    HiGrouponFeaturedAdapter adapter;

    FeaturedGrouponDao featuredGrouponDao;
    int pageNum;

    boolean nodata = false;

    public FeaturedGrouponCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        featuredGrouponDao = new FeaturedGrouponDaoImpl(this);
        adapter = new HiGrouponFeaturedAdapter(getActivity(), new ArrayList());
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
        initRecyclerView();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void onRefresh() {
        setPageNum(PageSize.BASE_NUMBER_ONE);
        setNodata(false);
        featuredGrouponDao.getFeaturedGroupBuy(getCategory().getCurrentHttpUuid(),
                AppRuntimeWorker.getCity_id(),
                String.valueOf(getPageNum()),
                String.valueOf(PageSize.BASE_PAGE_SIZE),
                "",
                BaiduMapManager.getInstance().getLongitude() + "",
                BaiduMapManager.getInstance().getLatitude() + "");
    }

    public void clearPage() {
        if (recyclerView == null || adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged(new ArrayList());
        recyclerView.setVisibility(View.GONE);
        featuredTitleLayout.setVisibility(View.GONE);
    }

    public void showPage() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void onLoadMore() {
        if (isNodata()) {
            getCategory().loadCompleteWithNoData();
            return;
        }
        featuredGrouponDao.getFeaturedGroupBuy(getCategory().currentHttpUuid,
                AppRuntimeWorker.getCity_id(),
                String.valueOf(getPageNum()),
                String.valueOf(PageSize.BASE_PAGE_SIZE),
                "",
                BaiduMapManager.getInstance().getLongitude() + "",
                BaiduMapManager.getInstance().getLatitude() + "");
    }

    @Override
    public void getFeaturedGrouponSuccess(final String httpUuid,final List<ModelFeaturedGroupBuy> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getCategory().isCurrentHttp(httpUuid)){
                    if (list != null && list.size() != 0) {
                        setPageNum(getPageNum() + 1);
                    }
                    featuredTitleLayout.setVisibility(SDCollectionUtil.isEmpty(list) ? View.GONE : View.VISIBLE);
                    recyclerView.setVisibility(SDCollectionUtil.isEmpty(list) ? View.GONE : View.VISIBLE);
                    adapter.notifyDataSetChanged(list);
                    getCategory().loadCompleteWithLoadmore();
                    return;
                }
                getCategory().loadComplete();
            }

        });
    }

    @Override
    public void getFeaturedGrouponLoadmoreSuccess(final String httpUuid, final List<ModelFeaturedGroupBuy> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getCategory().isCurrentHttp(httpUuid)){
                    if (list != null && list.size() != 0) {
                        setPageNum(getPageNum() + 1);
                    }
                    featuredTitleLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (SDCollectionUtil.isEmpty(list)) {
                        getCategory().loadCompleteWithNoData();
                        setNodata(true);
                    } else {
                        adapter.notifyDataSetChangedLoadmore(list);
                        getCategory().loadCompleteWithLoadmore();
                    }
                    return;
                }
                getCategory().loadComplete();

            }
        });

    }

    @Override
    public void getFeaturedGrouponError(final String httpUuid, String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getCategory().isCurrentHttp(httpUuid)){
                    featuredTitleLayout.setVisibility(View.GONE);
                    clearPage();
                    getCategory().loadCompleteWithError();
                    return;
                }
                getCategory().loadComplete();
            }
        });
    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public boolean isNodata() {
        return nodata;
    }

    public void setNodata(boolean nodata) {
        this.nodata = nodata;
    }

    public HiHomeFragmentCategory getCategory() {
        return ((HiHomeFragment) fragment).getCategory();
    }

}
