package com.fanwe.seller.views.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fanwe.base.CallbackView;
import com.fanwe.fragment.BaseFragment;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.adapters.ShopListAdapter;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.getRepresentMerchant.RootRepresentMerchant;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 我代言的店铺
 * Created by Administrator on 2016/7/29.
 */
public class FragmentMineShopList extends BaseFragment implements CallbackView {
    private View view;
    private ShopListAdapter mAdapter = null;
    private List<ModelStoreList> mListModel = new ArrayList<>();

    @ViewInject(R.id.ptr_listview_fragment_mine_shop_list)
    private PullToRefreshListView mPtrlvContent = null;

    private SellerHttpHelper sellerHttpHelper;

    private int pageSize = 10;
    private int pageNum = 1;
    private boolean isRefresh = true;
    private int type;
    private boolean isNotMine;

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
        type = getArguments().getInt("type");
        if (type == 1) {
            isNotMine = false;
        } else {
            isNotMine = true;
        }
        sellerHttpHelper = new SellerHttpHelper(getActivity(), this);
        getData();
        bindDefaultLvData();
        initPullRefreshLv();
        setListener();
    }

    ModelStoreList tempBean;

    private void setListener() {
        mPtrlvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempBean = mListModel.get(position - 1);
                if (isNotMine) {
                    //代言
                    sellerHttpHelper.getRepresentMerchant(tempBean.getEnt_id());
                } else {
                    CurLiveInfo.modelShop = tempBean;
                    getActivity().setResult(8888);
                    getActivity().finish();
                }
            }
        });
    }

    private void getData() {
        sellerHttpHelper.getStoreList(pageNum, pageSize, type + "", AppRuntimeWorker.getCity_id());
    }

    private void initPullRefreshLv() {
        mPtrlvContent.setMode(PullToRefreshBase.Mode.BOTH);
        mPtrlvContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = true;
                pageNum = 1;
                mListModel.clear();
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                if (!SDCollectionUtil.isEmpty(mListModel)) {
                    pageNum++;
                }
                getData();
            }
        });
    }

    private void bindDefaultLvData() {
        mAdapter = new ShopListAdapter(mListModel, getActivity(), isNotMine);
        mPtrlvContent.setAdapter(mAdapter);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private ArrayList<ModelStoreList> temps;
    List<RootRepresentMerchant> roots;

    @Override
    public void onSuccess(String method, List datas) {
        Message message = new Message();
        if (SellerConstants.STORE_LIST.equals(method)) {
            temps = (ArrayList<ModelStoreList>) datas;
            message.what = 0;
        } else if (SellerConstants.REPRESENT_MERCHANT.equals(method)) {
            roots = datas;
            message.what = 1;
        }
        mHandler.sendMessage(message);

    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //在主线程线程中更新数据
                    if (!SDCollectionUtil.isEmpty(temps)) {
                        mListModel.addAll(temps);
                    } else {
                        if (!isNotMine) {
                            MGToast.showToast("代言店铺列表为空");
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    mPtrlvContent.onRefreshComplete();
                    break;
                case 1:
                    if (!SDCollectionUtil.isEmpty(roots)) {
                        //代言失败
                        MGToast.showToast(roots.get(0).getMessage());
                        getActivity().finish();
                    } else {
                        CurLiveInfo.modelShop = tempBean;
                        getActivity().setResult(8888);
                        getActivity().finish();
                    }
                    break;
            }
        }
    };
}
