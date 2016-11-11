package com.miguo.category.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.groupon.model.getFeaturedGroupBuy.ModelFeaturedGroupBuy;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiGrouponFeaturedAdapter;
import com.miguo.dao.FeaturedGrouponDao;
import com.miguo.dao.impl.FeaturedGrouponDaoImpl;
import com.miguo.definition.PageSize;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiHomeFragment;
import com.miguo.view.FeaturedGrouponView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/28.
 * 首页精选推荐列表
 */
public class FeaturedGrouponCategory extends FragmentCategory implements FeaturedGrouponView, HiGrouponFeaturedAdapter.OnItemDataChangedListener{


    @ViewInject(R.id.featured_title_layout)
    LinearLayout featuredTitleLayout;

    @ViewInject(R.id.recyclerview_frag_featured_groupon)
    private RecyclerView recyclerView;
    HiGrouponFeaturedAdapter adapter;

    FeaturedGrouponDao featuredGrouponDao;
    int pageNum;

    public FeaturedGrouponCategory(View view, HiBaseFragment fragment){
        super(view ,fragment);
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
        adapter.setOnItemDataChangedListener(this);
    }

    @Override
    protected void init() {
        initRecyclerView();
    }


    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void onRefresh(){
        setPageNum(PageSize.BASE_NUMBER_ONE);
        featuredGrouponDao.getFeaturedGroupBuy(
                AppRuntimeWorker.getCity_id(),
                String.valueOf(getPageNum()),
                String.valueOf(PageSize.BASE_PAGE_SIZE),
                "",
                BaiduMapManager.getInstance().getLongitude() + "",
                BaiduMapManager.getInstance().getLatitude() + "");
    }

    public void clearPage(){
        if(recyclerView == null || adapter == null){
            return;
        }
        adapter.notifyDataSetChanged(new ArrayList());
        recyclerView.setVisibility(View.GONE);
        featuredTitleLayout.setVisibility(View.GONE);
    }

    public void showPage(){
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void onLoadMore(){
        featuredGrouponDao.getFeaturedGroupBuy(
                AppRuntimeWorker.getCity_id(),
                String.valueOf(getPageNum()),
                String.valueOf(PageSize.BASE_PAGE_SIZE),
                "",
                BaiduMapManager.getInstance().getLongitude() + "",
                BaiduMapManager.getInstance().getLatitude() + "");
    }

    @Override
    public void getFeaturedGrouponSuccess(final List<ModelFeaturedGroupBuy> list) {
        if(list != null && list.size() != 0){
            setPageNum(getPageNum() + 1);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                featuredTitleLayout.setVisibility(SDCollectionUtil.isEmpty(list) ? View.GONE : View.VISIBLE);
                recyclerView.setVisibility(SDCollectionUtil.isEmpty(list) ? View.GONE : View.VISIBLE);
                adapter.notifyDataSetChanged(list);
                getCategory().loadComplete();
                updateFeaturedGrouponViewHeight();
            }
        });
    }

    @Override
    public void getFeaturedGrouponLoadmoreSuccess(final List<ModelFeaturedGroupBuy> list) {
        if(list != null && list.size() != 0){
            setPageNum(getPageNum() + 1);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                featuredTitleLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChangedLoadmore(list);
                getCategory().loadComplete();
                updateFeaturedGrouponViewHeight();
            }
        });
    }

    @Override
    public void onItemChanged() {
        updateFeaturedGrouponViewHeight();
    }

    @Override
    public void getFeaturedGrouponError(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                featuredTitleLayout.setVisibility(View.GONE);
                clearPage();
                getCategory().loadComplete();
            }
        });
    }

    /**
     * 更新推荐商品列表高度
     */
    private void updateFeaturedGrouponViewHeight(){
        int height = adapter.getItemHeight();
        LinearLayout.LayoutParams params = getLineaLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        params.setMargins(0, dip2px(15), 0, 0);
        recyclerView.setLayoutParams(params);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public HiHomeFragmentCategory getCategory(){
        return ((HiHomeFragment)fragment).getCategory();
    }

}
