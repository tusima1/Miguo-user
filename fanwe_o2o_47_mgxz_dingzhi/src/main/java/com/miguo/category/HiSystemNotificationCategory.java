package com.miguo.category;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.LoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiSystemNotificationAdapter;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.MessageListDao;
import com.miguo.dao.impl.MessageListDaoImpl;
import com.miguo.definition.PageSize;
import com.miguo.entity.MessageListBean;
import com.miguo.listener.HiSystemNotificationListener;
import com.miguo.view.MessageListView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/15.
 *  * 系统消息列表
 */
public class HiSystemNotificationCategory extends Category {

    @ViewInject(R.id.ptr_layout)
    PtrFrameLayout ptrFrameLayout;

    @ViewInject(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    HiSystemNotificationAdapter adapter;

    MessageListDao messageListDao;

    public HiSystemNotificationCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        adapter = new HiSystemNotificationAdapter(getActivity(), new ArrayList());
        initMessageListDao();
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiSystemNotificationListener(this);
    }

    @Override
    protected void setThisListener() {
        recyclerView.setOnRefreshEndListener((HiSystemNotificationListener)listener);
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

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return recyclerView.isRefreshAble();
    }

    public void loadComplete(){
        ptrFrameLayout.refreshComplete();
        recyclerView.loadComplete();
    }

    public void onRefresh(){
        messageListDao.getSystemMessageList(PageSize.BASE_NUMBER_ONE, PageSize.BASE_PAGE_SIZE,App.getInstance().getToken());
    }

    public void onLoadmore() {
        messageListDao.getSystemMessageList(getNextPage(), PageSize.BASE_PAGE_SIZE,App.getInstance().getToken());
    }


    public int getNextPage(){
        return adapter.getItemCount() % PageSize.BASE_PAGE_SIZE > 0 ? -1 : (adapter.getItemCount() / PageSize.BASE_PAGE_SIZE) + 1;
    }
}
