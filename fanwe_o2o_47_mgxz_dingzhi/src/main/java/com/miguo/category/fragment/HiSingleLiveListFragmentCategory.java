package com.miguo.category.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.LoadMoreRecyclerView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiFunnyLiveListAdapter;
import com.miguo.dao.InterestingLiveVideoDao;
import com.miguo.dao.impl.InterestingLiveVideoDaoImpl;
import com.miguo.definition.PageSize;
import com.miguo.entity.HiFunnyLiveVideoBean;
import com.miguo.entity.InterestingLiveVideoBean;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HiSingleLiveListFragment;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.view.InterestingLiveVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class HiSingleLiveListFragmentCategory extends FragmentCategory implements InterestingLiveVideoView, SwipeRefreshLayout.OnRefreshListener,LoadMoreRecyclerView.OnRefreshEndListener {

    InterestingLiveVideoDao interestingLiveVideoDao;

    @ViewInject(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;
    HiFunnyLiveListAdapter adapter;

    @ViewInject(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshLayout;
    int pageNum;

    public HiSingleLiveListFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void initFirst() {
        adapter = new HiFunnyLiveListAdapter(getActivity(), new ArrayList());
        interestingLiveVideoDao = new InterestingLiveVideoDaoImpl(this);
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
        recyclerView.setOnRefreshEndListener(this);
    }

    @Override
    protected void init() {
        initSwipeRefreshLayout();
        initRecyclerView();
        initFirstIitem();
    }

    private void initFirstIitem(){
        if(getFragment().isFirstItem()){
            onRefresh();
        }
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    public void onRefresh(){
        setPageNum(PageSize.BASE_NUMBER_ONE);
        getInterestingLiveVideo();
    }

    private void getInterestingLiveVideo(){
        swipeRefreshLayout.setRefreshing(true);
        interestingLiveVideoDao.getInterestingLiveVideo(getPageNum(), PageSize.BASE_PAGE_SIZE, getFragment().getTab_id(), AppRuntimeWorker.getCity_id());
    }

    @Override
    public void onLoadmore() {
        getInterestingLiveVideo();
    }

    @Override
    public void onMoveTop() {

    }

    public void loadComplete(){
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setLoading(false);
    }

    public HiSingleLiveListFragment getFragment(){
        return (HiSingleLiveListFragment)fragment;
    }

    @Override
    public void getInterestingLiveVideoError() {
        loadComplete();
    }

    @Override
    public void getInterestingLiveVideoSuccess(List<ModelRoom> liveList) {
        if(!SDCollectionUtil.isEmpty(liveList)){
            setPageNum(getPageNum() + 1);
        }
        adapter.notifyDataSetChanged(liveList);
        loadComplete();
    }

    @Override
    public void getInterestingLiveVideoLoadmoreSuccess(List<ModelRoom> liveList) {
        if(!SDCollectionUtil.isEmpty(liveList)) {
            setPageNum(getPageNum() + 1);
        }
        adapter.notifyDataSetChangedLoadmore(liveList);
        loadComplete();
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
