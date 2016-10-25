package com.fanwe.groupon.views;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.base.CallbackView;
import com.fanwe.groupon.adapters.GrouponFeaturedAdapter;
import com.fanwe.groupon.model.GrouponConstants;
import com.fanwe.groupon.model.getFeaturedGroupBuy.ModelFeaturedGroupBuy;
import com.fanwe.groupon.presenters.GrouponHttpHelper;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;

import java.util.ArrayList;
import java.util.List;

/**
 * 今日精选
 */
public class FragmentFeaturedGroupon extends Fragment implements CallbackView {
    private GrouponHttpHelper grouponHttpHelper;
    private RecyclerView recyclerView;
    private GrouponFeaturedAdapter mGrouponFeaturedAdapter;
    private List<ModelFeaturedGroupBuy> datas = new ArrayList<>();
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private String keyword;
    private String m_longitude;
    private String m_latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_featured_groupon, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_frag_featured_groupon);
        setView();
        return view;
    }

    private void getData() {
        if (grouponHttpHelper == null) {
            grouponHttpHelper = new GrouponHttpHelper(getActivity(), this);
        }
        grouponHttpHelper.getFeaturedGroupBuy(AppRuntimeWorker.getCity_id(), String.valueOf(pageNum), String.valueOf(pageSize), keyword, m_longitude, m_latitude);
    }

    private void setView() {
        mGrouponFeaturedAdapter = new GrouponFeaturedAdapter(getActivity(), datas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mGrouponFeaturedAdapter);
    }

    public void setData(List<ModelFeaturedGroupBuy> models) {
        datas.clear();
        datas.addAll(models);
        if (mGrouponFeaturedAdapter != null)
            mGrouponFeaturedAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        isRefresh = true;
        pageNum = 1;
        getData();
    }

    /**
     * 加载更多数据
     */
    public void loadMore() {
        isRefresh = false;
        if (!SDCollectionUtil.isEmpty(items)) {
            pageNum++;
        }
        getData();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private List<ModelFeaturedGroupBuy> items;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (GrouponConstants.FEATURED_GROUP_BUG.equals(method)) {
            items = datas;
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRefresh) {
                        datas.clear();
                    }
                    if (!SDCollectionUtil.isEmpty(items)) {
                        datas.addAll(items);
                    }
                    mGrouponFeaturedAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
