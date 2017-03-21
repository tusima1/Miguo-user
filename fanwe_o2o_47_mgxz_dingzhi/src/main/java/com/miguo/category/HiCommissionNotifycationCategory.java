package com.miguo.category;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.LoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiCommissionNotificationAdapter;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.MessageListDao;
import com.miguo.dao.impl.MessageListDaoImpl;
import com.miguo.definition.PageSize;
import com.miguo.entity.MessageListBean;
import com.miguo.listener.HiCommissionNotifycationListener;
import com.miguo.view.MessageListView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/16.
 * 佣金代言消息列表
 */
public class HiCommissionNotifycationCategory extends Category {

    @ViewInject(R.id.ptr_layout)
    PtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    BarryBaseRecyclerAdapter adapter;

    MessageListDao messageListDao;

    public HiCommissionNotifycationCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        initMessageListDao();
        adapter = new HiCommissionNotificationAdapter(getActivity(), new ArrayList());
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiCommissionNotifycationListener(this);
    }

    @Override
    protected void setThisListener() {
        recyclerView.setOnRefreshEndListener((HiCommissionNotifycationListener)listener);
    }

    @Override
    protected void init() {
        initPtrLayout(ptrFrameLayout);
    }

    @Override
    protected void initViews() {
        initRecyclerView();
        onRefresh();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return recyclerView.isRefreshAble();
    }

    private void initMessageListDao(){
        messageListDao = new MessageListDaoImpl(new MessageListView() {
            @Override
            public void getMessageListSuccess(List<MessageListBean.Result.Body> messages) {
                adapter.notifyDataSetChanged(messages);
                loadComplete();
            }

            @Override
            public void getMessageListLoadmoreSuccess(List<MessageListBean.Result.Body> messages) {
                adapter.notifyDataSetChangedLoadmore(messages);
                loadComplete();
            }

            @Override
            public void getMessageListError(String message) {
                showToast(message);
                loadComplete();
            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void loadComplete(){
        ptrFrameLayout.refreshComplete();
        recyclerView.loadComplete();
    }

    public void onRefresh(){
        messageListDao.getCommissionMessageList(PageSize.BASE_NUMBER_ONE, PageSize.BASE_PAGE_SIZE, App.getInstance().getToken());
    }

    public void onLoadmore() {
        messageListDao.getCommissionMessageList(getNextPage(), PageSize.BASE_PAGE_SIZE, App.getInstance().getToken());
    }

    public int getNextPage(){
        return adapter.getItemCount() % PageSize.BASE_PAGE_SIZE > 0 ? -1 : (adapter.getItemCount() / PageSize.BASE_PAGE_SIZE) + 1;
    }

}
