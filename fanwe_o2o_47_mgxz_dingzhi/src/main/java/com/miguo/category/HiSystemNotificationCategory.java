package com.miguo.category;

import android.support.v7.widget.LinearLayoutManager;

import com.fanwe.o2o.miguo.R;
import com.fanwe.view.LoadMoreRecyclerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HiSystemNotificationAdapter;
import com.miguo.app.HiBaseActivity;
import com.miguo.listener.HiSystemNotificationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/15.
 *  * 系统消息列表
 */
public class HiSystemNotificationCategory extends Category {


    @ViewInject(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    HiSystemNotificationAdapter adapter;

    public HiSystemNotificationCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        adapter = new HiSystemNotificationAdapter(getActivity(), new ArrayList());
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

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        initRecyclerView();
        notifyDataChanged();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void notifyDataChanged(){
        List list = new ArrayList();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        adapter.notifyDataSetChanged(list);
    }

}
