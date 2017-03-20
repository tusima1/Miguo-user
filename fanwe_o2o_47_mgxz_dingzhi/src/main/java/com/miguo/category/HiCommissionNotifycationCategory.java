package com.miguo.category;

import android.support.v7.widget.LinearLayoutManager;

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
import com.miguo.entity.MessageListBean;
import com.miguo.listener.HiCommissionNotifycationListener;
import com.miguo.view.MessageListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/16.
 * 佣金代言消息列表
 */
public class HiCommissionNotifycationCategory extends Category {

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

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        initRecyclerView();
        onRefresh();
    }

    private void initMessageListDao(){
        messageListDao = new MessageListDaoImpl(new MessageListView() {
            @Override
            public void getMessageListSuccess(List<MessageListBean.Result.Body> messages) {
                adapter.notifyDataSetChanged(messages);
            }

            @Override
            public void getMessageListError(String message) {
                showToast(message);
            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void onRefresh(){
        messageListDao.getCommissionMessageList(App.getInstance().getToken());
    }
}
