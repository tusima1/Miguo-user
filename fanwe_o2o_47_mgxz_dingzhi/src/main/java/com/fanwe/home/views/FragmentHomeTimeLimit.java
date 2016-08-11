package com.fanwe.home.views;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.fragment.BaseFragment;
import com.fanwe.home.adapters.HomeTimeLimitAdapter;
import com.fanwe.model.EventModel_List;
import com.fanwe.model.Index_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 限时特惠
 * Created by Administrator on 2016/8/11.
 */
public class FragmentHomeTimeLimit extends BaseFragment {
    @ViewInject(R.id.recyclerView_fragment_time_limit)
    private RecyclerView mRecyclerView;
    private List<EventModel_List> mListModel = new ArrayList<EventModel_List>();

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_home_time_limit);
    }

    @Override
    protected void init() {
        super.init();
        bindData();
    }

    public void setmIndexModel(Index_indexActModel indexModel) {
        this.mListModel = indexModel.getSpecial().getList();
    }

    private void bindData() {
        if (!toggleFragmentView(mListModel)) {
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                outRect.right = DisplayUtil.dp2px(getActivity(), 9);
            }
        });
        HomeTimeLimitAdapter mHomeTimeLimitAdapter = new HomeTimeLimitAdapter(getActivity(), mListModel);
        mRecyclerView.setAdapter(mHomeTimeLimitAdapter);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
