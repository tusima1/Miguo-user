package com.fanwe.seller.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanwe.adapter.MerchantListAdapter;
import com.fanwe.fragment.BaseFragment;
import com.fanwe.model.StoreModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我代言的店铺
 * Created by Administrator on 2016/7/29.
 */
public class FragmentMineShopList extends BaseFragment {
    private View view;
    private MerchantListAdapter mAdapter = null;
    private List<StoreModel> mListModel = new ArrayList<StoreModel>();

    @ViewInject(R.id.ptr_listview_fragment_mine_shop_list)
    private PullToRefreshListView mPtrlvContent = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.fragment_mine_shop_list);
    }

    @Override
    protected void init() {
        super.init();
        preParam();
        bindDefaultLvData();
        initPullRefreshLv();
    }

    private void preParam() {
        for (int i = 0; i < 10; i++) {
            StoreModel storeModel = new StoreModel();
            mListModel.add(storeModel);
        }
    }

    private void initPullRefreshLv() {
        mPtrlvContent.setMode(PullToRefreshBase.Mode.BOTH);
        mPtrlvContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                mListModel.clear();
                for (int i = 0; i < 5; i++) {
                    StoreModel storeModel = new StoreModel();
                    mListModel.add(storeModel);
                }
                mAdapter.notifyDataSetChanged();

                mPtrlvContent.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                for (int i = 0; i < 5; i++) {
                    StoreModel storeModel = new StoreModel();
                    mListModel.add(storeModel);
                }
                mAdapter.notifyDataSetChanged();
                mPtrlvContent.onRefreshComplete();
            }
        });
//        mPtrlvContent.setRefreshing();
    }

    private void bindDefaultLvData() {
        mAdapter = new MerchantListAdapter(mListModel, getActivity());
        mPtrlvContent.setAdapter(mAdapter);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
