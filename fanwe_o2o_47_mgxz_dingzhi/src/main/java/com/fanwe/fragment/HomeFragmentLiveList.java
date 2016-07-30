package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.fanwe.adapter.HomeLiveListAdapter;
import com.fanwe.customview.SDGridViewInScroll;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class HomeFragmentLiveList extends BaseFragment {
    private View view;
    private SDGridViewInScroll mSDGridViewInScroll;
    private HomeLiveListAdapter mLiveViewAdapter;
    private ArrayList<Room> datas = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 可以初始化除了view之外的东西
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            initView(inflater, container);
            preParam();
            preView();
            setListener();
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从mView删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void preView() {
        mLiveViewAdapter = new HomeLiveListAdapter(getActivity(), getActivity().getLayoutInflater(), datas);
        mSDGridViewInScroll.setAdapter(mLiveViewAdapter);
    }

    private void setListener() {

    }

    private void preParam() {

    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = (View) inflater
                .inflate(R.layout.fragment_home_live_list, container, false);
        mSDGridViewInScroll = (SDGridViewInScroll) view.findViewById(R.id.gridView_home_fragment_list);

    }

    public void updateView(boolean isRefresh, List<Room> rooms) {
        if (isRefresh) {
            datas.clear();
        }
        if (!SDCollectionUtil.isEmpty(rooms))
            datas.addAll(rooms);
        mLiveViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
